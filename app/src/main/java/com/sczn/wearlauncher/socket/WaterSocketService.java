package com.sczn.wearlauncher.socket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.LogFile;
import com.sczn.wearlauncher.location.LocationCallBackHelper;
import com.sczn.wearlauncher.receiver.NetWorkReceiver;
import com.sczn.wearlauncher.receiver.TimeChangeReceiver;
import com.sczn.wearlauncher.socket.command.CmdCode;
import com.sczn.wearlauncher.socket.command.CmdGprsReceive;
import com.sczn.wearlauncher.socket.command.CommandHelper;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.gprs.post.LK;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;
import com.sczn.wearlauncher.util.NetworkUtils;
import com.sczn.wearlauncher.util.ThreadUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Description:
 * Created by Bingo on 2019/1/16.
 */
public class WaterSocketService extends Service {

    private final String TAG = getClass().getSimpleName();

    private LocalBinder binder = new LocalBinder();

    private Socket mSocket;
    //心跳机制
    private static final int HEART_BEAT_RATE = 10;
    private static final long HEART_BEAT_RATE_TIME = 300 * 1000;//定义5分钟秒检查一次
    private long lastSendTime;

    private NetWorkReceiver netWorkReceiver;
    private TimeChangeReceiver timeChangeReceiver;

    //是否重新初始化Socket
    private boolean isReConnectSocket = false;

    public class LocalBinder extends Binder {
        public WaterSocketService getService() {
            return WaterSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        MxyLog.d(TAG, "onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MxyLog.d(TAG, "onCreate");
        initSocket();
        // 发送心跳检查
        if (mHandler != null) {
            if (mHandler.hasMessages(HEART_BEAT_RATE)) {
                mHandler.removeMessages(HEART_BEAT_RATE);
            }
            mHandler.sendEmptyMessageDelayed(HEART_BEAT_RATE, HEART_BEAT_RATE_TIME);
        }

        /**
         *每分钟系统时间监听
         */
        timeChangeReceiver = new TimeChangeReceiver();
        IntentFilter refreshFilter = new IntentFilter();
        refreshFilter.addAction("refresh_background_service");
        // refreshFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeChangeReceiver, new IntentFilter(refreshFilter));
        Intent in = new Intent("refresh_background_service");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, in, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pendingIntent);
        }

        // 注册网络变化监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(NetWorkReceiver.ACTION_KEY_EVENT);
        //电话状态
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        netWorkReceiver = new NetWorkReceiver();
        registerReceiver(netWorkReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MxyLog.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Handler
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HEART_BEAT_RATE:
                    long t = System.currentTimeMillis() - lastSendTime;
                    if (t > 60000) {
                        MxyLog.d(TAG, "设备1分钟内没有调用send方法,需要发送心跳包,维持连接");
                        mHandler.postDelayed(heartRunnable, 0);
                    } else {
                        MxyLog.d(TAG, "不需要维持:" + t);
                    }
                    mHandler.sendEmptyMessageDelayed(HEART_BEAT_RATE, HEART_BEAT_RATE_TIME);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private String mBuffer = "";

    /**
     * 读取socket的线程
     */
    public class ReadSocketThread extends Thread {

        private ReadSocketThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            super.run();
            try {
                if (mSocket != null) {
                    InputStream is = mSocket.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length;
                    while (!mSocket.isClosed() && !mSocket.isInputShutdown() && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bos.write(buffer, 0, length);
                            String receiverMessage = bos.toString();
                            if (receiverMessage.startsWith(CmdCode.START)) {
                                mBuffer = null;
                                mBuffer = receiverMessage;
                                if (receiverMessage.endsWith(CmdCode.END)) {
                                    dealMsg(mBuffer, bos.toByteArray());
                                }
                            } else {
                                mBuffer = mBuffer + receiverMessage;
                                if (receiverMessage.endsWith(CmdCode.END)) {
                                    dealMsg(mBuffer, bos.toByteArray());
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                mSocket = null;
                e.printStackTrace();
            }
        }
    }

    //防止粘包
    private void dealMsg(String msg, byte[] array) {
        if (msg.startsWith(CmdCode.START)) {
            CmdGprsReceive.onResult(msg, array);
            mBuffer = "";
        } else {
            mBuffer = "";
        }
    }

    /**
     * 初始化Socket
     */
    private void initSocket() {
        if (!NetworkUtils.isNetWorkConnected(LauncherApp.getAppContext())) {
            MxyLog.d(TAG, "init socket not network connected.");
            return;
        }
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                doOnConnectSocket();
            }
        });
    }

    /**
     * 连接socket
     */
    private void doOnConnectSocket() {
        try {
            // 设置socket连接时间为8秒
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(SocketConstant.getHost(), SocketConstant.getPort()), 8000);
            boolean connect = mSocket.isConnected();
            MxyLog.d(TAG, "initSocket:" + connect);
            if (connect) {
                ReadSocketThread mReadThread = new ReadSocketThread("ReadSocketThread");
                mReadThread.start();
                // mHandler.postDelayed(heartRunnable, 2000);
                /**
                 * 重新初始化,需要发送一次心跳
                 */
                if (isReConnectSocket) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NetWorkReceiver.lk();
                        }
                    }, 2000);
                    isReConnectSocket = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getmSocket() {
        return mSocket;
    }

    /**
     * 重新初始化socket
     */
    public void reInitSocket() {
        MxyLog.d(TAG, "重新初始化socket");
        isReConnectSocket = true;
        releaseSocket();
        initSocket();
    }

    /**
     * 释放socket
     */
    public void releaseSocket() {
        MxyLog.d(TAG, "releaseSocket");
        try {
            if (null != mSocket && !mSocket.isClosed()) {
                mSocket.close();
                mSocket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送心跳状态包
     */
    private Runnable heartRunnable = new Runnable() {
        @Override
        public void run() {
            MxyLog.d(TAG, "一个心跳包检测..");
            LogFile.logCatWithTimeWithThread(">>>>>>定义5分钟秒检查一次心跳," + "net state:" + NetworkUtils.isNetworkAvailable(LauncherApp.getAppContext()));
            WaterSocketManager.getInstance().send(new LK(Config.step, 0, Config.battery, new CommandResultCallback() {
                @Override
                public void onSuccess(String baseObtain) {
                    MxyLog.i(TAG, "心跳包检测onSuccess");
                }

                @Override
                public void onFail() {
                    MxyLog.i(TAG, "心跳包检测onFail");
                }
            }));
        }
    };


    /**
     * 发送数据
     * 注意:发送数据需要异步发送
     *
     * @param command
     */
    public boolean send(final BaseCommand command) {
        MxyLog.d(TAG, "send:" + command.send());
        if (command.send() == null || command.send().isEmpty()) {
            MxyLog.d(TAG, "command isEmpty...");
            return false;
        }
        if (!NetworkUtils.isNetWorkConnected(LauncherApp.getAppContext())) {
            MxyLog.d(TAG, "send command not network connected.");
            return false;
        }
        if (null == mSocket) {
            MxyLog.w(TAG, "mSocket==null");
            return false;
        }
        if (!command.send().contains("power")) {//心跳
            lastSendTime = System.currentTimeMillis();
        }
        if (!mSocket.isClosed() && !mSocket.isOutputShutdown()) {
            OutputStream os;
            try {
                os = mSocket.getOutputStream();
                if (command.getCmd() != null && command.getCmd().equals(CmdCode.TK)) {
                    if (command.getData() != null && command.getData().length > 0) {
                        MxyLog.d(TAG, "" + CommandHelper.getInstance().hexString(command.getData()));
                        os.write(command.getData());
                    }
                } else {
                    byte[] bytes = CommandHelper.getInstance().stringToBytes(command.send() + "\r\n");
                    if (bytes != null) {
                        os.write(bytes);
                    }
                }
                os.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                mSocket = null;
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseSocket();
        unregisterReceiver(netWorkReceiver);
        unregisterReceiver(timeChangeReceiver);
        LocationCallBackHelper.getInstance().stopLocation();
    }
}

package com.sczn.wearlauncher.ruisocket;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.contact.ContactHelper;
import com.sczn.wearlauncher.util.FileHelper;
import com.sczn.wearlauncher.util.NetworkUtils;
import com.sczn.wearlauncher.util.ThreadUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Arrays;


public class SocketService extends Service {
    private String TAG = "SocketService";

    private String mListLocateProfile = null;
    private WakeLock mWakeLock = null; // 电源锁

    /**
     * 弱引用 在引用对象的同时允许对垃圾对象进行回收
     */
    private WeakReference<Socket> mSocket;

    private ReadThread mReadThread;

    // handler data
    public static int RELEASE_WAKE_LOCK = 1;

    /**
     * 主机IP地址
     */
    public String host = "116.62.215.244";
    /**
     * 端口号
     */
    public int port = 8886;

    private long statusTime = 0L;// 状态包发送时间
    private long serverRespondTime = 0L;// 服务器接收数据时间

    // 状态包发送间隔时间
    private int perStatusTime = 3 * 55 * 1000 - 5000;
    // 定位间隔时间
    private int perLocateTime = 0;

    private LocateDatabaseUtil mDatabaseUtil;
    private HeartDatabaseUtil mHeartRateDatabaseUtil;
    private boolean isLocating = false;
    private int wakeupTimes = 0;
    private boolean isLocateSuccess = false;
    private String imei = null;

    private String peopleImage = "http://s1.zjrt9999.com:8090//UploadFiles/Heading/ContactUser/User_";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabaseUtil = new LocateDatabaseUtil(this);
        mHeartRateDatabaseUtil = new HeartDatabaseUtil(this);
        imei = Util.getIMEI(this);

        /**
         * 检测网络变化广播
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(netStatusBroadcastReceiver, filter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MxyLog.i(TAG, "wakeupTimes:" + wakeupTimes + ",isNetworkAvailable:" + NetworkUtils.isNetworkAvailable(SocketService.this));
        mHandler.sendEmptyMessage(RELEASE_WAKE_LOCK);
        if (NetworkUtils.isNetworkAvailable(SocketService.this)
                && (System.currentTimeMillis() - serverRespondTime >= 5 * 60 * 1000)) {
            mHandler.removeMessages(4);
            mHandler.sendEmptyMessage(4);
        }

        if (perLocateTime == 30 * 60) {
            if ((wakeupTimes * 3) % 30 == 0) {
                stopLocation();
                initLocation();
                startLocation();
                isLocating = true;
            }
        } else if (perLocateTime == 60 * 60) {
            if ((wakeupTimes * 3) % 60 == 0) {
                stopLocation();
                initLocation();
                startLocation();
                isLocating = true;
            }
        } else if (perLocateTime == 120 * 60) {
            if ((wakeupTimes * 3) % 120 == 0) {
                stopLocation();
                initLocation();
                startLocation();
                isLocating = true;
            }
        }

        if (isLocateSuccess) {
            wakeupTimes++;
        }

        if (NetworkUtils.isNetworkAvailable(SocketService.this)) {
            if (System.currentTimeMillis() - statusTime >= perStatusTime) {
                sendRunnable("0016");
                mHandler.post(statusRunnable);
            }

            if (!TextUtils.isEmpty(mDatabaseUtil.querydata())) {
                mHandler.post(locateRunnable);
            }

            if (!TextUtils.isEmpty(mHeartRateDatabaseUtil.querydata())) {
                sendRunnable("001E");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReadThread != null) {
            mReadThread.release();
        }
        releaseLastSocket(mSocket);
        unregisterReceiver(netStatusBroadcastReceiver);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
				/*acquireWakeLock();

				if (isLocating) {
					mHandler.removeMessages(1);
					mHandler.sendEmptyMessageDelayed(1, 1000);
				} else {
					mHandler.removeMessages(2);
					if (System.currentTimeMillis() - serverRespondTime >= 5 * 60 * 1000) {
						mHandler.sendEmptyMessageDelayed(2, 3000);
					} else {
						mHandler.sendEmptyMessageDelayed(2, 1000);
					}

				}*/
                    break;
                case 2:
                    //releaseWakeLock();
                    break;
                case 3:
                    new InitSocketThread().start();
                    break;
                case 4:
                    break;
                case 6:
                    // showBindDialog(bindSquence, bindPhone);
                    break;
                case 8:
                    break;
                case 9:
                    sendBroadcast(new Intent("wearable_contactAvatar_change"));
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    //响应服务器包
    private void respondRunnable(final String serverSquence, final String string) {
        mHandler.post(new RespondRunnable(string) {
            @Override
            public void run() {
                boolean isSuccess = sendMsg(serverSquence, string);// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    mHandler.removeCallbacks(this);
                    if (mReadThread != null) {
                        mReadThread.release();
                    }
                    releaseLastSocket(mSocket);
                    new InitSocketThread().start();
                }
            }
        });
    }

    //响应服务器包
    private void sendRunnable(final String string) {
        mHandler.post(new RespondRunnable(string) {
            @Override
            public void run() {
                boolean isSuccess = sendMsg(null, string);// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    mHandler.removeCallbacks(this);
                    if (mReadThread != null) {
                        mReadThread.release();
                    }
                    releaseLastSocket(mSocket);
                    new InitSocketThread().start();
                }
            }
        });
    }

    // 发送定位包
    private Runnable locateRunnable = new Runnable() {
        @Override
        public void run() {
            boolean isSuccess = sendMsg(null, "0003");// 就发送一个\r\n过去,
            // 如果发送失败，就重新初始化一个socket
            if (!isSuccess) {
                mHandler.removeCallbacks(locateRunnable);
                if (mReadThread != null)
                    mReadThread.release();
                releaseLastSocket(mSocket);
                //new InitSocketThread().start();
            }
        }
    };

    // 发送心跳/状态包
    private Runnable statusRunnable = new Runnable() {
        @Override
        public void run() {
            boolean isSuccess = sendMsg(null, "0002");// 如果发送失败，就重新初始化一个socket
            if (!isSuccess) {
                mHandler.removeCallbacks(statusRunnable);
                if (mReadThread != null)
                    mReadThread.release();
                releaseLastSocket(mSocket);
                new InitSocketThread().start();
            }
        }
    };

    // 登入线程
    private Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            boolean isSuccess = sendMsg(null, "0001");// 就发送一个\r\n过去,
            // 如果发送失败，就重新初始化一个socket
            if (!isSuccess) {
                mHandler.removeCallbacks(loginRunnable);
                if (mReadThread != null)
                    mReadThread.release();
                releaseLastSocket(mSocket);
                new InitSocketThread().start();
            }
        }
    };

    /**
     * 初始化socket
     */
    private void initSocket() {
        try {
            Socket socket = new Socket(host, port);
            mSocket = new WeakReference<>(socket);
            mReadThread = new ReadThread(socket);
            mReadThread.start();
            mHandler.post(loginRunnable);// 初始化成功后，就准备发送心跳包
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放socket
     *
     * @param mSocket
     */
    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk != null && !sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化线程
    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            if (NetworkUtils.isNetworkAvailable(SocketService.this)) {
                initSocket();
            }
        }
    }

    // ***********************************数据发送与接收

    /**
     * 发送数据
     *
     * @param serverSquence
     * @param msg
     * @return
     */
    public boolean sendMsg(String serverSquence, String msg) {
        if (!NetworkUtils.isNetworkAvailable(SocketService.this) || imei == null) {
            return false;
        }
        String msg1 = "";
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        try {
            if (!soc.isClosed() && !soc.isOutputShutdown()) {
                OutputStream os = soc.getOutputStream();
                os.write(Util.toBytes(msg1));
                os.flush();
                mHandler.sendEmptyMessage(RELEASE_WAKE_LOCK);
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        MxyLog.d("----------send to server-------", msg1);
        return true;
    }

    String mBuffer = "";

    /**
     * 接收线程
     */
    public class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        private ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(mWeakSocket);
        }

        @Override
        public void run() {
            super.run();
            Socket socket = mWeakSocket.get();
            if (null != socket) {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            String receiverMessage = Util.hexString(Arrays.copyOf(buffer, length)).trim();
                            MxyLog.d("----------get from server-------", receiverMessage);
                            //将受到的数据放进缓存。确保缓存必须是 2A2A开头
                            if (receiverMessage.startsWith("2A2A")) {//2A2A开头直接放进去
                                mBuffer = null;
                                mBuffer = receiverMessage;
                                dealMsg(mBuffer);
                            } else {//不是2A2A开头拼接
                                mBuffer = mBuffer + receiverMessage;
                                dealMsg(mBuffer);
                            }
                            serverRespondTime = System.currentTimeMillis();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //防止粘包
    private void dealMsg(String msg) {
        if (msg.startsWith("2A2A")) {//2A2A开头处理，不是则清空
            int messageL = Integer.parseInt(msg.substring(4, 8), 16) * 2;
            if (messageL == msg.length()) {//包长度等长度
                dataDeal(msg);
                mBuffer = "";

            } else if (messageL < msg.length()) {//包长小于长度
                dataDeal(msg.substring(0, messageL));
                mBuffer = msg.substring(messageL, msg.length());
                dealMsg(mBuffer);
            }
            //包长大于长度，不做处理
        } else {//不是2A2A开头则清空
            mBuffer = "";
        }
    }

    //处理接收到的数据
    private void dataDeal(String msg) {
        String flag = msg.substring(8, 12);
        String isLogin = msg.substring(32, 34);
        if (isLogin.equals("FF")) {
            mHandler.post(loginRunnable);
        }
        MxyLog.i("---dataDeal---", "1001" + msg.substring(8, 12) + " " + msg);
        if (flag.equals("1001")) {//登录包响应

            MxyLog.i("---dataDeal---", "1001" + msg.substring(msg.length() - 6, msg.length() - 4));
            mHandler.sendEmptyMessageDelayed(8, 10000);
        } else if (flag.equals("1002")) {//心跳包响应


            MxyLog.i("---dataDeal---", "1002" + msg.substring(msg.length() - 6, msg.length() - 4));
        } else if (flag.equals("1003")) {//定位数据响应

            String outLineData = mDatabaseUtil.querydata();
            if (!TextUtils.isEmpty(outLineData)) {
                String[] outLineData1 = outLineData.split(" --- ");
                mDatabaseUtil.Delete(Integer.valueOf(outLineData1[0]));
                mHandler.post(locateRunnable);
            }

        } else if (flag.equals("1021")) {// 通讯录
            //2A2A 0045 1021 0003 0123456789000300 02 13880888080 38723872310000000000000000000000 01 00
            //13880888070 38723872310000000000000000000000 01 01  2323 (两个通讯录数据)
            int sum = Integer.parseInt(msg.substring(32, 34), 16);
            ContactHelper.getInstance().clearContact(LauncherApp.getAppContext());
            String CallNumber = null;
            for (int i = 1; i <= sum; i++) {
                String phoneInfo = msg.substring(34 + 48 * (i - 1), 34 + 48 * i);//电话字段
                String phoneNum = phoneInfo.substring(0, 11);//短话号码
                String phoneName = phoneInfo.substring(12, 44);//姓名
                String phoneSos = phoneInfo.substring(44, 46);//是否SOS号码及位置

                phoneName = Util.unicode2String2(phoneName);
                //如果是亲情号码可以考虑存入通讯录
                ContactHelper.getInstance().addContact(LauncherApp.getAppContext(), phoneName, phoneNum);

                //加入防火墙
                CallNumber += ":" + phoneNum;

                Settings.Global.putString(getApplication().getContentResolver(), "CallNumber", CallNumber);
                MxyLog.i("----------111111-------", Settings.Global.getString(getApplication().getContentResolver(), "isCall") +
                        "-----------------" + Settings.Global.getString(getApplication().getContentResolver(), "CallNumber"));
            }


        } else if (flag.equals("1008")) {//定位模式设置
            respondRunnable(msg.substring(8, 12), "0008");

        } else if (flag.equals("100B")) {//远程恢复出厂设置
            respondRunnable(msg.substring(12, 16), "000B");
            sendBroadcast(new Intent("com.werable.factory"));

        } else if (flag.equals("100C")) {//话费流量查询

        } else if (flag.equals("1010")) {//防火墙设置
            Settings.Global.putString(getApplication().getContentResolver(), "isCall", msg.substring(33, 34));

            respondRunnable(msg.substring(12, 16), "0010");

        } else if (flag.equals("1016")) {//计步数据响应

        } else if (flag.equals("100F")) {//心率报警数据
            //2A2A 0023 100F 0002 0048 0123456789000050  41 A0 02 13880888080 13880888080 2323(两个短信人员)
            int lowerAlarm = Integer.parseInt(msg.substring(32, 34), 16);
            int higherAlarm = Integer.parseInt(msg.substring(34, 36), 16);
            int num = Integer.parseInt(msg.substring(36, 38), 16);


            respondRunnable(msg.substring(12, 16), "100F");
        } else if (flag.equals("1018")) {//心率测量指令

        } else if (flag.equals("101E")) {//心率数据响应

            String outLineData = mHeartRateDatabaseUtil.querydata();
            if (!TextUtils.isEmpty(outLineData)) {
                String[] outLineData1 = outLineData.split(" --- ");
                mHeartRateDatabaseUtil.Delete(Integer.valueOf(outLineData1[0]));
                sendRunnable("001E");
            }


        } else if (flag.equals("1013")) {//语音下发

            sendBroadcast(new Intent("com.szwearable.chat.message_received"));
        } else if (flag.equals("1015")) {//计步数据响应

        } else if (flag.equals("1025")) {//绑定确认
            //2A2A 0015 1025 0048 0123456789000050 138808880880 2323

            bindSquence = msg.substring(12, 16);
            bindPhone = msg.substring(32, 44);
            mHandler.sendEmptyMessage(6);

        } else if (flag.equals("1027")) {//设备端解绑
            //2A2A 0013 00xx 0002 0123456789000050 01 2323
            if (msg.substring(32, 34).equals("01")) {
                respondRunnable(msg.substring(12, 16), "0027");
                sendBroadcast(new Intent("com.werable.factory"));
            } else {
                Toast.makeText(this, "unBind fail!", Toast.LENGTH_SHORT).show();
            }

        } else if (flag.equals("1026")) {//远程拍照


            Intent intent = new Intent(this, CameraActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("dowhat", "WEARABLE_ROMOTE_CAMERA");
            intent.putExtra("series", msg.substring(12, 16));
            startActivity(intent);

        } else if (flag.equals("1034")) {//接收服务中心定位用户信息。
            //2A2A 0043 1034 0002 6000000000000080 02 38723872310000000000000000000000 6000000000000080
            //38723872310000000000000000000000 6000000000000080 2323 （两个关注者）
            int sum = Integer.parseInt(msg.substring(32, 34), 16);//数量
            String mImeis = "" + sum;
            for (int i = 0; i < sum; i++) {
                String ss = msg.substring(34 + i * 48, 34 + (i + 1) * 60);
                String nickName = ss.substring(0, 32);
                String mImei = ss.substring(32, 47);
                nickName = Util.unicode2String2(nickName);

                mImeis += "_" + mImeis;
                Settings.Global.putString(getApplication().getContentResolver(), mImei, nickName);
            }
            Settings.Global.putString(getApplication().getContentResolver(), "mImeis", mImeis);

            respondRunnable(msg.substring(12, 16), "0034");
        } else if (flag.equals("1035")) {//收到老人小孩定位信息。
            //2A2A 0029 0035 0002 0123456789000020 6000000000000080 0F0105022330 01 160B95FF 720E6817 2323

            String Lat = msg.substring(62, 70);
            String Lng = msg.substring(70, 78);
            String mLat = Integer.parseInt(Lat.substring(0, 2), 16) + "." +
                    Integer.parseInt(Lat.substring(2, 4), 16) +
                    Integer.parseInt(Lat.substring(4, 6), 16) +
                    Integer.parseInt(Lat.substring(6, 8), 16);
            String mLng = Integer.parseInt(Lng.substring(0, 2), 16) + "." +
                    Integer.parseInt(Lng.substring(2, 4), 16) +
                    Integer.parseInt(Lng.substring(4, 6), 16) +
                    Integer.parseInt(Lng.substring(6, 8), 16);
            Intent intent = new Intent("com.getdalldevicedetail.broadcast");
            intent.putExtra("Lat", mLat);
            intent.putExtra("Lng", mLng);
            sendBroadcast(intent);

        } else if (flag.equals("1036")) {//联系人头像
            //2A2A 0019 1036 0003 0123456789000300 01 13880888080 2323 (1个通讯录数据)
            int doWhat = Integer.parseInt(msg.substring(32, 34), 16);//心率报警开关
            final String contactsImageNumber = msg.substring(34, 45);

            if (doWhat == 1) {
                HttpUtil mHttpUtil = new HttpUtil(mHandler);
                try {
                    FileHelper.deleteFile(Environment.getExternalStorageDirectory().toString() + "/" + "ContactAvatar" + "/" + contactsImageNumber + ".png");
                    MxyLog.i(TAG, "----------serverSquence-------" + peopleImage);
                    mHttpUtil.httpGetContactAvatar(peopleImage + contactsImageNumber + ".png", contactsImageNumber);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            } else if (doWhat == 2) {
                try {
                    FileHelper.deleteFile(Environment.getExternalStorageDirectory().toString() + "/" + "ContactAvatar" + "/" + contactsImageNumber + ".png");
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }


            respondRunnable(msg.substring(12, 16), "0036");
        } else if (flag.equals("1037")) {//心率报警开关
            //2A2A 0013 00xx 0002 0123456789000050 01 2323 (xxxx代表通用协议,根据首页导航自适应)
            int heartAlarm = Integer.parseInt(msg.substring(32, 34), 16);//心率报警开关


            respondRunnable(msg.substring(12, 16), "0037");
        } else if (flag.equals("1033")) {//心率定时设置开关
            //2A2A 0015 1033 0002 0123456789000050 01 001c 2323
            int heart = Integer.parseInt(msg.substring(32, 34), 16);
            int perHeart = Integer.parseInt(msg.substring(34, 36), 16);


            respondRunnable(msg.substring(12, 16), "0033");
        } else if (flag.equals("1039")) {//心率定时设置开关
            //2A2A 0020 1039 0001 0123456789000300 01 02 13880888080 13880888080 2323 (语音解析成功,同时返回两个商家号码)
            String shopDo = msg.substring(32, 34);
            Intent shopIt = new Intent("com.getzhinengchuxingdata.broadcast");
            if (shopDo.equals("00")) {
                shopIt.putExtra("result", false);
            } else {
                shopIt.putExtra("result", true);
                int shopNum = Integer.parseInt(msg.substring(34, 36), 16);
                String shopNumber = null;
                for (int i = 1; i <= shopNum; i++) {
                    String CallNumber = Settings.Global.getString(getApplication().getContentResolver(), "CallNumber");
                    shopNumber += ":" + msg.substring(36 + (i - 1) * 12, 36 + (i - 1) * 12 + 11);
                    CallNumber += ":" + msg.substring(36 + (i - 1) * 12, 36 + (i - 1) * 12 + 11);
                    Settings.Global.putString(getApplication().getContentResolver(), "CallNumber", CallNumber);
                }
                shopIt.putExtra("shopNumber", shopNumber);
            }
            sendBroadcast(shopIt);
            respondRunnable(msg.substring(12, 16), "0039");
        } else if (flag.equals("1040")) {//出行号码
            //2A2A 0018 1040 0001 0123456789000300 1388088808 2323
            String carNumber = msg.substring(32, 43);
            Settings.Global.putString(getApplication().getContentResolver(), "carNumber", carNumber);

            respondRunnable(msg.substring(12, 16), "0040");
        }


    }

    private String bindSquence = null;
    private String bindPhone = null;
    // ***********************************数据发送与接收

    /**
     * 电源锁
     */
    private void acquireWakeLock() {
        MxyLog.d(TAG, "Acquiring wake lock");
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm != null) {
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
                mWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
            }
        }
    }

    /**
     * 关闭电源锁
     */
    private void releaseWakeLock() {
        MxyLog.d(TAG, "Release wake lock");
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


    /**
     * 初始化定位服务
     */
    private void initLocation() {
        acquireWakeLock();
    }

    /**
     * 开始定位
     */
    private void startLocation() {

    }

    /**
     * 停止定位
     */
    private void stopLocation() {

    }

    /**
     * 监听到网络发生变化
     */
    private BroadcastReceiver netStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtils.isNetworkAvailable(SocketService.this)) {
                if (System.currentTimeMillis() - serverRespondTime >= 5 * 60 * 1000) {
                    if (mReadThread != null) {
                        mReadThread.release();
                    }
                    releaseLastSocket(mSocket);
                    // 获取socket的域名和端口
                    ThreadUtil.getPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            MxyLog.d(TAG, "get server socket address");
                            try {
                                String serverAddress1 = HttpUtil.getServerAndPort(imei);
                                if (serverAddress1 != null && !serverAddress1.isEmpty() && serverAddress1.contains("#")) {
                                    String[] serverAdress = serverAddress1.substring(1, serverAddress1.length() - 1).split("#");
                                    String[] hostAndport = serverAdress[0].split(":");
                                } else {
                                    resetSocketConfig();
                                }
                            } catch (Exception e) {
                                resetSocketConfig();
                            }
                            mHandler.sendEmptyMessage(3);
                        }
                    });
                }

                if (NetworkUtils.isNetworkAvailable(SocketService.this)
                        && (System.currentTimeMillis() - serverRespondTime >= 5 * 60 * 1000)) {
                    mHandler.removeMessages(4);
                    mHandler.sendEmptyMessageDelayed(4, 5000);
                }

                if (mListLocateProfile == null && !isLocating) {
                    stopLocation();
                    initLocation();
                    startLocation();
                    isLocating = true;
                } else if (mListLocateProfile != null
                        || mDatabaseUtil.querydata() != null) {
                    mHandler.post(locateRunnable);
                }
                if (NetworkUtils.isNetworkAvailable(SocketService.this)) {
                    if (mListLocateProfile != null
                            || mDatabaseUtil.querydata() != null) {
                        mHandler.postDelayed(locateRunnable, 500);
                    }
                }
            }
        }
    };

    /**
     * 设置默认的socket的配置信息
     */
    private void resetSocketConfig() {
    }
}

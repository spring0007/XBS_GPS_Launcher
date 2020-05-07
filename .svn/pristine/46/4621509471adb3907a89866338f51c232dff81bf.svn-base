package com.sczn.wearlauncher.socket;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.SparseArray;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.LogFile;
import com.sczn.wearlauncher.socket.command.post.BaseCommand;
import com.sczn.wearlauncher.util.BackgroundThread;
import com.sczn.wearlauncher.util.NetworkUtils;
import com.sczn.wearlauncher.util.ThreadUtil;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Description:
 * Created by Bingo on 2019/1/16.
 */
public class WaterSocketManager {

    private final String TAG = "WaterSocketManager";

    private static WaterSocketManager manager;
    private WaterSocketService socketService;

    private boolean mIsBindService;
    private ExecCmdRunnable execCmdRunnable;
    private volatile BlockingDeque<BaseCommand> commandBlockingDeque;

    private final int WAIT_MSG_CALLBACK_TIME_OUT = 103;//等待消息返回
    private final int WAIT_MSG_CALLBACK_TIME = 10000;//10秒

    /**
     * @return
     */
    public static WaterSocketManager getInstance() {
        if (null == manager) {
            synchronized (WaterSocketManager.class) {
                if (null == manager) {
                    manager = new WaterSocketManager();
                }
            }
        }
        return manager;
    }

    /**
     * 单例构造方法
     */
    public WaterSocketManager() {
        registerService();
    }

    /**
     * 注册service
     */
    private void registerService() {
        if (commandBlockingDeque == null) {
            commandBlockingDeque = new LinkedBlockingDeque<>();
        }
        if (!mIsBindService) {
            Intent intent = new Intent(LauncherApp.getAppContext(), WaterSocketService.class);
            LauncherApp.getAppContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
            mIsBindService = true;
        }
    }

    /**
     * connection
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WaterSocketService.LocalBinder binder = (WaterSocketService.LocalBinder) service;
            socketService = binder.getService();
            MxyLog.i(TAG, "bind service.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            socketService = null;
        }
    };

    /**
     * 发送协议
     * send command to server
     *
     * @param postBean
     */
    public synchronized void send(BaseCommand postBean) {
        MxyLog.d(TAG, "send..");
        LogFile.logCatCmdWithThread("net state:" + NetworkUtils.isNetworkAvailable(LauncherApp.getAppContext()) + ":" + postBean.send());
        if (execCmdRunnable != null) {
            BackgroundThread.removeTask(execCmdRunnable);
        }
        execCmdRunnable = new ExecCmdRunnable(postBean);
        BackgroundThread.post(execCmdRunnable);
    }

    /**
     * 发送协议(多条协议)
     * send commands to server
     *
     * @param postBeans
     */
    public synchronized void send(List<BaseCommand> postBeans) {
        MxyLog.d(TAG, "send..");
        if (execCmdRunnable != null) {
            BackgroundThread.removeTask(execCmdRunnable);
        }
        execCmdRunnable = new ExecCmdRunnable(postBeans);
        BackgroundThread.post(execCmdRunnable);
    }

    /**
     * 取消任务
     */
    public void cancel() {
        if (execCmdRunnable != null) {
            BackgroundThread.removeTask(execCmdRunnable);
        }
        if (commandBlockingDeque != null) {
            commandBlockingDeque.clear();
        }
    }

    /**
     *
     */
    public void onFail() {
        clearTimeOutMsg();
        if (commandBlockingDeque != null && commandBlockingDeque.size() > 0) {
            commandBlockingDeque.removeFirst().onFail();
        }
    }

    /**
     * @param obtain
     */
    public void onSuccess(String obtain) {
        clearTimeOutMsg();
        if (commandBlockingDeque != null && commandBlockingDeque.size() > 0) {
            commandBlockingDeque.removeFirst().onSuccess(obtain);
        }
    }

    /**
     * @return
     */
    public BaseCommand retrySendFirstCmd() {
        clearTimeOutMsg();
        if (commandBlockingDeque != null && commandBlockingDeque.size() > 0) {
            return commandBlockingDeque.removeFirst();
        }
        return null;
    }

    /**
     * 解除绑定service
     *
     * @param mContext
     */
    public void unInitSocketService(Context mContext) {
        if (mIsBindService && connection != null) {
            mContext.unbindService(connection);
            mIsBindService = false;
        }
        // 清除队列信息
        if (commandBlockingDeque != null) {
            commandBlockingDeque.clear();
        }
    }

    /**
     * 执行发送协议,可以使传递数组的形式,可以传递一个指令
     */
    class ExecCmdRunnable implements Runnable {
        SparseArray<BaseCommand> sendCommands = new SparseArray<>();

        public ExecCmdRunnable(final List<BaseCommand> commands) {
            if (sendCommands != null) {
                sendCommands.clear();
            } else {
                sendCommands = new SparseArray<>();
            }
            for (int i = 0; i < commands.size(); i++) {
                sendCommands.append(i, commands.get(i));
            }
        }

        public ExecCmdRunnable(final BaseCommand commands) {
            if (sendCommands != null) {
                sendCommands.clear();
            } else {
                sendCommands = new SparseArray<>();
            }
            sendCommands.append(0, commands);
        }

        @Override
        public void run() {
            if (sendCommands != null && sendCommands.size() > 0) {
                for (int i = 0; i < sendCommands.size(); i++) {
                    sendOrWait(sendCommands.get(i), sendCommands.get(i).getNeedCallBack());
                }
                clearSendCommand();
            }
        }

        public void clearSendCommand() {
            if (sendCommands != null) {
                sendCommands.clear();
            }
        }
    }

    /**
     * @param command
     */
    private void sendOrWait(BaseCommand command, boolean needCallBack) {
        int CHECK_TIME = 500;
        long waitTime = 0;
        long reconnectCounts = 0;
        if (socketService == null) {
            ThreadUtil.threadDelayTime(500);
            MxyLog.d(TAG, "send..socketService==null");
        }
        while (true) {
            MxyLog.d(TAG, "send msg ");
            if (socketService == null) {
                MxyLog.d(TAG, "error socketService == null");
                mIsBindService = false;
                registerService();
                onFail();
                break;
            }
            if (!NetworkUtils.isNetworkAvailable(LauncherApp.getAppContext())) {
                MxyLog.d(TAG, "error isNetworkAvailable false");
                onFail();
                break;
            }
            //如果需要增加队列等待发送的话,在这里检查commandBlockingDeque的size,如果有的大于0的话,需要进行等待操作
            if (commandBlockingDeque != null && commandBlockingDeque.size() > 0) {
                MxyLog.d(TAG, "has cmd send need waiting..");
                waitTime += CHECK_TIME;
                ThreadUtil.threadDelayTime(CHECK_TIME);
                if (waitTime > 10 * CHECK_TIME) {
                    MxyLog.d(TAG, "waiting command send error");
                    onFail();
                    break;
                }
            } else {
                if (socketService.getmSocket() == null || !socketService.getmSocket().isConnected()) {
                    //需要重新初始化socket
                    if (reconnectCounts < 2) {
                        MxyLog.d(TAG, "socket == null or socket not connected try to reconnect socket");
                        reconnectCounts++;
                        reInitSocket();
                        ThreadUtil.threadDelayTime(10000);
                    } else {
                        MxyLog.d(TAG, "socket == null or socket not connected reconnect socket error");
                        onFail();
                        break;
                    }
                } else {
                    waitTime += CHECK_TIME;
                    boolean result = socketService.send(command);
                    if (result) {
                        MxyLog.d(TAG, "send msg success.wait callback");
                        commandBlockingDeque.addLast(command);
                        clearTimeOutMsg();
                        if (needCallBack) {
                            mHandler.sendEmptyMessageDelayed(WAIT_MSG_CALLBACK_TIME_OUT, WAIT_MSG_CALLBACK_TIME);
                        } else {
                            onSuccess("send successful");
                            MxyLog.d(TAG, "send msg do not need call back.");
                        }
                        break;
                    } else {
                        if (socketService != null && socketService.getmSocket() != null) {
                            boolean connected = socketService.getmSocket().isConnected();
                            if (connected) {
                                MxyLog.d(TAG, command + "..wait send...");
                                ThreadUtil.threadDelayTime(CHECK_TIME);
                                if (waitTime > 10 * CHECK_TIME) {
                                    MxyLog.d(TAG, "command send error");
                                    onFail();
                                    break;
                                } else {
                                    MxyLog.d(TAG, "retry command send");
                                }
                            } else {
                                //需要重新初始化socket
                                if (reconnectCounts < 2) {
                                    MxyLog.d(TAG, "try to reconnect socket");
                                    reconnectCounts++;
                                    reInitSocket();
                                    ThreadUtil.threadDelayTime(10000);
                                } else {
                                    MxyLog.d(TAG, "reconnect socket error");
                                    onFail();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 取消超时等待消息
     */
    private void clearTimeOutMsg() {
        if (mHandler != null && mHandler.hasMessages(WAIT_MSG_CALLBACK_TIME_OUT)) {
            mHandler.removeMessages(WAIT_MSG_CALLBACK_TIME_OUT);
        }
    }

    /**
     * mHandler
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WAIT_MSG_CALLBACK_TIME_OUT) {
                MxyLog.d(TAG, "指令发送成功,到了设定的时间,接收指令超时..");
                onFail();
                //超时之后,重新初始化socket
                reInitSocket();
            }
            return false;
        }
    });

    /**
     * reset
     */
    public void reInitSocket() {
        if (socketService != null) {
            socketService.reInitSocket();
        }
    }
}

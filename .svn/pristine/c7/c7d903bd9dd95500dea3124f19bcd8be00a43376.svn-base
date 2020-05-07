package com.sczn.wearlauncher.chat.net;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.http.OkhttpClientManager;
import com.sczn.wearlauncher.chat.bean.GetGroupMsgObtain;
import com.sczn.wearlauncher.chat.bean.GroupBaseBeanObtain;
import com.sczn.wearlauncher.chat.bean.GroupMsg;
import com.sczn.wearlauncher.chat.bean.ListGroupMsgObtain;
import com.sczn.wearlauncher.chat.bean.ListGroupObtain;
import com.sczn.wearlauncher.chat.bean.SendGroupMsgObtain;
import com.sczn.wearlauncher.chat.bean.UploadGroupVoiceObtain;
import com.sczn.wearlauncher.chat.model.WechatGroupInfo;
import com.sczn.wearlauncher.chat.model.WechatMessageInfo;
import com.sczn.wearlauncher.socket.SocketConstant;
import com.sczn.wearlauncher.socket.WaterSocketManager;
import com.sczn.wearlauncher.socket.command.CommandResultCallback;
import com.sczn.wearlauncher.socket.command.ResultCode;
import com.sczn.wearlauncher.socket.command.post.LoginCmd;
import com.sczn.wearlauncher.util.GsonHelper;
import com.sczn.wearlauncher.util.NetworkUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description:微聊网络请求
 * Created by Bingo on 2019/3/19.
 */
public class Net {

    private final String TAG = "Net";
    private static Net net;

    public static Net getNet() {
        if (null == net) {
            synchronized (Net.class) {
                if (null == net) {
                    net = new Net();
                }
            }
        }
        return net;
    }

    /**
     * 网络判断
     *
     * @return
     */
    private boolean hasNet() {
        return NetworkUtils.isNetWorkConnected(LauncherApp.getAppContext());
    }

    /**
     * 发送请求并且需要重新登录,重试1次
     *
     * @param url
     * @param netCallBack
     */
    private void req(String url, final NetCallBack<String> netCallBack) {
        MxyLog.d(TAG, "-->>微聊:" + url);
        if (!hasNet()) {
            if (netCallBack != null) {
                netCallBack.onFail();
            }
            return;
        }
        final Request request = new Request.Builder().url(url).build();
        GroupBaseBeanObtain base;
        try {
            Response response = OkhttpClientManager.getInstance().getmOkHttpClient().newCall(request).execute();
            if (response != null && response.body() != null) {
                String json = response.body().string();
                MxyLog.d(TAG, "<<--微聊:" + json);
                base = GsonHelper.getInstance().getGson().fromJson(json, GroupBaseBeanObtain.class);
                /**
                 * 需要重新登录
                 */
                if (base != null && base.getCode() == ResultCode.CODE_DEVICE_NOT_LOGIN) {
                    LoginCmd loginCmd = new LoginCmd(new CommandResultCallback() {
                        @Override
                        public void onSuccess(String baseObtain) {
                            MxyLog.d(TAG, "设备重新登录成功.重新发送指令..");
                            try {
                                Response response = OkhttpClientManager.getInstance().getmOkHttpClient().newCall(request).execute();
                                if (response != null && response.body() != null) {
                                    String json = response.body().string();
                                    MxyLog.d(TAG, "<<--重试微聊:" + json);
                                    netCallBack.onSuccess(json);
                                } else {
                                    netCallBack.onFail();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                netCallBack.onFail();
                            }
                        }

                        @Override
                        public void onFail() {
                            MxyLog.d(TAG, "设备登录失败.");
                            netCallBack.onFail();
                        }
                    });
                    WaterSocketManager.getInstance().send(loginCmd);
                } else {
                    netCallBack.onSuccess(json);
                }
            } else {
                netCallBack.onFail();
            }
        } catch (IOException e) {
            e.printStackTrace();
            netCallBack.onFail();
        }
    }

    /**
     * 获取群列表
     *
     * @param localList 本地已保存的数据
     * @param callBack
     */
    public void getGroupListData(final List<WechatGroupInfo> localList, final NetCallBack<List<WechatGroupInfo>> callBack) {
        String url = SocketConstant.listGroup + "?imei=" + Config.IMEI;
        req(url, new NetCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                ListGroupObtain listGroupObtain = GsonHelper.getInstance().getGson().fromJson(json, ListGroupObtain.class);
                if (listGroupObtain != null && listGroupObtain.getCode() == 0 && listGroupObtain.getData() != null
                        && listGroupObtain.getData().size() > 0) {
                    List<WechatGroupInfo> wechatGroupInfoList = new ArrayList<>();
                    for (WechatGroupInfo data : listGroupObtain.getData()) {
                        if (data != null) {
                            WechatGroupInfo wechatGroupInfo = new WechatGroupInfo();
                            wechatGroupInfo.setGroupId(data.getGroupId() + "");
                            wechatGroupInfo.setGroupName(data.getName());
                            wechatGroupInfo.setHead(data.getHead());
                            wechatGroupInfo.setMaxMsgId(data.getMaxMsgId());
                            //更新已读未读标志
                            if (localList != null && localList.size() > 0) {
                                for (WechatGroupInfo info : localList) {
                                    if (info.getGroupId().equals(wechatGroupInfo.getGroupId())) {
                                        wechatGroupInfo.setMsgReadStatus(info.getMsgReadStatus());
                                        localList.remove(info);
                                        break;
                                    }
                                }
                            }
                            wechatGroupInfoList.add(wechatGroupInfo);
                            wechatGroupInfo.saveOrUpdate("groupId = ?", wechatGroupInfo.getGroupId());
                        }
                    }
                    if (localList != null && localList.size() > 0) {
                        for (WechatGroupInfo w : localList) {
                            MxyLog.d(TAG, "本地数据条数,比服务器多" + w.toString());
                            w.delete();
                        }
                    }
                    if (callBack != null) {
                        callBack.onSuccess(wechatGroupInfoList);
                    }
                } else {
                    //服务器没有数据了,删除本地群组
                    LitePal.deleteAll(WechatGroupInfo.class);
                    if (callBack != null) {
                        callBack.onFail();
                    }
                }
            }

            @Override
            public void onFail() {
                if (callBack != null) {
                    callBack.onFail();
                }
            }
        });
    }

    /**
     * 上传群语音
     *
     * @param groupId
     * @param filePath
     */
    private void uploadGroupVoice(String groupId, String filePath, NetCallBack<UploadGroupVoiceObtain> callBack) {
        if (!hasNet()) {
            if (callBack != null) {
                callBack.onFail();
            }
            return;
        }
        final File vFile = new File(filePath);
        String json;
        try {
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(vFile.getName());
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("file", vFile.getName(),
                            RequestBody.create(MediaType.parse(fileType), vFile))
                    .addFormDataPart("groupId", groupId)
                    .addFormDataPart("imei", Config.IMEI)
                    .build();
            Request request = new Request.Builder()
                    .url(SocketConstant.uploadGroupVoice)
                    .post(requestBody)
                    .build();
            Response response = OkhttpClientManager.getInstance().getmOkHttpClient().newCall(request).execute();
            if (response != null && response.body() != null) {
                json = response.body().string();
                MxyLog.d(TAG, "<<--上传群语音:" + json);
                UploadGroupVoiceObtain obtain = GsonHelper.getInstance().getGson().fromJson(json, UploadGroupVoiceObtain.class);
                if (obtain != null && obtain.getCode() == ResultCode.CODE_DEVICE_NOT_LOGIN) {//没有登录
                    LoginCmd loginCmd = new LoginCmd(new CommandResultCallback() {
                        @Override
                        public void onSuccess(String baseObtain) {
                            MxyLog.d(TAG, "设备重新登录成功.");
                        }

                        @Override
                        public void onFail() {
                            MxyLog.d(TAG, "设备登录失败.");
                        }
                    });
                    WaterSocketManager.getInstance().send(loginCmd);
                    if (callBack != null) {
                        callBack.onFail();
                    }
                    return;
                }
                if (obtain != null && obtain.getCode() == 0 && obtain.getData() != null) {
                    if (callBack != null) {
                        callBack.onSuccess(obtain);
                    }
                    return;
                }
            }
            if (callBack != null) {
                callBack.onFail();
            }
        } catch (IOException e) {
            MxyLog.w(TAG, "上传群语音失败~" + e.toString());
            if (callBack != null) {
                callBack.onFail();
            }
        }
    }

    /**
     * 发送群消息
     *
     * @param groupId
     * @param msgType
     * @param msgLength
     * @param msgContent
     * @param callBack
     */
    private void sendGroupMsg(String groupId, int msgType, int msgLength, String msgContent, NetCallBack<SendGroupMsgObtain> callBack) {
        if (!hasNet()) {
            if (callBack != null) {
                callBack.onFail();
            }
            return;
        }
        String json;
        try {
            String postJson = "{\"imei\": \"" + Config.IMEI
                    + "\",\"groupId\": " + groupId
                    + ",\"msgType\": " + msgType
                    + ",\"msgLength\": " + msgLength
                    + ",\"msgContent\": \"" + msgContent + "\"}";
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, postJson);
            MxyLog.d(TAG, "-->>发送群消息:" + postJson);
            Request request = new Request.Builder()
                    .url(SocketConstant.sendGroupMsg)
                    .post(body)
                    .build();
            Response response = OkhttpClientManager.getInstance().getmOkHttpClient().newCall(request).execute();
            if (response != null && response.body() != null) {
                json = response.body().string();
                MxyLog.d(TAG, "<<--发送群消息:" + json);
                SendGroupMsgObtain obtain = GsonHelper.getInstance().getGson().fromJson(json, SendGroupMsgObtain.class);
                if (obtain != null && obtain.getCode() == 0 && obtain.getData() != null) {
                    if (callBack != null) {
                        callBack.onSuccess(obtain);
                    }
                    return;
                }
            }
            if (callBack != null) {
                callBack.onFail();
            }
        } catch (IOException e) {
            MxyLog.w(TAG, "发送群消息失败~" + e.toString());
            if (callBack != null) {
                callBack.onFail();
            }
        }
    }

    /**
     * 发送语音,步骤
     * 1.先上传语音文件
     * 2.再发送语音消息
     *
     * @param groupId
     * @param filePath
     * @param msgLength
     * @param callBack
     */
    public void sendGroupVoiceMsg(String groupId, String filePath, final int msgLength, final NetCallBack<SendGroupMsgObtain> callBack) {
        uploadGroupVoice(groupId, filePath, new NetCallBack<UploadGroupVoiceObtain>() {
            @Override
            public void onSuccess(UploadGroupVoiceObtain obtain) {
                if (obtain != null && obtain.getData() != null) {
                    sendGroupMsg(obtain.getData().getGroupId() + "", 2, msgLength, obtain.getData().getVoiceUrl(), new NetCallBack<SendGroupMsgObtain>() {
                        @Override
                        public void onSuccess(SendGroupMsgObtain sendGroupMsgObtain) {
                            if (sendGroupMsgObtain != null) {
                                if (callBack != null) {
                                    callBack.onSuccess(sendGroupMsgObtain);
                                }
                            }
                        }

                        @Override
                        public void onFail() {
                            if (callBack != null) {
                                callBack.onFail();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFail() {
                if (callBack != null) {
                    callBack.onFail();
                }
            }
        });
    }

    /**
     * 获取群最新消息,并且把最新的消息保存到本地
     *
     * @param groupId
     * @param msgId
     * @param pageSize
     * @param callBack
     */
    public void getGroupMsg(String groupId, int msgId, int pageSize, final NetCallBack<List<WechatMessageInfo>> callBack) {
        String url = SocketConstant.getGroupMsg
                + "?imei=" + Config.IMEI
                + "&groupId=" + groupId
                + "&msgId=" + msgId
                + "&pageSize=" + pageSize;
        req(url, new NetCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                List<WechatMessageInfo> list;
                GetGroupMsgObtain obtain = GsonHelper.getInstance().getGson().fromJson(json, GetGroupMsgObtain.class);
                if (obtain != null && obtain.getCode() == 0 && obtain.getData() != null) {
                    list = saveData(obtain.getData());
                    Collections.sort(list);
                    if (callBack != null) {
                        callBack.onSuccess(list);
                    }
                } else {
                    if (callBack != null) {
                        callBack.onFail();
                    }
                }
            }

            @Override
            public void onFail() {
                if (callBack != null) {
                    callBack.onFail();
                }
            }
        });
    }

    /**
     * 获取群历史消息
     *
     * @param groupId
     * @param msgId
     * @param pageNum
     * @param pageSize
     * @param callBack
     */
    public void listGroupMsg(String groupId, int msgId, int pageNum, int pageSize, final NetCallBack<List<WechatMessageInfo>> callBack) {
        String url = SocketConstant.listGroupMsg
                + "?imei=" + Config.IMEI
                + "&groupId=" + groupId
                + "&msgId=" + msgId
                + "&pageNum=" + pageNum
                + "&pageSize=" + pageSize;
        req(url, new NetCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                List<WechatMessageInfo> list;
                ListGroupMsgObtain obtain = GsonHelper.getInstance().getGson().fromJson(json, ListGroupMsgObtain.class);
                if (obtain != null && obtain.getCode() == 0 && obtain.getData() != null) {
                    list = saveData(obtain.getData());
                    //降序
                    Collections.sort(list, new Comparator<WechatMessageInfo>() {
                        @Override
                        public int compare(WechatMessageInfo o1, WechatMessageInfo o2) {
                            return o2.getMsgId() - o1.getMsgId();
                        }
                    });
                    if (callBack != null) {
                        callBack.onSuccess(list);
                    }
                } else {
                    if (callBack != null) {
                        callBack.onFail();
                    }
                }
            }

            @Override
            public void onFail() {
                if (callBack != null) {
                    callBack.onFail();
                }
            }
        });
    }

    /**
     * 保存或者更新数据到本地数据库
     *
     * @param obtain
     * @return
     */
    private static List<WechatMessageInfo> saveData(List<GroupMsg> obtain) {
        List<WechatMessageInfo> list = new ArrayList<>();
        for (GroupMsg bean : obtain) {
            WechatMessageInfo info = new WechatMessageInfo();
            info.setGroupId(bean.getGroupId() + "");
            info.setSenderName(bean.getName());
            info.setHead(bean.getHead());
            info.setSenderId(bean.getSender() + "");
            info.setSenderType(bean.getSenderType());
            info.setContent(bean.getMsgContent());
            info.setType(bean.getMsgType());
            info.setMsgId(bean.getMsgId());
            info.setMsgLength(bean.getMsgLength());
            info.setDuration(bean.getMsgLength() + "");
            info.setVoicePath(SocketConstant.chatFileAddress(bean.getMsgContent()));
            info.setCreateTime(bean.getCreateTime());
            info.saveOrUpdate("msgId = ?", bean.getMsgId() + "");
            list.add(info);
        }
        return list;
    }

    /**
     * 获取文件MimeType
     *
     * @param filename
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }
}

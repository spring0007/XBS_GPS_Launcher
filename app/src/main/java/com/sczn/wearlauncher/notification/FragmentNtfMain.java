package com.sczn.wearlauncher.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.absFragment;
import com.sczn.wearlauncher.base.util.SysServices;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.base.view.WrapLinearLayoutManager;
import com.sczn.wearlauncher.notification.AdapterNtfMain.INotificationItemClick;
import com.sczn.wearlauncher.notification.FragmentNtfDetail.ICloseDetail;
import com.sczn.wearlauncher.notification.UtilNotification.INotificationListen;

import java.lang.ref.WeakReference;

/**
 * 通知页面
 */
public class FragmentNtfMain extends absFragment implements INotificationListen, INotificationItemClick, ICloseDetail {

    private static final String TAG = FragmentNtfMain.class.getSimpleName();

    private static final String FRAGMENT_TAG_DETAIL = "fragment_tag_notification_detail";

    public static FragmentNtfMain newInstance() {
        return new FragmentNtfMain();
    }

    private TextView mNotificationClear;
    private MyRecyclerView mNotificationRv;
    private AdapterNtfMain mAdapter;
    private UtilNotification mUtilNotification;

    private NotificationHandler mNotificationHandler;
    private boolean needFresh;
    private boolean canFresh;

    private FragmentNtfDetail mNotificationDetailFragment;

    private boolean isEmptyed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtilNotification = UtilNotification.getInstance();
    }

    @Override
    public void onDestroy() {
        mUtilNotification = null;
        super.onDestroy();
    }

    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_notification_main;
    }

    @Override
    protected void initView() {
        mNotificationClear = findViewById(R.id.notification_clear);
        mNotificationRv = findViewById(R.id.notification_main_list);
    }

    @Override
    protected void initData() {
        mAdapter = new AdapterNtfMain(getActivity(), mUtilNotification.getNotificationList(), this);
        mNotificationRv.setLayoutManager(new WrapLinearLayoutManager(getActivity()));
        mNotificationRv.setAdapter(mAdapter);
        mNotificationRv.setEmpty(findViewById(R.id.notification_empty));
        mNotificationClear.setOnClickListener(onClearClick);
        needFresh = true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNotificationHandler = new NotificationHandler(this);
        mUtilNotification.setListen(this);
    }

    @Override
    public void onDestroyView() {
        mUtilNotification.removeListen(this);
        mNotificationHandler.removeCallbacksAndMessages(null);
        mNotificationHandler = null;
        super.onDestroyView();
    }

    @Override
    protected void startFreshData() {
        super.startFreshData();
        canFresh = true;
        if (needFresh) {
            //changeClearState(false);
            freshData();
            needFresh = false;
        }
    }

    @Override
    protected void endFreshData() {
        super.endFreshData();
        changeClearState(false);
        canFresh = false;
    }

    private void freshData() {
        MxyLog.d(TAG, "freshData" + mUtilNotification.getNotificationList().size());
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void notificationAction() {
        if (getActivity() == null) {
            return;
        }
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.defaults = Notification.DEFAULT_SOUND;

        final int modeState = SysServices.getRingMode(getActivity());
        switch (modeState) {
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                vibrator.vibrate(1000);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
            default:
                notificationManager.notify(3, notification);
                break;
        }
    }

    @Override
    public void removePosition(int position) {
        MxyLog.d(TAG, "removePosition" + "position=" + position + "--canFresh=" + canFresh);
        if (!canFresh) {
            needFresh = true;
            return;
        }
        Message message = Message.obtain();
        message.what = NotificationHandler.MSG_REMOVE;
        message.arg1 = position;
        setMessage(message);
        //adapterNotifyRemoved(position);
    }

    @Override
    public void addNew(int position) {
        notificationAction();
        if (!canFresh) {
            needFresh = true;
            return;
        }
        Message message = Message.obtain();
        message.what = NotificationHandler.MSG_ADD;
        message.arg1 = position;
        setMessage(message);
    }

    @Override
    public void addNewDetail(String pkgName, int position) {
        notificationAction();
        Message message = Message.obtain();
        message.what = NotificationHandler.MSG_ADD_DETAIL;
        message.arg1 = position;
        message.obj = pkgName;
        setMessage(message);
    }

    @Override
    public void freshAll() {
        if (!canFresh) {
            needFresh = true;
            return;
        }
        Message message = Message.obtain();
        message.what = NotificationHandler.MSG_FRESH_ALL;
        setMessage(message);
    }

    private void adapterNotifyAdd(int position) {
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyItemInserted(position);
        changeClearState(false);
    }

    private void adapterNotifyRemoved(int position) {
        if (mAdapter == null || (position > mAdapter.getItemCount())) {
            return;
        }
        mAdapter.notifyItemRemoved(position);
    }

    private void setMessage(Message message) {
        if (mNotificationHandler != null) {
            mNotificationHandler.sendMessage(message);
        }
    }

    private void onWatchNotificationClick(StatusBarNotification notification, int position) {
        final String packageName = notification.getPackageName();
        final boolean isUsbPackageName = packageName.equals("android") || packageName.equals("com.android.systemui");
        if (!isUsbPackageName) {
            mUtilNotification.removeWatchNotification(notification);
        }

        PendingIntent mPendingIntent = notification.getNotification().contentIntent;
        try {
            if (!notification.getPackageName().equals("com.android.bluetooth")) {
                if (mPendingIntent != null)
                    mPendingIntent.send();
            }

        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }

    private void onPhoneNotificationClick(ModelPhonePkgNotification notifications, int position) {
        showDetailFragment(notifications);
    }

    @Override
    public void onNotificationClick(View view, int position) {
        if (view.getTag() == null || !(view.getTag() instanceof ModelNotification)) {
            return;
        }
        final ModelNotification ntf = (ModelNotification) view.getTag();

        switch (ntf.getNtfType()) {
            case ModelNotification.NTF_TYPE_WATCH:
                onWatchNotificationClick(ntf.getWatchNtf(), position);
                break;
            case ModelNotification.NTF_TYPE_PHONE:
                onPhoneNotificationClick(ntf.getPhoneNtf(), position);
                break;
            default:
                break;
        }

    }

    /**
     * 清除,关闭点击事件
     */
    private OnClickListener onClearClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEmptyed) {
                getActivity().onBackPressed();
            } else {
                mUtilNotification.clearNotificationList();
                changeClearState(true);
            }
        }
    };

    /**
     * 清除,关闭文字
     *
     * @param isEmpty
     */
    private void changeClearState(boolean isEmpty) {
        if (isEmpty) {
            mNotificationClear.setText(R.string.notification_closed);
            isEmptyed = true;
        } else {
            mNotificationClear.setText(R.string.notification_clear);
            isEmptyed = false;
        }
    }

    private void showDetailFragment(ModelPhonePkgNotification notification) {
        if (mNotificationDetailFragment != null) {
            if (mNotificationDetailFragment.isAdded()) {
                mNotificationDetailFragment.dismissAllowingStateLoss();
            }
            mNotificationDetailFragment = null;
        }
        mNotificationDetailFragment = FragmentNtfDetail.newInstance(notification);
        mNotificationDetailFragment.setCloseListen(this);
        mNotificationDetailFragment.show(getChildFragmentManager(), FRAGMENT_TAG_DETAIL);

    }

    private void freshDetailFragment(String pkgName) {
        if (mNotificationDetailFragment != null) {
            if (mNotificationDetailFragment.isAdded()) {
                if (pkgName.equals(mNotificationDetailFragment.getDetailPkgName())) {
                    mNotificationDetailFragment.freshDetail();
                }
            }
        }
    }

    private static class NotificationHandler extends Handler {
        private static final int MSG_FRESH_ALL = 0;
        private static final int MSG_REMOVE = 1;
        private static final int MSG_ADD = 2;
        private static final int MSG_ADD_DETAIL = 3;

        private static WeakReference<FragmentNtfMain> mFragment;

        private NotificationHandler(FragmentNtfMain fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentNtfMain fragment = mFragment.get();
            if (fragment == null) {
                return;
            }
            MxyLog.d(TAG, "handleMessage --msg.what=" + msg.what);
            switch (msg.what) {
                case MSG_FRESH_ALL:
                    fragment.freshData();
                    break;
                case MSG_REMOVE:
                    fragment.adapterNotifyRemoved(msg.arg1);
                    break;
                case MSG_ADD:
                    fragment.adapterNotifyAdd(msg.arg1);
                    break;
                case MSG_ADD_DETAIL:
                    fragment.freshDetailFragment((String) msg.obj);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void closeWithClear(ModelPhonePkgNotification notificationDetails) {
        mUtilNotification.removeNotificationDetails(notificationDetails);
        if (mNotificationDetailFragment != null) {
            if (mNotificationDetailFragment.isAdded()) {
                mNotificationDetailFragment.dismissAllowingStateLoss();
            }
            mNotificationDetailFragment = null;
        }
    }
}

package com.sczn.wearlauncher.card.healthalarm;

import android.content.ContentUris;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.MxyToast;
import com.sczn.wearlauncher.db.Provider;
import com.sczn.wearlauncher.db.Provider.ColumnsHealthAlarm;

public class FragmentAlarmEdit extends absDialogFragment implements OnClickListener {

    public static final String ARG_ALARM = "alarm";

    public static FragmentAlarmEdit newInstance(ModelAlarm alarm) {
        final FragmentAlarmEdit fragment = new FragmentAlarmEdit();
        final Bundle bdl = new Bundle();
        bdl.putParcelable(ARG_ALARM, alarm);
        fragment.setArguments(bdl);
        return fragment;
    }

    private ViewTimePicker mTimePicker;
    private ViewWeekdayPick mViewWeekdayPick;
    private TextView mSure;
    private TextView mCancle;
    private ModelAlarm mAlarm;

    private boolean isAdd = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        final Bundle bdl = getArguments();
        if (bdl != null) {
            mAlarm = (ModelAlarm) bdl.get(ARG_ALARM);
            if (ModelAlarm.INVALUED_ID == mAlarm.getID()) {
                isAdd = true;
            } else {
                isAdd = false;
            }
        }
    }

    @Override
    protected int getLayoutResouceId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_card_healthalarm_alarm_edit;
    }

    @Override
    protected void creatView() {
        // TODO Auto-generated method stub
        mTimePicker = findViewById(R.id.alarm_timer_pick);
        mViewWeekdayPick = findViewById(R.id.alarm_pick_weekday);
        mSure = findViewById(R.id.alarm_edit_sure);
        mCancle = findViewById(R.id.alarm_edit_cancle);

        findViewById(R.id.card_title_bar_back).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.card_title_bar_title)).setText(R.string.healthalarm_setting);

        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        mSure.setOnClickListener(this);
        mCancle.setOnClickListener(this);

        if (!isAdd) {
            mViewWeekdayPick.setInitValue(mAlarm.getRepeatDay());
            final long time = mAlarm.getTimeInDay();

            final int hour = (int) (time / 3600000);
            final int min = (int) ((time % 3600000) / 60000);
            mTimePicker.setInitTime(String.format("%02d", hour), String.format("%02d", min));
        }
    }

    @Override
    protected void destorytView() {
        // TODO Auto-generated method stub

    }

    private void alarmSave() {

        MxyLog.d(this, "alarmSave" + "--mAlarm.getType()" + mAlarm.getType());

        ContentValues values = new ContentValues();
        values.put(Provider.ColumnsHealthAlarm.COLUMNS_TIME, mTimePicker.getTimeInMill());
        values.put(Provider.ColumnsHealthAlarm.COLUMNS_TYPE, mAlarm.getType());
        values.put(Provider.ColumnsHealthAlarm.COLUMNS_REPEAT, mViewWeekdayPick.getValue());
        values.put(Provider.ColumnsHealthAlarm.COLUMNS_EBABLE, 1);

        if (isAdd) {
            if (LauncherApp.appContext.getContentResolver().insert(ColumnsHealthAlarm.CONTENT_URI, values) == null) {
                MxyToast.showShort(getActivity(), R.string.alarm_sava_failed);
            } else {
                MxyToast.showShort(getActivity(), R.string.alarm_sava_success);
            }

        } else {
            if (LauncherApp.appContext.getContentResolver().update(ContentUris.withAppendedId(ColumnsHealthAlarm.CONTENT_URI, mAlarm.getID()),
                    values, null, null) <= 0) {
                MxyToast.showShort(getActivity(), R.string.alarm_sava_failed);
            } else {
                MxyToast.showShort(getActivity(), R.string.alarm_sava_success);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.alarm_edit_sure:
                alarmSave();
                dismissAllowingStateLoss();
                break;
            case R.id.alarm_edit_cancle:
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }

}

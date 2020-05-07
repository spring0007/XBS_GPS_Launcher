package com.sczn.wearlauncher.card.sport;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.SystemClock;
import android.provider.Settings;

import com.sczn.wearlauncher.OperationService;
import com.sczn.wearlauncher.app.LauncherApp;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsBroadcastReceiver;
import com.sczn.wearlauncher.base.util.DateFormatUtil;
import com.sczn.wearlauncher.base.util.SensorMgrWrap;
import com.sczn.wearlauncher.db.Provider;
import com.sczn.wearlauncher.db.Provider.ColumnsStep;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.userinfo.UserInfoUtil;

import java.math.BigDecimal;
import java.util.Calendar;

public class SportMgrUtil {

    private static class Hold {
        private static SportMgrUtil instances = new SportMgrUtil();
    }

    public static SportMgrUtil getInstances() {
        return Hold.instances;
    }

    public static final String ACTION_ALARM_INIT = "broadcast.alarm.init";
    public static final String ACTION_ALARM_STEP = "broadcast.alarm.step";
    public static final String ACTION_ALARM_SLEEP = "broadcast.alarm.sleep";

    public static final String ACTION_ALARM_NEW_DAY = "broadcast.alarm.newday";

    public static final long ALARM_DELAY_SHORT = 10 * 1000;
    public static final long ALARM_DELAY_NORMAL = 1 * 60 * 1000;

    public static final int STEP_MAX_ONE_MINUTE = 300;

    private AlarmManager mAlarmManager;
    private SensorManagerReceiver mSensorManagerReceiver;

    private PendingIntent mInitAlarm;
    private PendingIntent mStepAlarm;
    private PendingIntent mNewDayAlarm;
    private StepInfoToday mStepInfoToday;

    public SportMgrUtil() {
        mAlarmManager = (AlarmManager) LauncherApp.appContext.getSystemService(Context.ALARM_SERVICE);

        mSensorManagerReceiver = new SensorManagerReceiver();

        mInitAlarm = PendingIntent.getBroadcast(LauncherApp.appContext, 0, new Intent(ACTION_ALARM_INIT), 0);
        mStepAlarm = PendingIntent.getBroadcast(LauncherApp.appContext, 0, new Intent(ACTION_ALARM_STEP), 0);
        mNewDayAlarm = PendingIntent.getBroadcast(LauncherApp.appContext, 0, new Intent(ACTION_ALARM_NEW_DAY), 0);
    }

    public void startMgr(Context context) {
        mSensorManagerReceiver.register(context);

        initStepToday();
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + ALARM_DELAY_SHORT, mInitAlarm);
    }

    public void stopMgr(Context context) {
        mSensorManagerReceiver.unRegister(context);
        mAlarmManager.cancel(mInitAlarm);
        mAlarmManager.cancel(mStepAlarm);
    }

    int socketServiceTimes = 1;

    /**
     * 保存运动数据
     */
    private void savaSportsInfo() {
        final Intent i = new Intent(LauncherApp.appContext, OperationService.class);
        i.putExtra(OperationService.AGR_OPRATION_TYPE, OperationService.TYPE_SAVA_SENSOR);
        LauncherApp.appContext.startService(i);
        // 启动socket service
        if ((socketServiceTimes % 3) == 0) {
            // Intent j = new Intent(LauncherApp.appContext, SocketService.class);
            // LauncherApp.appContext.startService(j);
        }
        socketServiceTimes++;
    }

    private void resetSportsState() {
        final Intent i = new Intent(LauncherApp.appContext, OperationService.class);
        i.putExtra(OperationService.AGR_OPRATION_TYPE, OperationService.TYPE_RESET_DATA);

        LauncherApp.appContext.startService(i);
    }

    private class SensorManagerReceiver extends AbsBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // step and sleep
            final String action = intent.getAction();

            MxyLog.d(this, "onReceive--action=" + action);
            if (ACTION_ALARM_INIT.equals(action)) {
                mAlarmManager.cancel(mStepAlarm);
                mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + ALARM_DELAY_NORMAL, mStepAlarm);
                savaSportsInfo();
                return;
            }
            if (ACTION_ALARM_STEP.equals(action)) {
                mAlarmManager.cancel(mStepAlarm);
                mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + ALARM_DELAY_NORMAL, mStepAlarm);
                savaSportsInfo();
                return;
            }

            if (ACTION_ALARM_NEW_DAY.equals(action)) {
                initStepToday();
                return;
            }

            if (Intent.ACTION_DATE_CHANGED.equals(action) || Intent.ACTION_TIME_CHANGED.equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
                resetSportsState();

                return;
            }
        }

        @Override
        public IntentFilter getIntentFilter() {
            // TODO Auto-generated method stub
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_ALARM_INIT);
            filter.addAction(ACTION_ALARM_STEP);
            filter.addAction(ACTION_ALARM_NEW_DAY);

            filter.addAction(Intent.ACTION_DATE_CHANGED);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            return filter;
        }
    }

    public StepInfoToday getStepInfoToday() {
        if (mStepInfoToday == null) {
            mStepInfoToday = new StepInfoToday();
        }
        return mStepInfoToday;
    }

    private void notifyStepTodayChange(Context context) {

        SPUtils.setParam(context, StepInfoToday.SHARE_KEY_STEP_TODAY, Integer.valueOf(getStepInfoToday().getmSteps()));
        SPUtils.setParam(context, StepInfoToday.SHARE_KEY_KCAL_TODAY, String.valueOf(getStepInfoToday().getmKcal()));
        SPUtils.setParam(context, StepInfoToday.SHARE_KEY_DISTODAY, String.valueOf(getStepInfoToday().getmDis()));

        final Intent i = new Intent(StepInfoToday.ACTION_CHANGED);
        context.sendBroadcast(i);
    }

    private void initStepToday() {
        final Intent i = new Intent(LauncherApp.appContext, OperationService.class);
        i.putExtra(OperationService.AGR_OPRATION_TYPE, OperationService.GLOBAL_INIT_STEP_TODAY);
        LauncherApp.appContext.startService(i);
    }

    public void onInitStepToday(Context context) {
        final StepInfoToday info = new StepInfoToday();

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);

        for (int i = 0; i < 24; i++) {
            calendar.set(Calendar.HOUR_OF_DAY, i);
            long time = calendar.getTimeInMillis();
            String selection = ColumnsStep.COLUMNS_GET_TIME + " == " + time;

            Cursor cursor = context.getContentResolver().query(ColumnsStep.CONTENT_URI, null, selection, null, null);
            if (cursor == null) {
                continue;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                continue;
            } else {
                try {
                    final int steps = cursor.getInt(cursor.getColumnIndex(ColumnsStep.COLUMNS_SETP_COUNT));
                    final double kcal = cursor.getDouble(cursor.getColumnIndex(ColumnsStep.COLUMNS_KCAL));
                    final double dis = cursor.getDouble(cursor.getColumnIndex(ColumnsStep.COLUMNS_DISTANCE));

                    info.setmSteps(info.getmSteps() + steps);
                    info.setmKcal(info.getmKcal() + kcal);
                    info.setmDis(info.getmDis() + dis);
                    info.addStepInHour(steps, i);
                } catch (Exception e) {
                    MxyLog.e(this, "onInitStepToday--e=" + e.toString());
                } finally {
                    cursor.close();
                }
            }
        }

        this.mStepInfoToday = info;
        notifyStepTodayChange(context);

        final Calendar c = Calendar.getInstance();
        final long start = c.getTimeInMillis();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.add(Calendar.HOUR_OF_DAY, 24);

        mAlarmManager.cancel(mNewDayAlarm);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + (c.getTimeInMillis() - start), mNewDayAlarm);

        //MxyLog.d(this, "mNewDayAlarm--offset=" + (c.getTimeInMillis() - start));
    }

    public void onStepTodayUpdate(Context context, int hour, int step, double kcal, double dis) {

        getStepInfoToday().setmSteps(getStepInfoToday().getmSteps() + step);
        getStepInfoToday().setmKcal(getStepInfoToday().getmKcal() + kcal);
        getStepInfoToday().setmDis(getStepInfoToday().getmDis() + dis);
        getStepInfoToday().addStepInHour(step, hour);
        notifyStepTodayChange(context);
    }

    public void onInitSensor(Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_STEP, 0);
        Settings.System.putLong(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP, 0L);
        Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE, SensorMgrWrap.INVALUED_VALUE);
        Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE_START, SensorMgrWrap.INVALUED_VALUE);
        Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_LAST_STATE, SensorMgrWrap.INVALUED_VALUE);

        onInitStepToday(context);
    }

    public void onResetData(Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE, SensorMgrWrap.INVALUED_VALUE);
        Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE_START, SensorMgrWrap.INVALUED_VALUE);
        Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_LAST_STATE, SensorMgrWrap.INVALUED_VALUE);

        onInitStepToday(context);
    }

    public void onSavaData(Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        final int lastState = Settings.System.getInt(contentResolver, SensorMgrWrap.SETTING_KEY_LAST_STATE, SensorMgrWrap.INVALUED_VALUE);
        boolean stateChanged = false;
    }

    private void updateDbSleep(Context context, int sleepState) {
        // TODO Auto-generated method stub
        final ContentResolver contentResolver = context.getContentResolver();
        Calendar calendar = Calendar.getInstance();
        final long currTime = calendar.getTimeInMillis();

        final String dateString = DateFormatUtil.getTimeString(DateFormatUtil.YYYY_MM_DD_HM, currTime);

        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.HOUR, 9);
        final long amNine = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR, 12);
        final long pmNine = calendar.getTimeInMillis();

        if (currTime > amNine && currTime < pmNine) {
            sleepState = SensorMgrWrap.SLEEP_STATE_NONE;
        }

        final int lastState = Settings.System.getInt(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE, SensorMgrWrap.INVALUED_VALUE);
        final long lastStartTime = Settings.System.getLong(contentResolver,
                SensorMgrWrap.SETTING_KEY_SLEEP_STATE_START, SensorMgrWrap.INVALUED_VALUE);

        if (SensorMgrWrap.INVALUED_VALUE == lastState || SensorMgrWrap.INVALUED_VALUE == lastStartTime) {
            Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE, sleepState);
            Settings.System.putLong(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE_START, currTime);
        } else if (lastState != sleepState) {
            final ContentValues values = new ContentValues();
            values.put(Provider.ColumnsSleep.COLUMNS_START_TIME, lastStartTime);
            values.put(Provider.ColumnsSleep.COLUMNS_END_TIME, currTime);
            values.put(Provider.ColumnsSleep.COLUMNS_SLEEP_STATE, lastState);
            values.put(Provider.ColumnsSleep.COLUMNS_SLEEP_STATE_STEP, 0);

            values.put(Provider.ColumnsSleep.COLUMNS_TIME_STRING, dateString);

            if (null != contentResolver.insert(Provider.ColumnsSleep.CONTENT_URI, values)) {
                Settings.System.putInt(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE, sleepState);
                Settings.System.putLong(contentResolver, SensorMgrWrap.SETTING_KEY_SLEEP_STATE_START, currTime);
            }
        } else {
        }

    }

    private void updateDbStep(Context context, int step) {
        final ContentResolver contentResolver = context.getContentResolver();
        Calendar c = Calendar.getInstance();

        final String dateString = DateFormatUtil.getTimeString(DateFormatUtil.YYYY_MM_DD_HM, c.getTimeInMillis());

        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        final long currentHour = c.getTimeInMillis();

        final int hourIndex = c.get(Calendar.HOUR_OF_DAY);

        int dbStep = 0;
        double dbCal = 0.0;
        double dbDis = 0.0;

        int recordId = -1;

        Cursor cursor = contentResolver.query(Provider.ColumnsStep.CONTENT_URI,
                null,
                ColumnsStep.COLUMNS_GET_TIME
                        + "==" + String.valueOf(currentHour), null, null);

        if (cursor != null && cursor.getCount() != 0
                && cursor.moveToFirst()) {

            dbStep = cursor.getInt(cursor.getColumnIndex(ColumnsStep.COLUMNS_SETP_COUNT));
            dbCal = cursor.getDouble(cursor.getColumnIndex(ColumnsStep.COLUMNS_KCAL));
            dbDis = cursor.getDouble(cursor.getColumnIndex(ColumnsStep.COLUMNS_DISTANCE));
            recordId = cursor.getInt(cursor.getColumnIndex(ColumnsStep._ID));
        } else {
            dbStep = 0;
        }

        if (cursor != null) {
            cursor.close();
        }

        final ContentValues values = new ContentValues();
        final double addDis = getDistance(context, step);
        final double addKcal = getCal(context, addDis);
        values.put(ColumnsStep.COLUMNS_SETP_COUNT, dbStep + step);
        values.put(ColumnsStep.COLUMNS_DISTANCE, dbDis + addDis);
        values.put(ColumnsStep.COLUMNS_KCAL, dbCal + addKcal);
        values.put(ColumnsStep.COLUMNS_TIME_STRING, dateString);
        if (recordId == -1) {
            values.put(ColumnsStep.COLUMNS_GET_TIME, currentHour);
            contentResolver.insert(Provider.ColumnsStep.CONTENT_URI, values);

        } else {
            contentResolver.update(ContentUris.withAppendedId(ColumnsStep.CONTENT_URI, recordId),
                    values, null, null);
        }
        onStepTodayUpdate(context, hourIndex, step, addKcal, addDis);
    }

    private static double getDistance(Context context, int step) {
        double distance = 0;    //单位公里
        double oneStepDistance;
        int height;
        height = UserInfoUtil.getHeightValue(context);
        if (height < 165) {
            oneStepDistance = UserInfoUtil.USER_ONE_STEP_DISTANCE_SMALL;
        } else if (height < 185) {
            oneStepDistance = UserInfoUtil.USER_ONE_STEP_DISTANCE_NORMAL;
        } else {
            oneStepDistance = UserInfoUtil.USER_ONE_STEP_DISTANCE_BIG;
        }

        distance = step * oneStepDistance * 0.001;
        return new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static double getCal(Context context, double distance) {
        double cal = 0;

        int weight = UserInfoUtil.getWeightValue(context);

        cal = weight * distance * 1.036;

        return new BigDecimal(cal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}

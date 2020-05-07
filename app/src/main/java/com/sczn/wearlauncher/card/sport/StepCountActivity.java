package com.sczn.wearlauncher.card.sport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsBroadcastReceiver;
import com.sczn.wearlauncher.card.view.ChartView;

import java.util.HashMap;

/**
 * Created by mxy on 2017/05/06.
 */
public class StepCountActivity extends Activity {

    private ChartView mDataLineChartView;
    private TextView mTodaySteps;
    private StepFreshReceiver mStepFreshReceiver;
    private float[] mStepsValues = new float[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStepFreshReceiver = new StepFreshReceiver();

        setContentView(R.layout.activity_card_steps);
        mDataLineChartView = findViewById(R.id.chart_line);
        mTodaySteps = findViewById(R.id.today_steps);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mStepFreshReceiver.register(this);
        freshView();
    }

    @Override
    protected void onPause() {
        mStepFreshReceiver.unRegister(this);
        super.onPause();
    }

    private void freshView() {
        mTodaySteps.setText(String.valueOf(SportMgrUtil.getInstances().getStepInfoToday().getmSteps()));

        mDataLineChartView.setIsHistogram(true);

        final HashMap<String, String> stepDetail = SportMgrUtil.getInstances().getStepInfoToday().getmStepDetails();
        for (int i = 0; i < mStepsValues.length; i++) {
            final String stepString = stepDetail.get(String.valueOf(i));
            mStepsValues[i] = stepString == null ? 0 : Integer.parseInt(stepString);
        }
        mDataLineChartView.setValues(mStepsValues);
    }

    private class StepFreshReceiver extends AbsBroadcastReceiver {
        @Override
        public IntentFilter getIntentFilter() {
            final IntentFilter filter = new IntentFilter();
            filter.addAction(StepInfoToday.ACTION_CHANGED);
            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            freshView();
        }
    }
}

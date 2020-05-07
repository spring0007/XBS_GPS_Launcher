package com.sczn.wearlauncher.card;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absDialogFragment;
import com.sczn.wearlauncher.card.sport.SportHelper;
import com.sczn.wearlauncher.card.view.CircleBarView;
import com.sczn.wearlauncher.userinfo.UserInfoUtil;
import com.sczn.wearlauncher.util.ThreadUtil;

/**
 * 显示当天步数
 */
public class CardFragmentStep extends absDialogFragment {

    private final String TAG = "CardFragmentStep";

    private TextView mStep;
    private TextView mDis;
    private TextView mCal;
    private TextView mTarget;
    private CircleBarView mStepCircleBar;

    private final int READ_STEP_MSG = 1;
    private final int READ_STEP_MSG_TIME = 30 * 1000;

    public static CardFragmentStep newInstance() {
        return new CardFragmentStep();
    }


    @Override
    protected int getLayoutResouceId() {
        return R.layout.fragment_card_step;
    }


    protected void initView() {
        mStep = findViewById(R.id.step_count);
        mTarget = findViewById(R.id.step_target);
        mDis = findViewById(R.id.step_dis);
        mCal = findViewById(R.id.step_cal);

        mStepCircleBar = findViewById(R.id.sport_circle_bar);
    }


    protected void initData() {
        mTarget.setText(String.valueOf(UserInfoUtil.getStepTarget(getActivity())));
        mRootView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * 暂时没有步数详情页面
                 */
                /*try {
                    Intent i = new Intent(getActivity(), StepCountActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(i);
                } catch (Exception e) {
                    MxyLog.e(this, "gotoSleepActivity--" + e.toString());
                }*/
            }
        });
    }

    @Override
    protected void creatView() {
        initView();
        initData();
        freshView();
        mHandler.sendEmptyMessageDelayed(READ_STEP_MSG, READ_STEP_MSG_TIME);
    }


    private void freshView() {
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                final int step = SportHelper.getHelper().getCurrentStepData(getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStep.setText(String.valueOf(step));
                        mStepCircleBar.setValue(CircleBarView.MAX_OFFSET * step / UserInfoUtil.getStepTarget(getActivity()));
                    }
                });
            }
        });
    }

    @Override
    protected void destorytView() {

    }

    /**
     * 在当前页面30秒定时刷新步数
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            freshView();
            mHandler.sendEmptyMessageDelayed(READ_STEP_MSG, READ_STEP_MSG_TIME);
            return false;
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}

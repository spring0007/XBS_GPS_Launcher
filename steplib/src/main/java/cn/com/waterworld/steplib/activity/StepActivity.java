package cn.com.waterworld.steplib.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.com.waterworld.steplib.R;
import cn.com.waterworld.steplib.step.UpdateUiCallBack;
import cn.com.waterworld.steplib.step.service.StepService;
import cn.com.waterworld.steplib.step.utils.SharedPreferencesUtils;
import cn.com.waterworld.steplib.view.StepArcView;

/**
 * 记步主页
 */
public class StepActivity extends AppCompatActivity {

    public static final String INTENT_STEP_DATA_ACTION = "GET_STEP_DATA";
    public static final String INTENT_STEP_VALUE = "step_value";

    private StepArcView cc;
    private TextView tv_isSupport;
    private SharedPreferencesUtils sp;

    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        assignViews();
        initData();
    }

    private void assignViews() {
        cc = findViewById(R.id.cc);
        tv_isSupport = findViewById(R.id.tv_isSupport);
    }

    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText(R.string.step_by_step);
        setupService();
    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //发送广播,将当前的步数发送出去,接收到上传服务器
            Intent intent = new Intent(INTENT_STEP_DATA_ACTION);
            intent.putExtra(INTENT_STEP_VALUE, stepService.getStepCount());
            sendBroadcast(intent);

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}

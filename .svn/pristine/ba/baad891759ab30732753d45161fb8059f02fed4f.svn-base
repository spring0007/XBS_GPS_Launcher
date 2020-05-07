package cn.com.waterworld.alarmclocklib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.com.waterworld.alarmclocklib.adapters.AlarmAdapter;
import cn.com.waterworld.alarmclocklib.db.AlarmDao;
import cn.com.waterworld.alarmclocklib.interfaces.OnSwithChangeListener;
import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.AlarmClock;
import cn.com.waterworld.alarmclocklib.util.ListUtil;
import cn.com.waterworld.alarmclocklib.util.Unit;

/**
 * Created by wangfeng on 2018/6/11.
 * 闹钟主界面
 */
public class AlarmClockActivity extends BaseActivity {
    private RelativeLayout rlAddAlarm;
    private TextView tvNoAlarmTip;
    private RecyclerView recyclerViewAlarm;

    private List<AlarmBean> alarmBeanList;
    private AlarmAdapter alarmAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        defaultData();
    }

    /**
     * 展示显示闹钟的列表
     */
    private void defaultData() {
        List<AlarmBean> list = AlarmDao.getInstance().queryAll();
        sortData(list);
        if (list.size() > 0 && alarmAdapter != null) {
            recyclerViewAlarm.setVisibility(View.VISIBLE);
            tvNoAlarmTip.setVisibility(View.GONE);
            alarmAdapter.addData(list);
            alarmAdapter.notifyDataSetChanged();
        } else {
            recyclerViewAlarm.setVisibility(View.GONE);
            tvNoAlarmTip.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 将闹钟按时间排序
     *
     * @param list
     */
    private void sortData(List<AlarmBean> list) {
        if (ListUtil.isEmpty(list)) {
            return;
        }
        Collections.sort(list, new Comparator<AlarmBean>() {
            @Override
            public int compare(AlarmBean bean1, AlarmBean bean2) {
                if (bean1.getAlarmTime() > bean2.getAlarmTime()) {
                    return 1;
                } else if (bean1.getAlarmTime() < bean2.getAlarmTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    private void initListener() {
        //添加闹钟
        rlAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmClockActivity.this, SetAlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        rlAddAlarm = findViewById(R.id.rl_add_alarm);
        tvNoAlarmTip = findViewById(R.id.tv_no_alarm);
        recyclerViewAlarm = findViewById(R.id.recyclerViewAlarm);
        alarmBeanList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(AlarmClockActivity.this, alarmBeanList);
        alarmAdapter.setOnItemClickListener(onItemClickListener);
        recyclerViewAlarm.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewAlarm.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewAlarm.setAdapter(alarmAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(200);
        defaultItemAnimator.setRemoveDuration(200);
        recyclerViewAlarm.setItemAnimator(defaultItemAnimator);
        alarmAdapter.setSwithChangeListener(swithChangeListener);
    }

    /**
     * Item开关
     */
    private OnSwithChangeListener swithChangeListener = new OnSwithChangeListener() {
        @Override
        public void OnChange(AlarmBean bean, boolean isChange) {
            if (bean != null && !bean.getID().equals("")) {
                if (isChange) {
                    bean.setIsOff(0);
                    setSystemAlarm(bean);
                } else {
                    bean.setIsOff(1);
                    cancelAlarm(bean);

                }
                AlarmDao.getInstance().updata(bean);
            }
        }
    };


    /**
     * switch开关打开闹钟
     * 例子：str = {"",0,1,1,1,1,1,1,1}
     *
     * @param bean
     */
    private void setSystemAlarm(AlarmBean bean) {
        if (bean.getFlag() == 0) {
            AlarmClock.setAlarmReceiver(AlarmClockActivity.this, 0, bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                    Integer.parseInt(bean.getID()), 0, getString(R.string.str_alarm_tip), 0);
        } else {
            String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1") && bean.getIsOff() == 0) {
                    AlarmClock.setAlarmReceiver(AlarmClockActivity.this, bean.getFlag(), bean.getAlarmTime() / 60, bean.getAlarmTime() % 60,
                            Integer.parseInt(bean.getID() + "" + (i - 1)), i - 1, getString(R.string.str_alarm_tip), 0);
                }
            }
        }
    }

    /**
     * switch开关关闭闹钟
     *
     * @param bean
     */
    private void cancelAlarm(AlarmBean bean) {
        if (bean.getFlag() == 0) {
            AlarmClock.deleteAlarmReceiver(this, Integer.parseInt(bean.getID()));
        } else {
            String[] str = Unit.toBinaryString2(bean.getWeekValue()).split("");
            for (int i = 0; i < str.length; i++) {
                if (i >= 2 && str[i].equals("1")) {
                    AlarmClock.deleteAlarmReceiver(this, Integer.parseInt(bean.getID() + "" + (i - 1)));
                }
            }
        }
    }

    /**
     * adapter点击事件
     */
    private AlarmAdapter.OnItemClickListener onItemClickListener = new AlarmAdapter.OnItemClickListener() {
        @Override
        public void OnItemClick(AlarmBean alarmBean) {
            if (null != alarmBean) {
                Intent intent = new Intent(AlarmClockActivity.this, AlarmDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("AlarmBean", alarmBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

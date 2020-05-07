package cn.com.waterworld.alarmclocklib.interfaces;


import cn.com.waterworld.alarmclocklib.model.AlarmBean;

/**
 * 闹钟Adapter开关Item接口
 */
public interface OnSwithChangeListener {

    void OnChange(AlarmBean bean, boolean isChange);

}

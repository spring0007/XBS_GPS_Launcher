package com.sczn.wearlauncher.event;

/**
 * Description:
 * Created by Bingo on 2019/1/21.
 */
public class RefreshBatteryEvent {
    private int batteryLevel;
    private int state;

    public RefreshBatteryEvent(int batteryLevel, int state) {
        this.batteryLevel = batteryLevel;
        this.state = state;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

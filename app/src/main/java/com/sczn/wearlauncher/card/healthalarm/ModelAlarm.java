package com.sczn.wearlauncher.card.healthalarm;

import java.util.Calendar;

import com.sczn.wearlauncher.R;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class ModelAlarm implements Parcelable{
	
	public static final int REPEAT_FLAG_ENABLE = 1;
	public static final int REPEAT_FLAG_SUNDAY = 1 << Calendar.SUNDAY;
	public static final int REPEAT_FLAG_MONDAY = 1 << Calendar.MONDAY;
	public static final int REPEAT_FLAG_TUESDAY = 1 << Calendar.TUESDAY;
	public static final int REPEAT_FLAG_WEDNESDAY = 1 << Calendar.WEDNESDAY;
	public static final int REPEAT_FLAG_THURSDAY = 1 << Calendar.THURSDAY;
	public static final int REPEAT_FLAG_FRIDAY  = 1 << Calendar.FRIDAY;
	public static final int REPEAT_FLAG_SATURDAY = 1 << Calendar.SATURDAY;
	
	public static final int REPEAT_ENABLE_CHECK = REPEAT_FLAG_SUNDAY | REPEAT_FLAG_MONDAY | REPEAT_FLAG_TUESDAY
			| REPEAT_FLAG_WEDNESDAY | REPEAT_FLAG_THURSDAY | REPEAT_FLAG_FRIDAY | REPEAT_FLAG_SATURDAY;
	

	public static final int ALARM_TYPE_SIT = 0;
	public static final int ALARM_TYPE_DRINK = 1;
	
	public static final int INVALUED_ID = Integer.MIN_VALUE;

	private int ID;
	private long timeInDay;
	private int repeatDay;
	private int type;
	private boolean enable;
	
	private long nextTimeOffset;

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public long getTimeInDay() {
		return timeInDay;
	}
	public void setTimeInDay(long timeInDay) {
		this.timeInDay = timeInDay;
	}
	public int getRepeatDay() {
		return repeatDay;
	}
	public void setRepeatDay(int repeatDay) {
		this.repeatDay = repeatDay;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public long getNextTimeOffset() {
		return nextTimeOffset;
	}
	public void setNextTimeOffset(long nextTimeOffset) {
		this.nextTimeOffset = nextTimeOffset;
	}
	
	public boolean isValue(long currTime, int dayFlag){
		if(!enable){
			return false;
		}
		if(timeInDay <= currTime){
			return false;
		}
		//MxyLog.d(this, "isValue--repeatDay=" + repeatDay + "--repeatDay & REPEAT_FLAG_ENABLE)=" + (repeatDay & REPEAT_FLAG_ENABLE) + "--dayFlag=" + dayFlag + "--repeatDay & dayFlag=" + (repeatDay & dayFlag));
		if((repeatDay & REPEAT_FLAG_ENABLE) == 0){
			return true;
		}
		
		return (repeatDay & (1 << dayFlag)) != 0;
	}
	
	public String getTimeString(){
		final int hour = (int) (timeInDay/3600000);
		final int min = (int) ((timeInDay%3600000)/60000);
		
		return String.format("%02d:%02d", hour,min);
		//return DateFormatUtil.getTimeString(DateFormatUtil.HM, timeInDay);
	}
	public String getRepeatString(Context context){
		if((repeatDay & REPEAT_FLAG_ENABLE) == 0){
			return context.getString(R.string.pick_weekday_noone);
		}
		
		String repeat = "";
		if((repeatDay & REPEAT_FLAG_SUNDAY) > 0){repeat = addRepeatString(repeat, context.getString(R.string.pick_weekday_su));}
		if((repeatDay & REPEAT_FLAG_MONDAY) > 0){repeat = addRepeatString(repeat, context.getString(R.string.pick_weekday_mo));}
		if((repeatDay & REPEAT_FLAG_TUESDAY) > 0){repeat = addRepeatString(repeat, context.getString(R.string.pick_weekday_tu));}
		if((repeatDay & REPEAT_FLAG_WEDNESDAY) > 0){repeat = addRepeatString(repeat, context.getString(R.string.pick_weekday_we));}
		if((repeatDay & REPEAT_FLAG_THURSDAY) > 0){repeat = addRepeatString(repeat, context.getString(R.string.pick_weekday_th));}
		if((repeatDay & REPEAT_FLAG_FRIDAY) > 0){repeat = addRepeatString(repeat, context.getString(R.string.pick_weekday_fr));}
		if((repeatDay & REPEAT_FLAG_SATURDAY) > 0){repeat = addRepeatString(repeat, context.getString(R.string.pick_weekday_sa));}
		
		if(repeat.isEmpty()){
			return context.getString(R.string.pick_weekday_noone);
		}
		return repeat;
	}
	
	private String addRepeatString(String src, String add){
		if(!src.isEmpty()){
			src += ",";
		}
		return src + add;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(ID);
		dest.writeLong(timeInDay);
		dest.writeInt(repeatDay);
		dest.writeInt(type);
		dest.writeInt(enable ? 1 :0);
	}
	
	public static final Parcelable.Creator<ModelAlarm> CREATOR = new Creator<ModelAlarm>() {
        @Override
        public ModelAlarm createFromParcel(Parcel source) {
        	ModelAlarm alarm = new ModelAlarm();
        	alarm.ID = source.readInt();
            alarm.timeInDay = source.readLong();
            alarm.repeatDay = source.readInt();
            alarm.type = source.readInt();
            alarm.enable = source.readInt() > 0;
            
            return alarm;
        }

        public ModelAlarm[] newArray(int size) {
            return new ModelAlarm[size];
        }
    };
}

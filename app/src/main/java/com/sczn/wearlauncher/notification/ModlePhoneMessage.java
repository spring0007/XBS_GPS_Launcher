/**
 * 
 */
package com.sczn.wearlauncher.notification;

import com.sczn.wearlauncher.base.util.DateFormatUtil;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * @author mxy		Hold json Date push from App notifition
 *			
 */
public class ModlePhoneMessage implements Parcelable{

    private String packageName;
    private String tickerText;
    private String id;
    private String time;
    private String appName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTickerText() {
    	if(tickerText.isEmpty()){
    		return "--";
    	}
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
    	try {
			return DateFormatUtil.getTimeString(DateFormatUtil.HM, Long.parseLong(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(tickerText);
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(appName);
    }

    public static final Parcelable.Creator<ModlePhoneMessage> CREATOR = new Creator<ModlePhoneMessage>() {
        @Override
        public ModlePhoneMessage createFromParcel(Parcel source) {
            ModlePhoneMessage ModlePhoneMessage = new ModlePhoneMessage();
            ModlePhoneMessage.packageName = source.readString();
            ModlePhoneMessage.tickerText = source.readString();
            ModlePhoneMessage.id = source.readString();
            ModlePhoneMessage.time = source.readString();
            ModlePhoneMessage.appName = source.readString();
            return ModlePhoneMessage;
        }

        public ModlePhoneMessage[] newArray(int size) {
            return new ModlePhoneMessage[size];
        }
    };

}

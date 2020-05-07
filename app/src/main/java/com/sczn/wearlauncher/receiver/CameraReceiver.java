package com.sczn.wearlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.sczn.wearlauncher.app.MxyLog;

/**
 * 监听拍照的动作,和获取拍照照片的路径
 */
public class CameraReceiver extends BroadcastReceiver {

    private final String TAG = "CameraReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        MxyLog.w(TAG, "咔嚓,拍照了");
        Cursor cursor = null;
        try {
            if (intent.getData() != null) {
                cursor = context.getContentResolver().query(intent.getData(), null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                    MxyLog.w(TAG, "咔嚓,拍照了:" + imagePath);
                    // String base64Img = Base64Utils.encodeFile(imagePath);
                    // MxyLog.w(TAG, "咔嚓,拍照了:" + base64Img.length());

                    // UploadImageCmd imageCmd = new UploadImageCmd(base64Img, new CommandResultCallback() {
                    //     @Override
                    //     public void onSuccess(String baseObtain) {
                    //     }
                    //     @Override
                    //     public void onFail() {
                    //     }
                    // });
                    // WaterSocketManager.getInstance().send(imageCmd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

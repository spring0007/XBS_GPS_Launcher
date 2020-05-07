package com.sczn.wearlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sczn.wearlauncher.OperationService;
import com.sczn.wearlauncher.app.LauncherApp;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            initSensorManager(context);
        }
    }

    private void initSensorManager(Context context) {
        final Intent i = new Intent(LauncherApp.appContext, OperationService.class);
        i.putExtra(OperationService.AGR_OPRATION_TYPE, OperationService.TYPE_INIT_SENSOR);
        context.startService(i);
    }
}

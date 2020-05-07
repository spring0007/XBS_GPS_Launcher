package com.sczn.wearlauncher.notification;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;

public class ActivityNotification extends AbsActivity {
    private static final String TAG = ActivityNotification.class.getSimpleName();

    private static final String FRAGMENT_TAG_NOTIFICATION = "fragment_main_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
    }

    private void initView() {
        final FragmentNtfMain fragment = (FragmentNtfMain) getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NOTIFICATION);
        if (fragment == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.notification_fragment, new FragmentNtfMain(), FRAGMENT_TAG_NOTIFICATION);
            ft.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

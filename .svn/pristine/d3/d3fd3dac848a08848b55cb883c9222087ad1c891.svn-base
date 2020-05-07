package com.sczn.wearlauncher.card;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.absFragment;

public class CardFragmentAltitude extends absFragment {

    public static final String ARG_IS_TMP = "is_tmp";

    public static CardFragmentAltitude newInstance(boolean isTmp) {
        CardFragmentAltitude fragment = new CardFragmentAltitude();
        Bundle bdl = new Bundle();
        bdl.putBoolean(ARG_IS_TMP, isTmp);
        fragment.setArguments(bdl);
        return fragment;

    }

    private Context mContext;
    private RelativeLayout mRootView;
    private TextView mAltTextView, mBadSignal;
    private int mAlt = 0;
    private LinearLayout mAltPart;

    private boolean isTmp = false;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bdl = getArguments();
        if (bdl != null) {
            isTmp = bdl.getBoolean(ARG_IS_TMP, false);
        }
    }

    @Override
    protected int getLayoutResouceId() {
        // TODO Auto-generated method stub
        mContext = getActivity();
        return R.layout.fragment_card_altitude;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mRootView = findViewById(R.id.altitude_root);
        mAltTextView = findViewById(R.id.alt_text);
        mAltPart = findViewById(R.id.alt_part);
        mAltPart.setVisibility(View.GONE);
        mBadSignal = findViewById(R.id.bad_signal);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        locateSetting();
    }

    private void locateSetting() {
    }

    @Override
    protected void startFreshData() {
        // TODO Auto-generated method stub
        super.startFreshData();
        if (isTmp) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //启动定位
        locateSetting();
    }

    @Override
    protected void endFreshData() {
        // TODO Auto-generated method stub
        super.endFreshData();
        if (isTmp) {
            return;
        }
        mAltPart.setVisibility(View.GONE);
        mBadSignal.setVisibility(View.GONE);

//		if(mBadSignal.getVisibility() == View.VISIBLE){
//			mBadSignal.setVisibility(View.GONE);
//		}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

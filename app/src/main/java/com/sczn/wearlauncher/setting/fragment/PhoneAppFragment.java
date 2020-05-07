package com.sczn.wearlauncher.setting.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sczn.wearlauncher.R;

public class PhoneAppFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    private View mRootView = null;
    private ImageView mAppView;

    public static PhoneAppFragment newInstance(OnFragmentInteractionListener listener) {
        final PhoneAppFragment fragment = new PhoneAppFragment();

        fragment.listener = listener;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            if (mRootView.getParent() != null) {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                parent.removeAllViews();
            }
        } else {
            mRootView = inflater.inflate(R.layout.fragment_phone_app, null);
        }
        //
        mRootView.findViewById(R.id.next_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNext();
                }
            }
        });
        mAppView = (ImageView) mRootView.findViewById(R.id.app_view);
        //
        initListener();
        //
        return mRootView;
    }

    private void initListener() {
        mAppView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoScaleCodePic();
            }
        });
    }

    public void autoScaleCodePic() {
        ImageView imageView = new ImageView(getActivity());

        imageView.setImageDrawable(mAppView.getDrawable());
        //
        final ViewHolder viewHolder = new ViewHolder(imageView);

        DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(viewHolder)
                // .setHeader(R.layout.dialog_header)
                .setHeader(null)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(null)
                .setExpanded(false)
                .setContentWidth(240)
                .setContentHeight(240)
                .setOnCancelListener(null)
//                        .setOverlayBackgroundResource(android.R.color.transparent)
                .setContentBackgroundResource(android.R.color.black)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
    }

    public interface OnFragmentInteractionListener {
        void onNext();
    }
}

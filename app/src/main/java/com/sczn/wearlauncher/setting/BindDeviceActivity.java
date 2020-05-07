package com.sczn.wearlauncher.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.setting.adapetr.CodeImageAdapter;
import com.sczn.wearlauncher.setting.fragment.BindFragment;
import com.sczn.wearlauncher.setting.fragment.PhoneAppFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 */
public class BindDeviceActivity extends AbsActivity {

    private ViewPager viewPager;
    private RadioGroup radioGroup;

    private ImageView imgFirstPoint;
    private ImageView imgSecondPoint;

    private BindFragment bindFragment;
    private PhoneAppFragment phoneAppFragment;

    // FIXME: 2017/12/8 新增加一个Handler 处理3秒后对应的二维码放大效果
    private Handler autoFullScreenHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                if (viewPager.getCurrentItem() == 0) {
                    //如果是下载二维码 通知下载二维码自动放大
                    phoneAppFragment.autoScaleCodePic();
                } else if (viewPager.getCurrentItem() == 1) {
                    //如果是绑定码的二维码的界面 通知绑定码的二维码自动放大
                    bindFragment.autoScaleCodePic();
                }
            } catch (Exception e) {

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.container);

        List<Fragment> frags = new ArrayList<>();
        bindFragment = new BindFragment();
        phoneAppFragment = PhoneAppFragment.newInstance(mPhoneAppFragmentListener);
        frags.add(phoneAppFragment);
        frags.add(bindFragment);
        //
        viewPager.setAdapter(new CodeImageAdapter(getSupportFragmentManager(), frags));
        //
        radioGroup = (RadioGroup) findViewById(R.id.bottom_layout);
        //
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                autoFullScreenHandler.removeMessages(0);
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.leftBtn);

                        imgFirstPoint.setImageResource(R.drawable.icon_bind_black_point);
                        imgSecondPoint.setImageResource(R.drawable.icon_bind_white_point);
                        break;
                    case 1:
                        radioGroup.check(R.id.rightBtn);

                        imgFirstPoint.setImageResource(R.drawable.icon_bind_white_point);
                        imgSecondPoint.setImageResource(R.drawable.icon_bind_black_point);
                        break;
                }
                autoFullScreenHandler.sendEmptyMessageDelayed(0, 3 * 1000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgFirstPoint = (ImageView) findViewById(R.id.img_first_point);
        imgSecondPoint = (ImageView) findViewById(R.id.img_second_point);
        autoFullScreenHandler.sendEmptyMessageDelayed(0, 3 * 1000);
    }

    private PhoneAppFragment.OnFragmentInteractionListener mPhoneAppFragmentListener = new PhoneAppFragment.OnFragmentInteractionListener() {
        @Override
        public void onNext() {
            viewPager.setCurrentItem(1);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

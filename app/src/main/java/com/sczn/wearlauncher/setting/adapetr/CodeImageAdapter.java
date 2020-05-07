package com.sczn.wearlauncher.setting.adapetr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CodeImageAdapter extends FragmentPagerAdapter {
    
    private List<Fragment> frags = new ArrayList<Fragment>();

    public CodeImageAdapter(FragmentManager fm , List<Fragment> fragments) {
        super(fm);
        frags = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return frags.get(arg0);
    }

    @Override
    public int getCount() {
        return frags.size();
    }
    
    
  
}

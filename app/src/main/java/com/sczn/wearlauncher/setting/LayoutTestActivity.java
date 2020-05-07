package com.sczn.wearlauncher.setting;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.base.view.HorizalViewPager;
import com.sczn.wearlauncher.base.view.MyRecyclerView;
import com.sczn.wearlauncher.menu.adapter.MainApplistFragmentAdapter;
import com.sczn.wearlauncher.menu.adapter.ViewPagerAdapter;
import com.sczn.wearlauncher.menu.bean.AppMenu;
import com.sczn.wearlauncher.menu.util.AppListUtil;
import com.sczn.wearlauncher.util.ThreadUtil;

import java.util.ArrayList;

/**
 * Description:
 * Created by Bingo on 2019/3/6.
 */
public class LayoutTestActivity extends AbsActivity {

    private HorizalViewPager viewPager;
    private ViewPagerAdapter adapter;

    private ArrayList<View> vlist;
    private ArrayList<AppMenu> menuList;

    private MainApplistFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_test);
        initView();
        initMenu();
    }

    private void initView() {
        vlist = new ArrayList<>();
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(vlist);
        viewPager.setAdapter(adapter);

        View view = LayoutInflater.from(this).inflate(R.layout.layout_recycleview, null);
        vlist.add(view);
        adapter.notifyData(vlist);

        MyRecyclerView recyclerView = view.findViewById(R.id.rView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        fragmentAdapter = new MainApplistFragmentAdapter(menuList);
        recyclerView.setAdapter(fragmentAdapter);

    }

    /**
     * 初始化menu数据
     */
    private void initMenu() {
        ThreadUtil.getPool().execute(new Runnable() {
            @Override
            public void run() {
                menuList = AppListUtil.getInctance().getAppList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // for (int x = 0; x < menuList.size(); x++) {
                        //     AppMenu mAppMenu = menuList.get(x);
                        //     View view = LayoutInflater.from(LayoutTestActivity.this).inflate(R.layout.viewpager_item, null);
                        //     TextView mTextView = view.findViewById(R.id.view_title);
                        //     ImageView mimage = view.findViewById(R.id.view_image);
                        //     mTextView.setText(MenuUtil.getName(LayoutTestActivity.this, menuList, x));
                        //     mimage.setImageDrawable(MenuUtil.getIcon(LayoutTestActivity.this, menuList, x, false));
                        //     mimage.setTag(mAppMenu);
                        //     vlist.add(view);
                        // }
                        // adapter.notifyData(vlist);
                        fragmentAdapter.notifyData(menuList);
                    }
                });
            }
        });
    }
}

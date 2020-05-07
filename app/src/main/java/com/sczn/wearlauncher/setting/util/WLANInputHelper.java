package com.sczn.wearlauncher.setting.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.setting.adapetr.InputHelperAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WLANInputHelper {


    public static int INPUT_TYPE_NUMBER = 1;

    public static int INPUT_TYPE_LETTER = 2;

    public static int INPUT_TYPE_SYMBOL = 3;


    public static int CAPS_TYPE_LOWER = 1;

    public static int CAPS_TYPE_UPPER = 2;

    private final Context mContext;
    private final View view;

    private RecyclerView mRv1;

    private int mInputType = INPUT_TYPE_NUMBER;

    private int mInputTypeIndex = 0;

    private int mCapsType = CAPS_TYPE_LOWER;

    private int mCapsTypeIndex = 0;

    private List<String> mNumberStringList;
    private List<String> mLetterStringList;
    private List<String> mSymbolStringList;

    private InputHelperAdapter mDataAdapter;

    private List<String> mDataList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;
    private OnStatusChangeListener mOnStatusChangeListener;

    public WLANInputHelper(Context context, View view) {
        this.mContext = context.getApplicationContext();
        this.view = view;

        initRecyclerView();
        initListener();
        initData();
    }

    private void initRecyclerView() {
        mRv1 = (RecyclerView) findViewById(R.id.keyboard_view);

        final GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);

        mRv1.setLayoutManager(layoutManager);




    }

    private void initListener() {

    }

    private void initData() {
        List<String> list = null;
        if (mInputType == INPUT_TYPE_NUMBER) {
            if (mNumberStringList == null) {
                final String[] stringArray = mContext.getResources().getStringArray(R.array.input_type_number);

                mNumberStringList = Arrays.asList(stringArray);
            }
            list = mNumberStringList;
        } else if (mInputType == INPUT_TYPE_LETTER) {
            if (mLetterStringList == null) {
                final String[] stringArray = mContext.getResources().getStringArray(R.array.input_type_letter);

                mLetterStringList = Arrays.asList(stringArray);
            }
            list = mLetterStringList;
        } else if (mInputType == INPUT_TYPE_SYMBOL) {
            if (mSymbolStringList == null) {
                final String[] stringArray = mContext.getResources().getStringArray(R.array.input_type_symbol);

                mSymbolStringList = Arrays.asList(stringArray);
            }
            list = mSymbolStringList;
        }
        intentInflateRecyclerView(list);
    }

    private void intentInflateRecyclerView(List<String> list) {
        mDataList.clear();

        if (list != null && !list.isEmpty()) {
            mDataList.addAll(list);
        }

        inflateRecyclerView(mDataList);
    }

    private void inflateRecyclerView(List<String> list) {
        if (list == null) {
            return;
        }
        if (mDataAdapter == null) {
            mDataAdapter = new InputHelperAdapter(mContext, list, this);

            mRv1.setAdapter(mDataAdapter);
        } else {
            mDataAdapter.notifyDataSetChanged();
        }
    }

    public void changeInputType() {
        ++mInputTypeIndex;

        final int index = mInputTypeIndex % 3;

        if (index == 0) {
            this.mInputType = INPUT_TYPE_NUMBER;
        } else if (index == 1) {
            this.mInputType = INPUT_TYPE_LETTER;
        } else if (index == 2) {
            this.mInputType = INPUT_TYPE_SYMBOL;
        }

        initData();

        if (mOnStatusChangeListener != null) {
            mOnStatusChangeListener.onStatusChange(mInputType, mCapsType);
        }
    }

    public void changeCaps() {
        ++mCapsTypeIndex;

        final int index = mCapsTypeIndex % 2;

        if (index == 0) {
            this.mCapsType = CAPS_TYPE_LOWER;
        } else if (index == 1) {
            this.mCapsType = CAPS_TYPE_UPPER;
        }

        this.mInputTypeIndex = 1;
        this.mInputType = INPUT_TYPE_LETTER;

        initData();

        if (mOnStatusChangeListener != null) {
            mOnStatusChangeListener.onStatusChange(mInputType, mCapsType);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.mOnStatusChangeListener = listener;
    }

    public OnStatusChangeListener getOnStatusChangeListener() {
        return mOnStatusChangeListener;
    }

    public int getInputType() {
        return mInputType;
    }

    public int getCapsType() {
        return mCapsType;
    }

    private View findViewById(int resId) {
        return view.findViewById(resId);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String character);
    }

    public interface OnStatusChangeListener {
        void onStatusChange(int inputType, int capsType);
    }
}

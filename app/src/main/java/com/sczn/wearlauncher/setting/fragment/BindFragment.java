package com.sczn.wearlauncher.setting.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.ruisocket.Util;

import java.util.Hashtable;

public class BindFragment extends Fragment {

    private TextView mDeviceIdView;
    private TextView mNextView;
    private ImageView mDeviceIdQRCodeView;
    private String Imei;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bind, container, false);
    }

    @Override
    public void onViewCreated(View mRootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mRootView, savedInstanceState);
        mDeviceIdView = mRootView.findViewById(R.id.device_id_view);
        mDeviceIdQRCodeView = mRootView.findViewById(R.id.device_id_qrcode_view);
        mNextView = mRootView.findViewById(R.id.next_view);

        initListener();
        initData();
    }

    public void autoScaleCodePic() {
        ImageView imageView = new ImageView(getActivity());

        imageView.setImageDrawable(mDeviceIdQRCodeView.getDrawable());
        //
        final ViewHolder viewHolder = new ViewHolder(imageView);

        DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(viewHolder)
                // .setHeader(R.layout.dialog_header)
                .setHeader(null)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setPadding(10, 10, 10, 10)
                .setOnDismissListener(null)
                .setExpanded(false)
                .setContentWidth(240)
                .setContentHeight(240)
                .setOnCancelListener(null)
//                        .setOverlayBackgroundResource(android.R.color.transparent)
                .setContentBackgroundResource(android.R.color.white)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
    }

    private void initListener() {
        mDeviceIdQRCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoScaleCodePic();
            }
        });
        //
        mNextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MxyLog.d("", "");
                getActivity().finish();
            }
        });
    }

    private void initData() {
        Imei = Util.getIMEI(getContext());
        if (isGUIDValid(Imei)) {
            mDeviceIdView.setText(String.valueOf(Imei));
            try {
                mDeviceIdQRCodeView.setImageBitmap(Create2DCode(Imei));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mDeviceIdQRCodeView.setImageResource(R.drawable.bg_white);
            mDeviceIdView.setText(getString(R.string.guide_tip_7));
            mNextView.setText(R.string.guide_tip_6);
        }
    }

    public static boolean isGUIDValid(String guid) {
        return guid != null && guid.length() >= 3;
    }

    /**
     * 将指定的内容生成成二维码
     *
     * @param content 将要生成二维码的内容
     * @return 返回生成好的二维码事件
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateTwoDCode(String content) throws WriterException {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 100, 100);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 用字符串生成二维码
     *
     * @return
     * @throws WriterException
     */
    public static Bitmap Create2DCode(String text) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败    
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //二维码边框宽度，这里文档说设置0-4
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 400, 400, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了    
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }

            }
        }
        Log.v("zhangjiaofa", "width = " + width);
        Log.v("zhangjiaofa", "height = " + height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api    
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
     *
     * @param content 将要生成一维码的内容
     * @return 返回生成好的一维码bitmap
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateOneDCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 400, 150);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成显示编码的Bitmap
     *
     * @param contents
     * @param width
     * @param height
     * @param context
     * @return
     */
    protected static Bitmap creatCodeBitmap(String contents, int width, int height, Context context) {
        TextView tv = new TextView(context);
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(height);
        tv.setTextSize(11);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(Color.BLACK);
        tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }

    /**
     * 将两个Bitmap合并成一个
     *
     * @param first
     * @param second
     * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
     * @return
     */
    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        int marginW = 20;
        Bitmap newBitmap =
                Bitmap.createBitmap(first.getWidth() + second.getWidth() + marginW,
                        first.getHeight() + second.getHeight(),
                        Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, marginW, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }

}

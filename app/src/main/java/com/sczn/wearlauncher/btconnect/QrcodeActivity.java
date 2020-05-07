package com.sczn.wearlauncher.btconnect;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.sp.SPUtils;
import com.sczn.wearlauncher.base.util.SysServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;

/**
 * 二维码显示
 */
public class QrcodeActivity extends AbsActivity {

    public static final String QRCODE_IMAGE_NAME = "QrcodeImg";
    public static final String PREFERENCE_KEY_INDEX = "qrcode_index";

    private ImageView mQrcodeImage;
    private QrcodeTask mQrcodeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.fragment_status_qrcode);
        mQrcodeImage = (ImageView) findViewById(R.id.qrcode_image);
        mQrcodeTask = new QrcodeTask();
        initQrcode();
    }


    /**
     *
     */
    public void initQrcode() {
        final String deviceId = getDeviceId();
        if (!deviceId.equals(readQrcodeIndex())) {
            createQrcode(deviceId);
            return;
        }
        final Bitmap image = getQrcodeImage();
        if (image == null) {
            createQrcode(deviceId);
            return;
        }
        mQrcodeImage.setImageBitmap(image);
    }

    private void writeQrcodeIndex(String index) {
        SPUtils.setParam(this, PREFERENCE_KEY_INDEX, index);
    }

    private String readQrcodeIndex() {
        return (String) SPUtils.getParam(this, PREFERENCE_KEY_INDEX, "");
    }

    /**
     * 创建二维码
     *
     * @param code
     */
    private void createQrcode(String code) {
        if (mQrcodeTask != null) {
            final QrcodeTask oldTask = mQrcodeTask;
            oldTask.cancel(true);
            mQrcodeTask = null;
        }
        mQrcodeTask = new QrcodeTask();
        mQrcodeTask.execute(code);

    }

    private Bitmap getQrcodeImage() {
        File QRFile = new File(SysServices.getStoragePath(), QRCODE_IMAGE_NAME);
        try {
            FileInputStream inputStream = new FileInputStream(QRFile);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("HardwareIds")
    private String getDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "";
        }
        return telephonyManager.getDeviceId();
    }

    private class QrcodeTask extends AsyncTask<String, Void, String> {

        private static final int QR_WIDTH = 400;
        private static final int QR_HEIGHT = 400;

        @Override
        protected String doInBackground(String... params) {
            try {
                Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                BitMatrix bitMatrix = new QRCodeWriter().encode(params[0], BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
                int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
                for (int y = 0; y < QR_HEIGHT; y++) {
                    for (int x = 0; x < QR_WIDTH; x++) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * QR_WIDTH + x] = 0xff000000;
                        } else {
                            pixels[y * QR_WIDTH + x] = 0xffffffff;
                        }
                    }
                }

                Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

                File QRPic = new File(SysServices.getStoragePath(), QRCODE_IMAGE_NAME);
                if (QRPic.exists()) {
                    QRPic.delete();
                }
                FileOutputStream outputStream = new FileOutputStream(QRPic);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.flush();
                outputStream.close();
                return params[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
            } else {
                writeQrcodeIndex(result);
                initQrcode();
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
        }
    }
}

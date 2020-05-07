package com.sczn.wearlauncher.base.util;

import android.os.Environment;
import android.text.TextUtils;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.util.FileHelper;
import com.sczn.wearlauncher.util.ThreadUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * LogFile
 */
public class LogFile {

    public static final String PATH_LOGCAT_NAME = "WTWD_LOG";
    public static String PATH_LOGCAT;
    public static boolean isDebug = true;// 是否需要打印log,默认是

    /**
     * @param msg
     */
    public static void logCatWithTime(String msg) {
        if (isDebug) {
            long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            final String logCat = StringUtils.getJoinString(sdf.format(time), ":::", msg, System.lineSeparator());
            logCat("\n\n" + logCat);
        }
    }

    /**
     * @param msg
     */
    public static void logCatCmdWithThread(String msg) {
        if (isDebug) {
            long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            final String logCat = StringUtils.getJoinString(sdf.format(time), ":::", msg, System.lineSeparator());
            ThreadUtil.getPool().execute(new Runnable() {
                @Override
                public void run() {
                    logCat("\nsend:>>>>>>>>>>>>>>" + logCat);
                }
            });
        }
    }

    /**
     * @param msg
     */
    public static void logCatCmdReceiver(String msg) {
        if (isDebug) {
            long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            final String logCat = StringUtils.getJoinString(sdf.format(time), ":::", msg, System.lineSeparator());
            logCat("\nreceiver:<<<<<<<<<<<<<" + logCat);
        }
    }

    /**
     * 子线程存储数据
     *
     * @param msg
     */
    public static void logCatWithTimeWithThread(String msg) {
        if (isDebug) {
            long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            final String logCat = StringUtils.getJoinString(sdf.format(time), ":::", msg, System.lineSeparator());
            ThreadUtil.getPool().execute(new Runnable() {
                @Override
                public void run() {
                    logCat(logCat);
                }
            });
        }
    }

    /**
     * @param msg
     */
    private static void logCat(String msg) {
        if (isDebug) {
            if (PATH_LOGCAT == null) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    PATH_LOGCAT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PATH_LOGCAT_NAME + File.separator + "Log";
                } else {
                    PATH_LOGCAT = getExternalSdCardPath() + File.separator + PATH_LOGCAT_NAME + File.separator + "Log";
                }
            }
            byte[] logBytes = msg.getBytes();
            File file = new File(PATH_LOGCAT);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream out;
            try {
                final String logName = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                File ssr = new File(PATH_LOGCAT, logName + ".log");
                if (!ssr.exists()) {
                    try {
                        ssr.createNewFile();
                        deleteFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ssr.exists()) {
                    out = new FileOutputStream(ssr, true);
                    out.write(logBytes);
                    out.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                MxyLog.e("logCat", "logCat--" + e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                MxyLog.e("logCat", "logCat--" + e.toString());
            }
        }
    }

    private static void deleteFile() {
        List<File> files = FileHelper.listFileSortByModifyTime(new File(PATH_LOGCAT));
        if (files == null) {
            return;
        }
        int index = files.size() - 7;
        if (index > 0) {
            for (int i = 0; i < index; i++) {
                FileHelper.deleteFile(files.get(i));
            }
        }
    }

    private static String getExternalSdCardPath() {
        String path = null;
        File sdCardFile;
        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }

        return null;
    }

    /**
     * @return
     */
    private static ArrayList<String> getDevMountList() {
        ArrayList<String> out = new ArrayList<String>();
        try {
            String fileInfos = readFile("/etc/vold.fstab");
            if (!TextUtils.isEmpty(fileInfos)) {
                String[] toSearch = fileInfos.split(" ");
                for (int i = 0; i < toSearch.length; i++) {
                    if (toSearch[i].contains("dev_mount")) {
                        if (new File(toSearch[i + 2]).exists()) {
                            out.add(toSearch[i + 2]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private static String readFile(String filePath) {
        StringBuilder fileContent = new StringBuilder();
        File file = new File(filePath);
        if (!file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file));
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(" ");
            }
            reader.close();
            return fileContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileContent.toString();
    }
}

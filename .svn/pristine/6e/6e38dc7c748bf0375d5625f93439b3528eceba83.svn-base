package com.sczn.wearlauncher.util;

import android.os.Environment;

import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.util.StringUtils;
import com.sczn.wearlauncher.socket.command.CommandHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description:文件操作
 * Created by Bingo on 2019/1/14.
 */
public class FileHelper {

    /**
     * 音频文件的路径
     */
    private static final String VOICE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gprs_voice";

    /**
     * 保存下发的音频文件
     *
     * @param bytes
     * @param voiceOutName 为本地生产保存到数据库闹钟的id
     */
    public static void saveVoiceData(byte[] bytes, String voiceOutName) {
        MxyLog.e("saveVoiceData", CommandHelper.getInstance().hexString(bytes));
        try {
            File file = new File(VOICE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (StringUtils.isEmpty(voiceOutName)) {
                MxyLog.w("voice", "voiceOutName is null,error");
                return;
            }
            File tempWav = new File(file, voiceOutName + ".amr");
            if (!tempWav.exists()) {
                tempWav.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(tempWav);
            fos.write(bytes);
            fos.flush();
            fos.close();
            MxyLog.d("voice", "save voice file finish");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.isFile() && file.exists() && file.delete();
    }

    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param file
     * @return
     */
    public static List<File> listFileSortByModifyTime(File file) {
        if (file == null || file.listFiles().length <= 7) {
            return null;
        }
        List<File> list = Arrays.asList(file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".log");
            }
        }));
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return -1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
        return list;
    }

    public static boolean deleteFile(File file) {
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file != null && file.exists() && file.isFile()) {
            if (file.delete()) {
                MxyLog.e("DeleteFileUtil", "删除单个文件成功！" + file.getName());
                return true;
            } else {
                MxyLog.e("DeleteFileUtil", "删除单个文件失败！");
                return false;
            }
        } else {
            MxyLog.e("DeleteFileUtil", "删除单个文件失败：不存在！");
            return false;
        }
    }

    /**
     * File和byte[]转换
     *
     * @param filePath
     * @return
     */
    public static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}

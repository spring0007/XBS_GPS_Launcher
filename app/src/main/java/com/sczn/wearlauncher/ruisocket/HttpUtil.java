package com.sczn.wearlauncher.ruisocket;

import android.os.Environment;
import android.os.Handler;

import com.sczn.wearlauncher.app.MxyLog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 *
 */
public class HttpUtil {
    private Handler handler;

    public HttpUtil(Handler mHandler) {
        handler = mHandler;
    }

    /**
     * 写前准备 1.在AndroidManifest.xml中进行权限配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * 2.取得写入SDCard的权限,取得SDCard的路径:Environment.getExternalStorageDirectory()
     * 3.检查要保存的文件上是否已经存在
     * 4.不存在，新建文件夹，新建文件 5.将input流中的信息写入SDCard 6.关闭流
     *
     * @param urlString
     * @param dirString
     */
    public void httpGet(String urlString, String dirString) {
        // String urlStr="http://116.62.215.244:8090//UploadFiles/Heading/ContactUser/Device_20171026200948627.png";
        OutputStream output = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 取得inputStream，并将流中的信息写入SDCard
            String SDCard = Environment.getExternalStorageDirectory().toString();
            File file = new File(dirString);
            InputStream input = conn.getInputStream();
            if (file.exists()) {
                MxyLog.d("httpGet", "file exits");
            } else {
                String dir = SDCard + "/" + dirString;
                new File(dir).mkdir();// 新建文件夹
                file.createNewFile();// 新建文件
                output = new FileOutputStream(file);
                // 读取大文件
                byte[] buffer = new byte[4 * 1024];
                while (input.read(buffer) != -1) {
                    output.write(buffer);
                }
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                MxyLog.d("httpGet", "success");
            } catch (IOException e) {
                MxyLog.d("httpGet", "fail");
                e.printStackTrace();
            }
        }
    }


    /**
     * @param urlString
     * @param phoneNum
     */
    public void httpGetContactAvatar(String urlString, String phoneNum) {
        // String urlStr="http://116.62.215.244:8090//UploadFiles/Heading/ContactUser/Device_20171026200948627.png";
        OutputStream output = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //设置获取图片的方式为GET
            conn.setRequestMethod("GET");
            // 取得inputStream，并将流中的信息写入SDCard
            String SDCard = Environment.getExternalStorageDirectory().toString();
            File dir1 = new File(Environment.getExternalStorageDirectory().toString() + "/ContactAvatar");
            if (!dir1.exists()) {
                dir1.mkdirs();
            }

            File file = new File(SDCard + "/" + "ContactAvatar" + "/" + phoneNum + ".png");
            InputStream input = conn.getInputStream();
            if (file.exists()) {
                System.out.println("exits");
            } else {
                String dir = SDCard + "/" + "ContactAvatar";
                new File(dir).mkdir();// 新建文件夹
                file.createNewFile();// 新建文件
                output = new FileOutputStream(file);
                //读取大文件
                byte[] buffer = new byte[4 * 1024];
                while (input.read(buffer) != -1) {
                    output.write(buffer);
                }
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                handler.sendEmptyMessage(9);
                MxyLog.d("httpGetContactAvatar", "fail");
            } catch (IOException e) {
                MxyLog.d("httpGetContactAvatar", "fail");
                e.printStackTrace();
            }
        }
    }

    /**
     * 往服务器上上传文本 比如log日志
     *
     * @param urlstr     请求的url
     * @param uploadFile log日志的路径 /mnt/shell/emulated/0/LOG/LOG.log
     * @param newName    log日志的名字 LOG.log
     * @return
     */
    public static void httpPost(String urlstr, String uploadFile, String newName) {
        MxyLog.d("httpPost", "urlstr=" + urlstr + ";uploadFile=" + uploadFile + ";newName=" + newName);
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";// 边界标识
        int TIME_OUT = 10 * 1000; // 超时时间
        HttpURLConnection con = null;
        DataOutputStream ds = null;
        InputStream is = null;
        try {
            URL url = new URL(urlstr);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(TIME_OUT);
            con.setConnectTimeout(TIME_OUT);
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // 设置http连接属性
            con.setRequestMethod("POST");// 请求方式
            con.setRequestProperty("Connection", "Keep-Alive");// 在一次TCP连接中可以持续发送多份数据而不会断开连接
            con.setRequestProperty("Charset", "UTF-8");// 设置编码
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);// multipart/form-data能上传文件的编码格式

            ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data;" + "name=\"stblog\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);

            // 取得文件的FileInputStream
            FileInputStream fStream = new FileInputStream(uploadFile);
            /*设置每次写入1024bytes*/
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /*从文件读取数据至缓冲区*/
            while ((length = fStream.read(buffer)) != -1) {
                /*将资料写入DataOutputStream中*/
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);// 结束

            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            is = con.getInputStream();
            int ch;
            StringBuilder b = new StringBuilder();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 将Response显示于Dialog */
            MxyLog.i("TAG", "上传成功");
        } catch (Exception e) {
            MxyLog.i("TAG", "上传失败" + e);
        } finally {
            /*关闭DataOutputStream*/
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }

    /**
     * android上传文件到服务器
     * <p>
     * 下面为 http post 报文格式
     * <p>
     * POST/logsys/home/uploadIspeedLog!doDefault.html HTTP/1.1 　　 Accept:
     * text/plain, 　　 Accept-Language: zh-cn 　　 Host: 192.168.24.56
     * Content-Type
     * :multipart/form-data;boundary=-----------------------------7db372eb000e2
     * 　　 User-Agent: WinHttpClient 　　 Content-Length: 3693 　　 Connection:
     * Keep-Alive 注：上面为报文头 　　 -------------------------------7db372eb000e2
     * Content-Disposition: form-data; name="file"; filename="kn.jpg"
     * Content-Type: image/jpeg 　　 (此处省略jpeg文件二进制数据...）
     * -------------------------------7db372eb000e2--
     *
     * @param picPaths   需要上传的文件路径集合
     * @param requestURL 请求的url
     * @return 返回响应的内容
     */
    public static Boolean uploadFile(String[] picPaths, String requestURL) {
        String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
        String prefix = "--", end = "\r\n";
        String content_type = "multipart/form-data"; // 内容类型
        String CHARSET = "utf-8"; // 设置编码
        int TIME_OUT = 10 * 10000000; // 超时时间
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", content_type + ";boundary=" + boundary);
            /**
             * 当文件不为空，把文件包装并且上传
             */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(end);
            dos.write(stringBuffer.toString().getBytes());

            String name = "Wearable";
            dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + end);
            dos.writeBytes(end);
            dos.writeBytes("Wearable");
            dos.writeBytes(end);

            for (int i = 0; i < picPaths.length; i++) {
                File file = new File(picPaths[i]);

                StringBuffer sb = new StringBuffer();
                sb.append(prefix);
                sb.append(boundary);
                sb.append(end);

                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"").append(i)
                        .append("\"; filename=\"").append(file.getName()).append("\"").append(end);
                sb.append("Content-Type: application/octet-stream; charset=").append(CHARSET).append(end);
                sb.append(end);
                dos.write(sb.toString().getBytes());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[8192];// 8k
                int len;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(end.getBytes());// 一个文件结束标志
            }
            byte[] end_data = (prefix + boundary + prefix + end).getBytes();// 结束http流
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            MxyLog.e("uploadFile", "response code:" + res);
            if (res == 200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getServerAndPort(String imei) {
        String jsonArrayData = "";
        URL url;
        try {
            url = new URL("http://s1.zjrt9999.com:8090/Common/GetHttpInfor?deviceId=" + imei);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection == null) {
                return null;
            }
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonArrayData += line;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return jsonArrayData;

    }
}

package com.example.zhengyongxiang.inputevent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhengyx
 * @description 图片下载
 * @date 2017/4/13
 */
public class DownLoadPicManager {

    private final static String TAG = "DownLoadPicManager";
    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/download_test/";
    private Bitmap mBitmap;
    private String mFileName;
    private Context context;


    protected void onCreate(Context context) {
        this.context = context;
        new Thread(connectNet).start();

    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * Get data from stream
     *
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public File saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }


    /*
     * 连接网络
     * 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
     */
    private Runnable connectNet = new Runnable() {
        @Override
        public void run() {
            try {
                String filePath = "http://img.my.csdn.net/uploads/201211/21/1353511891_4579.jpg";
                mFileName = "test.jpg";

                byte[] data = getImage(filePath);
                if (data != null) {
                    mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
                }
                String mSaveMessage;
                File file = null;
                try {
                    file = saveFile(mBitmap, mFileName);
                    mSaveMessage = "图片保存成功！";
                } catch (IOException e) {
                    mSaveMessage = "图片保存失败！";
                    e.printStackTrace();
                }
                Log.d(TAG, "save image ..." + mSaveMessage);
                // 其次把文件插入到系统图库
                String path = file.getAbsolutePath();
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            path, mFileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };


}
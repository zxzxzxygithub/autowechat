package com.example.zhengyongxiang.inputevent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;

/**
 * Created by zhengyongxiang on 2017/4/13.
 */

public class Utils {
    /**
     * @description 把图片加入到系统图库
     * @author zhengyx
     * @date 2017/4/13
     */
    public static void addToSysPicGallery(Context context, String path, String fileName) {
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    path, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
}

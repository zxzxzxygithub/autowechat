package com.example.zhengyongxiang.inputevent;

import android.app.Application;

import com.yuan.library.dmanager.download.DownloadManager;

import cn.jpush.android.api.JPushInterface;

/**
 * @author zhengyx
 * @description application类，初始化jpush
 * @date 2017/4/13
 */
public class MyApplication extends Application {
    public static final String KEY_PUSHSTR = "key_pushstr";
    public static final String ACTION_PUSH = "com.example.zhengyx.input";
    public static final int REQUEST_CODE_LOCKSCREE = 9999;
    public static final String KEY_LOCKSCREEN = "lockscreen";

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        DownloadManager.getInstance().init(this, 9);
    }


}

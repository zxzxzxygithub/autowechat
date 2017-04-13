package com.example.zhengyongxiang.inputevent;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * @author zhengyx
 * @description application类，初始化jpush
 * @date 2017/4/13
 */
public class MyApplication extends Application {
    public static final String KEY_PUSHSTR = "key_pushstr";
    public static final String ACTION_PUSH= "com.example.zhengyx.input";
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
















}

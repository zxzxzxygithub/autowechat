package com.example.zhengyongxiang.inputevent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

/**
 * Created by zhengyongxiang on 2017/4/14.
 */

public class UnLockScreenActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        registerReceiver(finishReceiver,new IntentFilter(MyApplication.ACTION_FINISH));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(finishReceiver);
    }

    BroadcastReceiver finishReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              finish();
        }
    };
}

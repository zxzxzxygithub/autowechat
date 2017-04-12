package com.example.zhengyongxiang.inputevent;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        ComponentName cn = activityManager.getRunningTasks(Integer.MAX_VALUE).get(0).topActivity;
        String className = cn.getClassName();
        Log.d("topclass", "className: "+className);
        return super.onStartCommand(intent, flags, startId);
    }
}

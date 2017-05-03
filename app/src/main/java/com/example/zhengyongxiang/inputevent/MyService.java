package com.example.zhengyongxiang.inputevent;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yuan.library.dmanager.download.DownloadManager;
import com.yuan.library.dmanager.download.DownloadTask;
import com.yuan.library.dmanager.download.DownloadTaskListener;
import com.yuan.library.dmanager.download.TaskEntity;

import java.util.List;

/**
 * @author zhengyx
 * @description 设置为前台广播，同时接收推送receiver进行命令处理
 * @date 2017/5/3
 */
public class MyService extends Service {


    private DownloadManager mDownloadManager;
    private int finishedCount;
    private boolean isScreenOn = true;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = DownloadManager.getInstance();
//    注册屏幕点亮关闭广播
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启用前台服务，主要是startForeground()
        Notification.Builder builder = new Notification.Builder(this);
        Notification notification = builder.build();
        Intent mIntent = new Intent(this, MyService.class);
        int requestCode = 111;
        PendingIntent pendingIntent = PendingIntent.getService(this, requestCode, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle("加油123");
        //设置通知默认效果
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        int id = 123;
        startForeground(id, notification);
//       取消通知
        startService(new Intent(this, AssistService.class));
        if (intent != null) {
            boolean doOrder = intent.getBooleanExtra(MyApplication.KEY_DOORDER, false);
            String pushStr = intent.getStringExtra(MyApplication.KEY_PUSHSTR);
            Context context = this;
            if (doOrder) {
                doOrder(context, pushStr);
            }
        }
        return super.onStartCommand(intent,
                Service.START_FLAG_REDELIVERY, startId);
    }


    /**
     * @description 屏幕亮灭广播
     * @author zhengyx
     * @date 2017/4/14
     */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                isScreenOn = true;
                Log.d("lyj", "-----------------screen is on...");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                isScreenOn = false;
                Log.d("lyj", "----------------- screen is off...");

            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mBatInfoReceiver != null) {
            unregisterReceiver(mBatInfoReceiver);
            mBatInfoReceiver = null;
        }
    }

    /**
     * @description 执行命令
     * @author zhengyx
     * @date 2017/4/14
     */
    private void doOrder(Context context, String pushStr) {
        //           屏幕熄灭了，先解锁并亮屏
        if (!isScreenOn) {
            Utils.wakeUpAndUnlock(context);
            Intent intent = new Intent(this, UnLockScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        Gson gson = new Gson();
        try {
            final MockManager manager = new MockManager();
            ConfigBean configBean = gson.fromJson(pushStr, ConfigBean.class);
            final String text = configBean.getText();
            final boolean hasPic = configBean.isHasPic();
            final List<String> urls = configBean.getUrls();
            String type = "图文朋友圈";
            if (!hasPic) {
                type = "纯文本朋友圈";
            }
            final AlertDialog alertDialog = new AlertDialog.Builder(MyService.this, R.style.Theme_AppCompat_Light).setMessage("收到服务端通知，马上进行朋友圈自动发送" + "\n"
                    + "发送形式: " + type + "\n"
                    + "发送内容: " + text
            ).create();
            Window window = alertDialog.getWindow();
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            if (!hasPic && !TextUtils.isEmpty(text)) {
                                manager.sendTextOnly(getApplicationContext(), text);
                            } else if (hasPic && urls != null && urls.size() > 0) {
                                int position = 0;
                                final int totalSize = urls.size();
                                for (String url :
                                        urls) {
                                    position++;
                                    final int myposition = position;
                                    DownloadTask itemTask = new DownloadTask(new TaskEntity.Builder().url(url).build());
                                    itemTask.setListener(new DownloadTaskListener() {
                                        @Override
                                        public void onQueue(DownloadTask downloadTask) {
                                            Log.d("downloadtag", "onQueue+" + myposition);
                                        }

                                        @Override
                                        public void onConnecting(DownloadTask downloadTask) {

                                        }

                                        @Override
                                        public void onStart(DownloadTask downloadTask) {
                                            TaskEntity taskEntity = downloadTask.getTaskEntity();
                                            taskEntity.getTaskStatus();
                                            String fileName = taskEntity.getFileName();
                                            Log.d("downloadtag", "onStart+" + myposition + "_fileName_" + fileName);
                                        }

                                        @Override
                                        public void onPause(DownloadTask downloadTask) {

                                        }

                                        @Override
                                        public void onCancel(DownloadTask downloadTask) {

                                        }

                                        @Override
                                        public void onFinish(DownloadTask downloadTask) {
//                                            mDownloadManager.cancelTask(downloadTask);
                                            finishedCount++;
                                            String path = downloadTask.getTaskEntity().getFilePath();
                                            String fileName = downloadTask.getTaskEntity().getFileName();
                                            Utils.addToSysPicGallery(getApplicationContext(), path, fileName);

                                            if (finishedCount == totalSize) {
                                                Toast.makeText(getApplicationContext(), "所有图片下载完毕", Toast.LENGTH_SHORT).show();
                                                new MockManager().sendPicAndText(getApplicationContext(), text, totalSize);
                                                finishedCount = 0;
                                            }
                                            Log.d("downloadtag", "downloadfinish+" + myposition);
                                        }

                                        @Override
                                        public void onError(DownloadTask downloadTask, int code) {
                                            Log.d("downloadtag", "onError+code_" + code);

                                        }
                                    });
                                    TaskEntity taskEntity = itemTask.getTaskEntity();
                                    taskEntity.setFilePath(DownLoadPicManager.ALBUM_PATH);
                                    mDownloadManager.addTask(itemTask);
                                }

                            }
                        }
                    });

                }
            }).start();

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}

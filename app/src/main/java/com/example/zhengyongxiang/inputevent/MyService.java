package com.example.zhengyongxiang.inputevent;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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

public class MyService extends Service {

    private DownloadManager mDownloadManager;
    private int finishedCount;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = DownloadManager.getInstance();
        registerReceiver(mReceivePush, new IntentFilter(MyApplication.ACTION_PUSH));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    BroadcastReceiver mReceivePush = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pushStr = intent.getStringExtra(MyApplication.KEY_PUSHSTR);
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
                final AlertDialog alertDialog = new AlertDialog.Builder(MyService.this).setMessage("收到服务端通知，马上进行朋友圈自动发送" + "\n"
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
                                                mDownloadManager.cancelTask(downloadTask);
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
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceivePush != null) {
            unregisterReceiver(mReceivePush);
            mReceivePush = null;
        }
    }
}

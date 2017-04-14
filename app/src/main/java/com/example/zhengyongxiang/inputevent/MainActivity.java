package com.example.zhengyongxiang.inputevent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yuan.library.dmanager.download.DownloadManager;
import com.yuan.library.dmanager.download.DownloadTask;
import com.yuan.library.dmanager.download.DownloadTaskListener;
import com.yuan.library.dmanager.download.TaskEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private String detaiText = "我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。";
    private DownloadManager mDownloadManager;
    private int finishedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownloadManager = DownloadManager.getInstance();
        registerReceiver(mReceivePush, new IntentFilter(MyApplication.ACTION_PUSH));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 1;
                new MockManager().sendPicAndText(MainActivity.this, "好", count);
            }
        });
    }

    private void downLoadPic() {
        new DownLoadPicManager().onCreate(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setMessage("收到服务端通知，马上进行朋友圈自动发送" + "\n"
                        + "发送形式: " + type + "\n"
                        + "发送内容: " + text
                ).create();
                alertDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                if (!hasPic && !TextUtils.isEmpty(text)) {
                                    manager.sendTextOnly(MainActivity.this, text);
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
                                                int taskStatus = taskEntity.getTaskStatus();
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
                                                Utils.addToSysPicGallery(MainActivity.this, path, fileName);

                                                if (finishedCount == totalSize) {
                                                    Toast.makeText(MainActivity.this, "所有图片下载完毕", Toast.LENGTH_SHORT).show();
                                                    new MockManager().sendPicAndText(MainActivity.this, text,totalSize);
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


}

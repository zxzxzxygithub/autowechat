package com.example.zhengyongxiang.inputevent;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("first","你好，新的一天"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "朋友圈自动发送开始", Toast.LENGTH_SHORT).show();
//                0.先获取root权限
                final RootShellCmd rootShellCmd = new RootShellCmd();
                //1.打开微信
                Intent intent = new Intent();
//                com.tencent.mm/com.tencent.mm.plugin.nfc_open.ui.NfcWebViewUI
                intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
//                intent.putExtra("Kdescription","加油！！！");
//                intent.putExtra("WechatForwarderText",true);
//                intent.putExtra("Ksnsupload_type",9);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                2.点击发现：
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        发现
                        rootShellCmd.exec("input tap 465  1188 " + "\n");
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        朋友圈
                        rootShellCmd.exec("input tap 507  225 " + "\n");
                        try {
                            Thread.sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        相机按钮
                        rootShellCmd.exec("input tap 662  79 " + "\n");
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        4.点击从相册选择
                        rootShellCmd.exec("input tap 431  717 " + "\n");
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        5. 选择第一张图片
                        rootShellCmd.exec("input tap 218  189 " + "\n");
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        6. 点击完成按钮
                        rootShellCmd.exec("input tap 637  110 " + "\n");
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        7.  input text  light
//                        rootShellCmd.exec("input text  '你好，新的一天！'  " + "\n");
                        rootShellCmd.exec("am broadcast -a ADB_INPUT_TEXT --es msg '我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。' " + "\n");

                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        7.  发送（和相机的坐标一致）
                        rootShellCmd.exec("input tap 662  79  " + "\n");
                        try {
                            Thread.sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        7.  返回
                        rootShellCmd.simulateKey(KeyEvent.KEYCODE_BACK);
                        try {
                            Thread.sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        8.  返回
                        rootShellCmd.simulateKey(KeyEvent.KEYCODE_BACK);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "朋友圈自动发送结束!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();


//                Intent intent = new Intent(MainActivity.this, MyService.class);
//                intent.setAction("com.example.zhengyongxiang.inputevent.repeating");
//                PendingIntent sender = PendingIntent
//                        .getService(MainActivity.this, 0, intent, 0);
////开始时间
//                long firstime = SystemClock.elapsedRealtime();
//
//                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
////5秒一个周期，不停的发送广播
//                am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 1 * 1000, sender);

            }
        });
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
}

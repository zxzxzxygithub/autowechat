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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MainActivity extends AppCompatActivity {


    private String detaiText = "我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(mReceivePush, new IntentFilter(MyApplication.ACTION_PUSH));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        downLoadPic();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MockManager().sendTextOnly(MainActivity.this
                        , "想要吃好吃哒");
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
                MockManager manager = new MockManager();
                ConfigBean configBean = gson.fromJson(pushStr, ConfigBean.class);
                String text = configBean.getText();
                boolean hasPic = configBean.isHasPic();
                String type = "图文朋友圈";
                if (!hasPic) {
                    type = "纯文本";
                }
                new AlertDialog.Builder(MainActivity.this).setMessage("收到服务端通知，马上进行朋友圈自动发送" + "\n"
                        + "发送形式：" + type + "\n"
                        + "发送内容" + text
                ).create().show();
                if (!hasPic && !TextUtils.isEmpty(text)) {
                    manager.sendTextOnly(MainActivity.this, text);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        }
    };
}

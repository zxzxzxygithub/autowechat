package com.example.zhengyongxiang.inputevent;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * @author zhengyx
 * @description 入口activity
 * @date 2017/4/14
 */
public class MainActivity extends AppCompatActivity {


    private String detaiText = "我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。";

    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //启动后台服务
        startService(new Intent(this, MyService.class));
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int count = 1;
//                new MockManager().sendPicAndText(MainActivity.this, "好", count);
                Utils.openWeChat(MainActivity.this);
            }
        });
        // 获取设备管理服务
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // ComponentName这个我们在用intent跳转的时候用到过。
        // 自己的AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(this, AdminReceiver.class);
        /*
         * 假如先判断是否有权限，如果没有则调用activeManage()，然后立即锁屏，最后再finish()。
         * 这样做是有问题的，因为activeManage()可能还在等待另一个Activity的结果，那么此时依然没有权限却
         * 执行了lockNow()，这样就出错了。 处理方法有2个：
         * 1、是重写OnActivityResult()函数，在里面判断是否获取权限成功，是则锁屏并finish()
         * 否则继续调用activeManage()获取权限（这样激活后立即锁屏，效果很好）
         * 2、不重写OnActivityResult()函数，第一次获取权限后不锁屏而立即finish()，这样从理论上说也可能
         * 失败，可能权限还没获取好就finish了（这样激活后就回到桌面，还得再按一次锁屏才能锁） 综上推荐第一种方法。
         */

        // 判断是否有锁屏权限，若有则立即锁屏并结束自己，若没有则获取权限
        if (!policyManager.isAdminActive(componentName)) {
            Utils.activeManage(this, componentName);//获取权限
        } else {
            Intent intent = getIntent();
            boolean lockscreen = intent.getBooleanExtra(MyApplication.KEY_LOCKSCREEN, false);
            if (lockscreen) {
                policyManager.lockNow();// 锁屏
                finish();
            }

        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取权限成功，立即锁屏并finish自己，否则继续获取权限
        if (requestCode == MyApplication.REQUEST_CODE_LOCKSCREE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "已获取锁屏权限", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}

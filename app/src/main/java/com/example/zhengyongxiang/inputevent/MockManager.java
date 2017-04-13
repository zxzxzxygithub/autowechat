package com.example.zhengyongxiang.inputevent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by zhengyongxiang on 2017/4/13.
 */

public class MockManager {

    private String detaiText = "我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。";

    /**
     * @description 只发文字
     * @author zhengyx
     * @date 2017/4/13
     */
    public void sendTextOnly(final Activity context, final String detaiText) {
        Toast.makeText(context, "朋友圈自动发送开始", Toast.LENGTH_SHORT).show();
//                0.先获取root权限
        final RootShellCmd rootShellCmd = new RootShellCmd();
        //1.打开微信
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
//                        长按相机按钮
                rootShellCmd.exec("input swipe 662 79  662 79 800 " + "\n");
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                        7.  input text  light
//                        rootShellCmd.exec("input text  '你好，新的一天！'  " + "\n");
                rootShellCmd.exec("am broadcast -a ADB_INPUT_TEXT --es msg '" +
                        detaiText +
                        "' " + "\n");

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
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "朋友圈自动发送结束!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    /**
     * @description 发送带图片的朋友圈
     * @author zhengyx
     * @date 2017/4/13
     */
    public void sendPicAndText(final Activity context, final String detaiText) {
        Toast.makeText(context, "朋友圈自动发送开始", Toast.LENGTH_SHORT).show();
//                0.先获取root权限
        final RootShellCmd rootShellCmd = new RootShellCmd();
        //1.打开微信
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
                rootShellCmd.exec("am broadcast -a ADB_INPUT_TEXT --es msg '" +
                        detaiText +
                        "' " + "\n");

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
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "朋友圈自动发送结束!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

}

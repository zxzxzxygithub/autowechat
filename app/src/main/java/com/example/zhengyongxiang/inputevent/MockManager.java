package com.example.zhengyongxiang.inputevent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author zhengyx
 * @description tcl p590l 模拟点击微信
 * @date 2017/4/14
 */
public class MockManager {

    private static String pic1 = " 218 189 ";//第1张图片点击坐标
    private static String pic2 = " 443 179  ";//第2张图片点击坐标
    private static String pic3 = " 701 163 ";//第3张图片点击坐标
    private static String pic4 = " 220 401 ";//第4张图片点击坐标
    private static String pic5 = " 443 409 ";//第5张图片点击坐标
    private static String pic6 = " 664 410 ";//第6张图片点击坐标
    private static String pic7 = " 202 666 ";//第7张图片点击坐标
    private static String pic8 = " 431 663 ";//第8张图片点击坐标
    private static String pic9 = " 669 656 ";//第9张图片点击坐标

    private static String textEditPoint = " 236 223 ";//朋友圈文本编辑点击坐标

    private static ArrayList<String> list = new ArrayList<>();

    static {
        list.clear();
        list.add(pic1);
        list.add(pic2);
        list.add(pic3);
        list.add(pic4);
        list.add(pic5);
        list.add(pic6);
        list.add(pic7);
        list.add(pic8);
        list.add(pic9);
    }

    /**
     * @description 只发文字
     * @author zhengyx
     * @date 2017/4/13
     */
    public void sendTextOnly(final Context context, final String detaiText) {
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
                performClick(rootShellCmd, "input tap 465  1188 " + "\n", 800);
//                        朋友圈
                performClick(rootShellCmd, "input tap 507  225 " + "\n", 1800);
//                        长按相机按钮
                performClick(rootShellCmd, "input swipe 662 79  662 79 800 " + "\n", 800);
//                        7.  点击文本编辑框，防止adbkeyboard没有生效
                performClick(rootShellCmd, "input tap " +
                        textEditPoint  + "\n", 800);
//                8.  输入文本
                performClick(rootShellCmd, "am broadcast -a ADB_INPUT_TEXT --es msg '" +
                        detaiText +
                        "' " + "\n", 800);
//                        7.  发送（和相机的坐标一致）
                performClick(rootShellCmd, "input tap 662  79  " + "\n", 1800);
//                        7.  返回
                clickBack(rootShellCmd);
//                        8.  返回
                clickBack(rootShellCmd);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
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
    public void sendPicAndText(final Context context, final String detaiText, final int count) {
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
                performClick(rootShellCmd, "input tap 465  1188 " + "\n", 800);
//                        朋友圈
                performClick(rootShellCmd, "input tap 507  225 " + "\n", 1800);
//                        相机按钮
                performClick(rootShellCmd, "input tap 662  79 " + "\n", 1800);
//                        4.点击从相册选择
                performClick(rootShellCmd, "input tap 431  717 " + "\n", 800);

//                        5.点击相册图片
                clickPic(rootShellCmd, count);

////                        5. 选择第一张图片
//                performClick(rootShellCmd, "input tap 218  189 " + "\n", 800);


//                        6. 点击完成按钮
                performClick(rootShellCmd, "input tap 637  110 " + "\n", 1800);
 //                        7.  点击文本编辑框，防止adbkeyboard没有生效
                performClick(rootShellCmd, "input tap " +
                        textEditPoint  + "\n", 800);
//                        8.  输入文本
                performClick(rootShellCmd, "am broadcast -a ADB_INPUT_TEXT --es msg '" +
                        detaiText +
                        "' " + "\n", 800);
//                        7.  发送（和相机的坐标一致）
                performClick(rootShellCmd, "input tap 662  79  " + "\n", 1800);
//                        7.  返回
                clickBack(rootShellCmd);
//                        8.  返回
                clickBack(rootShellCmd);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "朋友圈自动发送结束!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    /**
     * @description 点击返回
     * @author zhengyx
     * @date 2017/4/14
     */
    private void clickBack(RootShellCmd rootShellCmd) {
        rootShellCmd.simulateKey(KeyEvent.KEYCODE_BACK);
        try {
            Thread.sleep(1800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 模拟点击
     * @author zhengyx
     * @date 2017/4/14
     */
    private void performClick(RootShellCmd rootShellCmd, String cmd, int millis) {
        rootShellCmd.exec(cmd);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param count 点击多少张图片
     * @description 模拟点击朋友圈发图片
     * @author zhengyx
     * @date 2017/4/14
     */
    private void clickPic(RootShellCmd rootShellCmd, int count) {
        if (count > 0 || count < 10) {
            int innerCount = 0;
            for (String point : list) {
                innerCount++;
                rootShellCmd.exec("input tap " +
                        point + "\n");
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (innerCount == count) {
                    break;
                }
            }
        }
    }

}

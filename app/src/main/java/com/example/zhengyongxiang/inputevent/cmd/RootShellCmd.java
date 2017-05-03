package com.example.zhengyongxiang.inputevent.cmd;

import android.text.TextUtils;

import java.io.OutputStream;

/**
 * @author zhengyx
 * @description 命令行代码
 * @date 2017/5/3
 */
public class RootShellCmd {

    private OutputStream os;

    /**
     * 执行shell指令
     *
     * @param cmd 指令
     */
    public final void exec(String cmd) {
        try {
            if (os == null) {
                os = Runtime.getRuntime().exec("su").getOutputStream();
            }
            if (!TextUtils.isEmpty(cmd)) {
                os.write(cmd.getBytes());
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 后台模拟全局按键
     *
     * @param keyCode 键值
     */
    public final void simulateKey(int keyCode) {
        exec("input keyevent " + keyCode + "\n");
    }
}
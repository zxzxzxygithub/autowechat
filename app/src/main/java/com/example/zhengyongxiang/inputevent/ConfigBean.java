package com.example.zhengyongxiang.inputevent;

import java.util.List;

/**
 * @author zhengyx
 * @description 配置的bean
 * @date 2017/4/13
 */
public class ConfigBean {

    private boolean hasPic;
    private String text;
    private List<String> urls;

    public boolean isHasPic() {
        return hasPic;
    }

    public void setHasPic(boolean hasPic) {
        this.hasPic = hasPic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}

package com.ljkj.lib_common.bean;

/**
 * 作者: fzy
 * 日期: 2024/9/25
 * 描述:
 */
public class WarnDataBean {
    private String text;
    private long time;

    public WarnDataBean(String text, long time) {
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

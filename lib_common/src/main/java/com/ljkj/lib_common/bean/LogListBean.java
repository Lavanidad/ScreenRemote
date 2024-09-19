package com.ljkj.lib_common.bean;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class LogListBean {

    public String name = "";

    public boolean isSelect = false;

    public String path = "";

    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "LogListBean{" +
                "name='" + name + '\'' +
                ", isSelect=" + isSelect +
                ", path='" + path + '\'' +
                ", type=" + type +
                '}';
    }
}

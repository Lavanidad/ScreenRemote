package com.ljkj.lib_common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class LogBean {
    @SerializedName("file_name")
    private String fileName;
    @SerializedName("path")
    private String path;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "LogBean{" +
                "fileName='" + fileName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

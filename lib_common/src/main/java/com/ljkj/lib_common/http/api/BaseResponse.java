package com.ljkj.lib_common.http.api;

import androidx.annotation.NonNull;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class BaseResponse<T>{

    private int err_code;
    private String err_msg;
    private T err_data;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public T getErr_data() {
        return err_data;
    }

    public void setErr_data(T err_data) {
        this.err_data = err_data;
    }

    @NonNull
    @Override
    public String toString() {
        return "ApiResponse{" +
                "err_code=" + err_code +
                ", err_msg='" + err_msg + '\'' +
                ", err_data=" + err_data +
                '}';
    }
}

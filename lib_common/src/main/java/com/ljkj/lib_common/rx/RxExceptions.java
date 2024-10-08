package com.ljkj.lib_common.rx;

import com.google.gson.JsonIOException;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class RxExceptions {

    public static String exceptionHandler(Throwable throwable) {
        String errorMsg = "未知异常" + throwable.getMessage();
        if (throwable instanceof UnknownHostException) {
            errorMsg = "网络不可用";
        } else if (throwable instanceof SocketTimeoutException) {
            errorMsg = "网络连接超时";
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            errorMsg = convertStatusCode(httpException);
        } else if (throwable instanceof ParseException || throwable instanceof JSONException
                || throwable instanceof JsonIOException) {
            errorMsg = "数据解析错误";
        }
        return errorMsg;
    }

    /**
     * 网络异常类型处理类
     *
     * @param httpException
     * @return
     */
    private static String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "服务器处理请求错误," + httpException.code();
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg = "服务器无法处理请求," + httpException.code();
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}

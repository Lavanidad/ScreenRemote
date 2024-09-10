package com.ljkj.lib_common.http.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "HttpRequest";

    private final HttpLoggingInterceptor httpLoggingInterceptor;

    public LoggingInterceptor() {
        httpLoggingInterceptor = new HttpLoggingInterceptor(this::logLongMessage);
        /*
         * NONE: 不打印日志。
         * BASIC: 仅打印请求方法、URL、响应码等基本信息。
         * HEADERS: 打印请求和响应的所有头信息。
         * BODY: 打印所有请求和响应体信息（最详细的级别）。
         */
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startNs = System.nanoTime();

        Response response = httpLoggingInterceptor.intercept(chain);

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        logLongMessage(String.format("Request to %s took %.1fms", request.url(), (double) tookMs));

        return response;
    }

    /**
     * logcat 单条日志的限制是约4000字符
     * @param message
     */
    private void logLongMessage(String message) {
        final int MAX_LOG_LENGTH = 4000;

        if (message.length() <= MAX_LOG_LENGTH) {
            Log.d(TAG, message);
        } else {
            int length = message.length();
            for (int i = 0; i < length; i += MAX_LOG_LENGTH) {
                int end = Math.min(length, i + MAX_LOG_LENGTH);
                Log.d(TAG, message.substring(i, end));
            }
        }
    }
}
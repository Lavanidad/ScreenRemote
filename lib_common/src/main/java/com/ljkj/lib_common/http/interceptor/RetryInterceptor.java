package com.ljkj.lib_common.http.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class RetryInterceptor implements Interceptor {

    private static final String TAG = "RetryInterceptor";

    private final int maxRetry;
    private final int retryTime;

    public RetryInterceptor(int maxRetry, int retryTime) {
        this.maxRetry = maxRetry;
        this.retryTime = retryTime;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        int tryCount = 0;
        Response response = null;

        while (tryCount < maxRetry) {
            try {
                // 尝试执行请求
                response = chain.proceed(request);
                // 如果响应成功，直接返回响应
                if (response.isSuccessful()) {
                    return response;
                } else {
                    int responseCode = response.code();
                    if (responseCode >= 400 && responseCode < 500) {
                        Log.d(TAG, "Client error, not retrying. Response code: " + responseCode);
                        return response;
                    }
                    Log.d(TAG, "Request failed with code: " + response.code());
                }
            } catch (Exception e) {
                Log.e(TAG, "Request failed", e);
                if (tryCount >= maxRetry - 1) {
                    // 到达最大重试次数时，抛出异常
                    throw new IOException("Max retry attempts reached", e);
                }
            }

            // 增加重试计数并记录重试日志
            tryCount++;
            Log.d(TAG, "Retrying request, attempt: " + tryCount);

            // 重试前等待一段时间
            try {
                Thread.sleep(retryTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        if (response != null) {
            Log.e(TAG, "Request failed after " + maxRetry + " attempts, returning last response.");
            return response;  // 返回最后一次的失败响应
        } else {
            throw new IOException("Request failed after " + maxRetry + " attempts");
        }
    }
}



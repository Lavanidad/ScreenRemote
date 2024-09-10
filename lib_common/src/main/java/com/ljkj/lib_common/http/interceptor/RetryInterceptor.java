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
                response = chain.proceed(request);
                if (response.isSuccessful()) {
                    return response;
                } else {
                    Log.d(TAG, "Request failed with code: " + response.code());
                }
            } catch (Exception e) {
                Log.e(TAG, "Request failed", e);
            }
            tryCount++;
            Log.d(TAG, "Retrying request, attempt: " + tryCount);
            try {
                Thread.sleep(retryTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Log.e(TAG, "Request failed after " + maxRetry + " attempts");
        return response;
    }
}


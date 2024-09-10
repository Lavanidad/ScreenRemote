package com.ljkj.lib_common.http.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class CacheInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Request request = chain.request();

        CacheControl cacheControl = new CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS) // 缓存过期时间
                .build();
        request = request.newBuilder()
                .cacheControl(cacheControl)
                .build();
        Response response = chain.proceed(request);

        return response.newBuilder()
                .header("Cache-Control", "public, max-stale=" + 3 * 24 * 60 * 60)
                .build();
    }
}

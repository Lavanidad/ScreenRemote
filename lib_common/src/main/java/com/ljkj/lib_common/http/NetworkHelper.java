package com.ljkj.lib_common.http;

import android.content.Context;

import com.ljkj.lib_common.common.Constants;
import com.ljkj.lib_common.http.api.ApiService;
import com.ljkj.lib_common.http.interceptor.CacheInterceptor;
import com.ljkj.lib_common.http.interceptor.CookieInterceptor;
import com.ljkj.lib_common.http.interceptor.HeaderInterceptor;
import com.ljkj.lib_common.http.interceptor.LoggingInterceptor;
import com.ljkj.lib_common.http.interceptor.RetryInterceptor;
;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class NetworkHelper {

    private static final String TAG = NetworkHelper.class.getSimpleName();
    private final ApiService apiService;
    private final Context context;

    @Inject
    public NetworkHelper(Context context) {
        this.context = context;
        Retrofit retrofit = createRetrofit();
        apiService = retrofit.create(ApiService.class);
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    public ApiService getApiService() {
        return apiService;
    }


    private OkHttpClient getOkHttpClient() {

        File cacheDir = new File(context.getCacheDir(), "http_cache");
        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(new CookieInterceptor())
                .addNetworkInterceptor(new RetryInterceptor(3, 1000))
                .build();
    }
}



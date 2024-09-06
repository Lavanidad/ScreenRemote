package com.ljkj.lib_common.helper;

import com.ljkj.lib_common.common.CommonConstants;
import com.ljkj.lib_common.http.api.ApiService;
;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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

    @Inject
    public NetworkHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        return clientBuilder.build();
    }
}



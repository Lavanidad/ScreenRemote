package com.ljkj.screenremote.di.module;

import com.ljkj.lib_common.helper.HttpHelper;
import com.ljkj.lib_common.helper.NetworkHelper;
import com.ljkj.lib_common.http.HttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    public NetworkHelper providerNetworkHelper(){
        return new NetworkHelper();
    }

    @Provides
    @Singleton
    public HttpHelper provideHttpHelper(NetworkHelper networkHelper) {
        return new HttpHelper(networkHelper);
    }

    @Provides
    @Singleton
    public HttpClient provideHttpClient(HttpHelper httpHelper) {
        return new HttpClient(httpHelper);
    }

}

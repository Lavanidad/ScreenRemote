package com.ljkj.lib_common.helper;


import com.ljkj.lib_common.http.api.ApiService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class HttpHelper implements IHttpHelper {

    public NetworkHelper mNetworkHelper;

    private ApiService getApiService() {
        return mNetworkHelper.getApiService();
    }

    @Inject
    public HttpHelper(NetworkHelper networkHelper) {
        this.mNetworkHelper = networkHelper;
    }

    public Observable<String> getTest() {
        return getApiService().getTest();
    }


    public Observable<String> postTest(String phone, String pwd, String repwd) {
        return getApiService().postTest(phone, pwd, repwd);
    }
}

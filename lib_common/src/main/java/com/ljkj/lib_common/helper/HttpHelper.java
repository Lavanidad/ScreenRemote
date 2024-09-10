package com.ljkj.lib_common.helper;


import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.bean.TestBean;
import com.ljkj.lib_common.http.api.ApiResponse;
import com.ljkj.lib_common.http.api.ApiService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class HttpHelper {

    private final NetworkHelper mNetworkHelper;

    @Inject
    public HttpHelper(NetworkHelper networkHelper) {
        this.mNetworkHelper = networkHelper;
    }

    private ApiService getApiService() {
        return mNetworkHelper.getApiService();
    }

    public Observable<TestBean> getTest() {
        return getApiService().getTest();
    }


    public Observable<String> postTest(String phone, String pwd, String repwd) {
        return getApiService().postTest(phone, pwd, repwd);
    }

    public Observable<ApiResponse<SharingPathListBean>> postTest2(String page_index, String page_size, String lat, String lng, int path_type) {
        return getApiService().postTest2(page_index, page_size, lat, lng, path_type);
    }
}

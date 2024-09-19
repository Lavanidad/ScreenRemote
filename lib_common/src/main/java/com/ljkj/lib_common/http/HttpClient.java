package com.ljkj.lib_common.http;

import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.bean.TestBean;
import com.ljkj.lib_common.helper.HttpHelper;
import com.ljkj.lib_common.http.api.BaseResponse;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;


/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class HttpClient {

    private final HttpHelper httpHelper;

    @Inject
    public HttpClient(HttpHelper helper) {
        this.httpHelper = helper;
    }


    public Observable<TestBean> getTest() {
        return httpHelper.getTest();
    }

    public Observable<String> postTest(String phone, String pwd, String repwd) {
        return httpHelper.postTest(phone, pwd, repwd);
    }

    public Observable<BaseResponse<SharingPathListBean>> postTest2(String page_index, String page_size, String lat, String lng, int path_type) {
        return httpHelper.postTest2(page_index, page_size, lat, lng, path_type);
    }

    public Observable<BaseResponse<LogBean>> uploadLog(String sn, String logType, File file, String fileName, String jsonString) {
        return httpHelper.uploadLog(sn, logType, file, fileName, jsonString);
    }
}
package com.ljkj.lib_common.http;


import com.ljkj.lib_common.bean.AppVersionBean;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.bean.TestBean;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.http.api.ApiService;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public Observable<BaseResponse<SharingPathListBean>> postTest2(String page_index, String page_size, String lat, String lng, int path_type) {
        return getApiService().postTest2(page_index, page_size, lat, lng, path_type);
    }

    public Observable<BaseResponse<LogBean>> uploadLog(String sn, String logType, File file, String fileName, String jsonString) {
        RequestBody snBody = RequestBody.create(sn, MediaType.parse("text/plain"));
        RequestBody logTypeBody = RequestBody.create(logType, MediaType.parse("text/plain"));
        RequestBody paramsBody = RequestBody.create(jsonString, MediaType.parse("text/plain"));
        RequestBody fileNameBody = RequestBody.create(fileName, MediaType.parse("text/plain"));

        RequestBody fileBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fileName, fileBody);

        return getApiService().uploadLog(snBody, logTypeBody, paramsBody, filePart, fileNameBody);
    }

    public Observable<BaseResponse<AppVersionBean>> checkAppVersion(String moduleName, String deviceType) {
        return getApiService().checkAppVersion(moduleName, deviceType);
    }
}

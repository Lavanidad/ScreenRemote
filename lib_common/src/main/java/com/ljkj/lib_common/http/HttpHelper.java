package com.ljkj.lib_common.http;


import com.ljkj.lib_common.bean.AppVersionBean;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.bean.PathInfoBean;
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


    public Observable<BaseResponse<LogBean>> uploadLog(String sn, String logType, File file, String fileName, String jsonString) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sn", sn)
                .addFormDataPart("log_type", logType)
                .addFormDataPart("params", jsonString)
                .addFormDataPart("file", fileName, fileBody)
                .build();

        return getApiService().uploadLog(requestBody);
    }

    public Observable<BaseResponse<AppVersionBean>> checkAppVersion(String moduleName, String deviceType) {
        return getApiService().checkAppVersion(moduleName, deviceType);
    }

    public Observable<BaseResponse<PathInfoBean>> getPathInfo(String deviceCode, String pathId) {
        return getApiService().getPathInfo(deviceCode, pathId);
    }
}

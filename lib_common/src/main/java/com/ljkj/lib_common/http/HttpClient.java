package com.ljkj.lib_common.http;

import com.ljkj.lib_common.bean.AppVersionBean;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.bean.PathInfoBean;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.bean.TestBean;
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


    public Observable<BaseResponse<LogBean>> uploadLog(String sn, String logType, File file, String fileName, String jsonString) {
        return httpHelper.uploadLog(sn, logType, file, fileName, jsonString);
    }

    public Observable<BaseResponse<AppVersionBean>> checkAppVersion(String moduleName, String deviceType) {
        return httpHelper.checkAppVersion(moduleName, deviceType);
    }

    public Observable<BaseResponse<PathInfoBean>> getPathInfo(String deviceCode, String pathId) {
        return httpHelper.getPathInfo(deviceCode, pathId);
    }
}
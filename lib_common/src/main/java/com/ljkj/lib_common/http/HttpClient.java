package com.ljkj.lib_common.http;

import com.ljkj.lib_common.bean.TestBean;
import com.ljkj.lib_common.helper.HttpHelper;

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

    public Observable<String> getTest() {
        // 添加自定义逻辑（例如日志记录、错误处理等）
        return httpHelper.getTest();
    }

    public Observable<String> postTest(String phone, String pwd, String repwd) {
        // 添加自定义逻辑（例如日志记录、错误处理等）
        return httpHelper.postTest(phone, pwd, repwd);
    }
}
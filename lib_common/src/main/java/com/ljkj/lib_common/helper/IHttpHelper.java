package com.ljkj.lib_common.helper;

import com.ljkj.lib_common.bean.TestBean;

import io.reactivex.rxjava3.core.Observable;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public interface IHttpHelper {

    Observable<TestBean> getTest();

    Observable postTest(String phone, String pwd, String rpwd);
}

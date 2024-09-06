package com.ljkj.lib_common.http.api;

import com.ljkj.lib_common.bean.TestBean;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述: 接口列表
 */
public interface ApiService {

    @GET("harmony/index/json")
    Observable getTest();

    @FormUrlEncoded
    @POST("user/login")
    Observable postTest(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);
}

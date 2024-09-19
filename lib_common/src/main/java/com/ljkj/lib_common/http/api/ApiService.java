package com.ljkj.lib_common.http.api;

import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.bean.TestBean;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述: 接口列表
 */
public interface ApiService {

    @GET("tools/list/json")
    Observable<TestBean> getTest();

    @FormUrlEncoded
    @POST("user/login")
    Observable<String> postTest(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    @GET("v1/sharing/path/list")
    Observable<BaseResponse<SharingPathListBean>> postTest2(
            @Query("page_index") String page_index,
            @Query("page_size") String page_size,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("path_type") int path_type);

    /**
     * 文件上传
     *
     * @param sn
     * @param logType
     * @param params
     * @param file
     * @param fileName
     * @return
     */
    @Multipart
    @PUT("v1/upload/log")
    Observable<BaseResponse<LogBean>> uploadLog(
            @Part("sn") RequestBody sn,
            @Part("log_type") RequestBody logType,
            @Part("params") RequestBody params,
            @Part MultipartBody.Part file,
            @Part("file_name") RequestBody fileName
    );
}

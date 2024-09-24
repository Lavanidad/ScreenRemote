package com.ljkj.lib_common.http.api;

import com.ljkj.lib_common.bean.AppVersionBean;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.bean.PathInfoBean;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述: 接口列表
 */
public interface ApiService {


//    @GET("v1/sharing/path/list")
//    Observable<BaseResponse<SharingPathListBean>> postTest2(
//            @Query("page_index") String page_index,
//            @Query("page_size") String page_size,
//            @Query("lat") String lat,
//            @Query("lng") String lng,
//            @Query("path_type") int path_type);

    /**
     * 文件上传
     *
     * @param
     * @return
     */
    @PUT("v1/upload/log")
    Observable<BaseResponse<LogBean>> uploadLog(@Body RequestBody requestBody);

    /**
     * 查询新版本APP
     *
     * @param moduleName H12Pro
     * @param deviceType remote_control
     * @return
     */
    @GET("v1/ota/info/last")
    Observable<BaseResponse<AppVersionBean>> checkAppVersion(
            @Query("module_name") String moduleName,
            @Query("device_type") String deviceType
    );

    /**
     * 获取路径详情
     * @param deviceCode
     * @param pathId
     * @return
     */
    @GET("v1/control/path/info")
    Observable<BaseResponse<PathInfoBean>> getPathInfo(
            @Query("device_code") String deviceCode,
            @Query("path_id") String pathId
    );

}

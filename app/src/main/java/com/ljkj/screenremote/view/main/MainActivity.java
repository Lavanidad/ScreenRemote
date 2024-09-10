package com.ljkj.screenremote.view.main;


import android.util.Log;

import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.http.api.ApiResponse;
import com.ljkj.lib_common.utils.PermissionUtils;
import com.ljkj.screenremote.R;
import com.skydroid.rcsdk.PipelineManager;
import com.skydroid.rcsdk.RCSDKManager;
import com.skydroid.rcsdk.SDKManagerCallBack;
import com.skydroid.rcsdk.comm.CommListener;
import com.skydroid.rcsdk.common.error.SkyException;
import com.skydroid.rcsdk.common.pipeline.Pipeline;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        PermissionUtils.requestPermissions(this);

    }

    @Override
    protected void initData() {
        super.initData();
//        mPresenter.sendGet();
//        mPresenter.sendPost(getApplicationContext(), "test134134", "123456Aa", "123456Aa");

        double lat = 32.81275597;
        double lnt = 118.7267046;

        //苏宁
//        double lat = 32.6155776;
//        double lnt = 118.0686208;
        int page_index = 0;
        int page_size = 50;

        List<String> list_get = new ArrayList<>();
        list_get.add("lat=" + lat);
        list_get.add("lng=" + lnt);
        list_get.add("page_index=" + page_index);
        list_get.add("page_size=" + page_size);
        list_get.add("path_type=-1");
        Collections.sort(list_get);
        String tempStr = "";
        for (String str : list_get) {
            tempStr = tempStr + str + "&";
        }

        mPresenter.sendPost2(page_index + "", page_size + "", lat + "", lnt + "", -1);
    }

    @Override
    public void showPost(String msg) {

    }

    @Override
    public void showGet(String msg) {
        Log.i("Main", msg);
    }

    @Override
    public void showPost2(ApiResponse<SharingPathListBean> bean) {
        Log.e("Main2", bean.toString());
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Log.i("Main2", errorMsg);

    }
}
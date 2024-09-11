package com.ljkj.screenremote.ui.main;


import android.util.Log;

import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.http.api.ApiResponse;
import com.ljkj.lib_common.utils.PermissionUtils;
import com.ljkj.screenremote.R;
import com.ljkj.screenremote.manager.ControllerManager;


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
        ControllerManager.getInstance(this).init();
    }

    @Override
    protected void initData() {
        super.initData();

        double lat = 32.81275597;
        double lnt = 118.7267046;

        mPresenter.sendPost2(0 + "", 50 + "", lat + "", lnt + "", -1);
    }

    @Override
    protected void initListener() {
        ControllerManager.getInstance(this).setRcSerialDataCallback(data -> {

        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ControllerManager.getInstance(this).release();
    }
}
package com.ljkj.screenremote.view.main;


import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.screenremote.R;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.sendGet();
        mPresenter.sendPost(getApplicationContext(), "test134134","123456Aa","123456Aa");
    }

    @Override
    public void showPost(String msg) {

    }

    @Override
    public void showGet(String msg) {

    }
}
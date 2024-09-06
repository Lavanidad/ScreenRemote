package com.ljkj.lib_common.base.activity;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import javax.inject.Inject;

/**
 * 作者: fzy
 * 日期: 2024/9/5
 */
public abstract class BaseActivity<P extends AbstractBasePresenter<BaseView>> extends AbstractBaseActivity implements BaseView {

    //Presenter 对象注入 (注意不能使用 private )
    @Inject
    protected P mPresenter;

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    @Inject
    DispatchingAndroidInjector<android.app.Fragment> frameworkFragmentInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void initView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }


    @Override
    public void showLoginView() {

    }

    @Override
    public void showLogoutView() {

    }

    @Override
    public void pageReload() {

    }
}
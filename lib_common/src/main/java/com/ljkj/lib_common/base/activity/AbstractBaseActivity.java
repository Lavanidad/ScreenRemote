package com.ljkj.lib_common.base.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AbstractBaseActivity extends AppCompatActivity {

    protected AbstractBaseActivity mContext;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
        initListener();
    }

    protected abstract int getLayout();


    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mUnbinder = null;
    }
}
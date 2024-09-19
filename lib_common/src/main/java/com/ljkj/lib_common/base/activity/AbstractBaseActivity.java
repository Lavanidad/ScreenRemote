package com.ljkj.lib_common.base.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;


public abstract class AbstractBaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected AbstractBaseActivity mContext;

    protected T binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getBinding();
        if (binding == null) {
            throw new IllegalStateException("Binding cannot be null");
        }
        setContentView(binding.getRoot());
        mContext = this;
        initView();
        initData();
        initListener();
    }

    protected abstract T getBinding();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
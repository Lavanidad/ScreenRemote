package com.ljkj.screenremote;

import android.app.Application;

import com.ljkj.screenremote.di.component.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class MyApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .application(this)
                .build();
    }

}

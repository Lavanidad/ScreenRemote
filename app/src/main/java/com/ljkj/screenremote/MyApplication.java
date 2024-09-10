package com.ljkj.screenremote;

import com.ljkj.screenremote.di.component.DaggerAppComponent;
import com.ljkj.screenremote.di.module.AppModule;

import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
@Module
public class MyApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .application(this)
                .appModule(new AppModule(this))
                .build();
    }
}

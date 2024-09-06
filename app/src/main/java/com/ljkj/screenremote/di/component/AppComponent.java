package com.ljkj.screenremote.di.component;

import android.app.Application;

import com.ljkj.screenremote.MyApplication;
import com.ljkj.screenremote.di.module.AppModule;
import com.ljkj.screenremote.di.module.ActivityBindingModule;
import com.ljkj.screenremote.di.module.FragmentBindingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
@Singleton
@Component(modules = {
        AppModule.class,
        ActivityBindingModule.class,
        FragmentBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApplication application);

        AppComponent build();
    }

}

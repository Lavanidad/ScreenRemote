package com.ljkj.screenremote.di.module;

import com.ljkj.screenremote.di.module.activity.MainActivityModule;
import com.ljkj.screenremote.di.scope.ActivityScope;
import com.ljkj.screenremote.view.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivity();

}

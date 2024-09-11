package com.ljkj.screenremote.di.module.activity;

import com.ljkj.screenremote.di.scope.ActivityScope;
import com.ljkj.screenremote.ui.main.MainContract;
import com.ljkj.screenremote.ui.main.MainPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
@Module
public abstract class MainActivityModule {
    @ActivityScope
    @Binds
    abstract MainContract.MainActivityPresenter bindPresenter(MainPresenter presenter);
}

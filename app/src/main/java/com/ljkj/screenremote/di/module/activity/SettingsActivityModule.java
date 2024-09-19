package com.ljkj.screenremote.di.module.activity;

import com.ljkj.screenremote.di.scope.ActivityScope;
import com.ljkj.screenremote.ui.settings.SettingsContract;
import com.ljkj.screenremote.ui.settings.SettingsPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
@Module
public abstract class SettingsActivityModule {
    @ActivityScope
    @Binds
    abstract SettingsContract.SettingsPresenter bindPresenter(SettingsPresenter presenter);
}

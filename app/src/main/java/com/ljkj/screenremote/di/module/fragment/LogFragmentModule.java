package com.ljkj.screenremote.di.module.fragment;

import com.ljkj.screenremote.di.scope.FragmentScope;
import com.ljkj.screenremote.ui.settings.contarct.LogFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.LogFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
@Module
public abstract class LogFragmentModule {
    @FragmentScope
    @Binds
    abstract LogFragmentContract.LogPresenter bindPresenter(LogFragmentPresenter presenter);
}

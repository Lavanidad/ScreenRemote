package com.ljkj.screenremote.di.module.fragment;

import com.ljkj.screenremote.di.scope.FragmentScope;
import com.ljkj.screenremote.ui.settings.contarct.UpdateFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.UpdateFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * 作者: fzy
 * 日期: 2024/9/20
 * 描述:
 */
@Module
public abstract class UpdateFragmentModule {
    @FragmentScope
    @Binds
    abstract UpdateFragmentContract.UpdatePresenter bindPresenter(UpdateFragmentPresenter presenter);
}

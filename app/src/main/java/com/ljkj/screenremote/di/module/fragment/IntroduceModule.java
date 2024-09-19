package com.ljkj.screenremote.di.module.fragment;

import com.ljkj.screenremote.di.scope.FragmentScope;
import com.ljkj.screenremote.ui.settings.contarct.IntroduceFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.IntroducePresenter;

import dagger.Binds;
import dagger.Module;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
@Module
public abstract class IntroduceModule {
    @FragmentScope
    @Binds
    abstract IntroduceFragmentContract.IntroducePresenter bindPresenter(IntroducePresenter presenter);
}
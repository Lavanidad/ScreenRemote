package com.ljkj.screenremote.di.module.fragment;

import com.ljkj.screenremote.di.scope.FragmentScope;
import com.ljkj.screenremote.ui.settings.contarct.LanguageFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.LanguageFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
@Module
public abstract class LanguageFragmentModule {

    @FragmentScope
    @Binds
    abstract LanguageFragmentContract.LanguagePresenter bindPresenter(LanguageFragmentPresenter presenter);
}

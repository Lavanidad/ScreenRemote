package com.ljkj.screenremote.di.module;

import com.ljkj.screenremote.di.module.fragment.IntroduceModule;
import com.ljkj.screenremote.di.module.fragment.LanguageFragmentModule;
import com.ljkj.screenremote.di.module.fragment.LogFragmentModule;
import com.ljkj.screenremote.di.scope.FragmentScope;
import com.ljkj.screenremote.ui.settings.fragment.IntroduceFragment;
import com.ljkj.screenremote.ui.settings.fragment.LanguageFragment;
import com.ljkj.screenremote.ui.settings.fragment.LogFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
@Module
public abstract class FragmentBindingModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = LanguageFragmentModule.class)
    abstract LanguageFragment contributeLanguageFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = LogFragmentModule.class)
    abstract LogFragment contributeLogFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = IntroduceModule.class)
    abstract IntroduceFragment contributeIntroduceFragment();

}

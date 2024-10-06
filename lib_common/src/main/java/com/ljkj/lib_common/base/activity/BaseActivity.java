package com.ljkj.lib_common.base.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.SPUtils;
import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;
import com.ljkj.lib_common.common.Constants;
import com.ljkj.lib_common.utils.PermissionUtils;

import java.util.Locale;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import javax.inject.Inject;

/**
 * 作者: fzy
 * 日期: 2024/9/5
 */
public abstract class BaseActivity<P extends AbstractBasePresenter, VB extends ViewBinding> extends AbstractBaseActivity<VB> implements BaseView {

    //Presenter 对象注入 (注意不能使用 private )
    @Inject
    protected P mPresenter;

    protected VB binding;

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    @Inject
    DispatchingAndroidInjector<android.app.Fragment> frameworkFragmentInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        if (binding == null) {
            binding = getBinding();
        }
        if (binding == null) {
            throw new IllegalStateException("Binding cannot be null");
        }
        setContentView(binding.getRoot());
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        PermissionUtils.requestPermissions(this, PermissionUtils.REQUIRED_PERMISSIONS);
        initMapView(savedInstanceState);
    }

    protected void initMapView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(updateResources(context));
    }

    public static Context updateResources(Context context) {
        Resources resources = context.getResources();
        String languageToLoad = SPUtils.getInstance().getString(Constants.SP_KEY_LANGUAGE);
        Locale locale = getLocaleFromLanguage(languageToLoad);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    private static Locale getLocaleFromLanguage(String language) {
        switch (language) {
            case "CN":
                return Locale.SIMPLIFIED_CHINESE;
            case "EN":
                return Locale.US;
            case "UG":
                return new Locale("UG");
            case "KO":
                return Locale.KOREAN;
            case "TH":
                return new Locale("TH");
            case "FRENCH":
                return Locale.FRENCH;
            case "PT":
                return new Locale("PT");
            case "TR":
                return new Locale("TR");
            case "ES":
                return new Locale("ES");
            case "IT":
                return new Locale("IT");
            case "RU":
                return new Locale("RU");
            case "FS":
                return getSystemDefaultLocale();
            default:
                return Locale.SIMPLIFIED_CHINESE;
        }
    }

    private static Locale getSystemDefaultLocale() {
        String localeLanguage = Locale.getDefault().getLanguage();
        return switch (localeLanguage) {
            case "zh" -> Locale.SIMPLIFIED_CHINESE;
            case "en" -> Locale.US;
            case "ug", "ar" -> new Locale("UG");
            case "ko" -> Locale.KOREAN;
            case "th" -> new Locale("TH");
            case "fr" -> Locale.FRENCH;
            case "pt" -> new Locale("PT");
            case "tr" -> new Locale("TR");
            case "es" -> new Locale("ES");
            case "it" -> new Locale("IT");
            case "ru" -> new Locale("RU");
            default -> Locale.SIMPLIFIED_CHINESE;
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    protected abstract VB getBinding();


    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }


    @Override
    public void showLoginView() {

    }

    @Override
    public void showLogoutView() {

    }

    @Override
    public void pageReload() {

    }
}
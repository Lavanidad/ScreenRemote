package com.ljkj.screenremote.ui.settings;

import com.ljkj.lib_common.base.presenter.BaseRxPresenter;
import com.ljkj.lib_common.http.HttpClient;

import javax.inject.Inject;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class SettingsPresenter extends BaseRxPresenter<SettingsContract.SettingsView> implements SettingsContract.SettingsPresenter {

    public static final String TAG = SettingsPresenter.class.getSimpleName();

    private HttpClient mHttpClient;

    @Inject
    public SettingsPresenter(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    @Override
    public void attachView(SettingsContract.SettingsView view) {
        super.attachView(view);
    }
}

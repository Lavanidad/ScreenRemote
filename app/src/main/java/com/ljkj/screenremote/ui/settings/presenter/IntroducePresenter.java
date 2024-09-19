package com.ljkj.screenremote.ui.settings.presenter;

import com.ljkj.lib_common.base.presenter.BaseRxPresenter;
import com.ljkj.lib_common.http.HttpClient;
import com.ljkj.screenremote.ui.settings.contarct.IntroduceFragmentContract;

import javax.inject.Inject;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class IntroducePresenter extends BaseRxPresenter<IntroduceFragmentContract.IntroduceFragmentView> implements IntroduceFragmentContract.IntroducePresenter {

    public static final String TAG = LanguageFragmentPresenter.class.getSimpleName();

    private HttpClient mHttpClient;

    @Inject
    public IntroducePresenter(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    @Override
    public void attachView(IntroduceFragmentContract.IntroduceFragmentView view) {
        super.attachView(view);
    }
}
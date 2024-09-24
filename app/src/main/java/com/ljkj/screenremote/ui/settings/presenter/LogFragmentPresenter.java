package com.ljkj.screenremote.ui.settings.presenter;


import android.content.Context;

import com.ljkj.lib_common.base.presenter.BaseRxPresenter;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.http.HttpClient;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.rx.ProgressObserver;
import com.ljkj.lib_common.rx.RxSchedulers;
import com.ljkj.screenremote.ui.settings.contarct.LogFragmentContract;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class LogFragmentPresenter extends BaseRxPresenter<LogFragmentContract.LogView> implements LogFragmentContract.LogPresenter {

    public static final String TAG = LanguageFragmentPresenter.class.getSimpleName();

    private HttpClient mHttpClient;

    @Inject
    public LogFragmentPresenter(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    @Override
    public void attachView(LogFragmentContract.LogView view) {
        super.attachView(view);
    }


    @Override
    public void uploadLog(Context context, String sn, String logType, File file, String fileName, String jsonString) {
        Observable<BaseResponse<LogBean>> responseBodyObservable = mHttpClient.uploadLog(sn, logType, file, fileName, jsonString);
        responseBodyObservable.compose(RxSchedulers.observableIO2Main())
                .subscribe(new ProgressObserver<BaseResponse<LogBean>>(context, "上传中") {
                    @Override
                    public void onSuccess(BaseResponse<LogBean> result) {
                        mView.showUpLoadSuccess(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.showErrorMsg(errorMsg);
                    }
                });
    }
}
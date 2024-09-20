package com.ljkj.screenremote.ui.settings.presenter;

import android.content.Context;

import com.ljkj.lib_common.base.presenter.BaseRxPresenter;
import com.ljkj.lib_common.bean.AppVersionBean;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.http.HttpClient;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.rx.BaseObserver;
import com.ljkj.lib_common.rx.ProgressObserver;
import com.ljkj.lib_common.rx.RxSchedulers;
import com.ljkj.screenremote.ui.settings.contarct.LogFragmentContract;
import com.ljkj.screenremote.ui.settings.contarct.UpdateFragmentContract;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

/**
 * 作者: fzy
 * 日期: 2024/9/20
 * 描述:
 */
public class UpdateFragmentPresenter extends BaseRxPresenter<UpdateFragmentContract.UpdateView> implements UpdateFragmentContract.UpdatePresenter {

    public static final String TAG = LanguageFragmentPresenter.class.getSimpleName();

    private HttpClient mHttpClient;

    @Inject
    public UpdateFragmentPresenter(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    @Override
    public void attachView(UpdateFragmentContract.UpdateView view) {
        super.attachView(view);
    }

    @Override
    public void checkAppVersion(String moduleName, String deviceType) {
        Observable<BaseResponse<AppVersionBean>> responseObservable = mHttpClient.checkAppVersion(moduleName,deviceType);
        responseObservable.compose(RxSchedulers.observableIO2Main())
                .subscribe(new BaseObserver<>() {
                    @Override
                    public void onSuccess(BaseResponse<AppVersionBean> result) {
                        mView.showUpdateSuccess(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.showUpdateFailed(errorMsg);
                    }
                });
    }
}

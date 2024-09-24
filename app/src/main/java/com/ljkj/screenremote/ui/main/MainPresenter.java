package com.ljkj.screenremote.ui.main;

import android.content.Context;
import android.util.Log;

import com.ljkj.lib_common.base.presenter.BaseRxPresenter;
import com.ljkj.lib_common.bean.PathInfoBean;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.bean.TestBean;
import com.ljkj.lib_common.http.HttpClient;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.rx.BaseObserver;
import com.ljkj.lib_common.rx.ProgressObserver;
import com.ljkj.lib_common.rx.RxSchedulers;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;


/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class MainPresenter extends BaseRxPresenter<MainContract.MainView> implements MainContract.MainActivityPresenter {

    public static final String TAG = MainPresenter.class.getSimpleName();

    private HttpClient mHttpClient;

    @Inject
    public MainPresenter(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    @Override
    public void attachView(MainContract.MainView view) {
        super.attachView(view);
    }

    @Override
    public void sendPost(Context context, String username, String pwd, String rpwd) {
        Observable<String> responseBodyObservable = mHttpClient.postTest(username, pwd, rpwd);
        responseBodyObservable.compose(RxSchedulers.observableIO2Main(context))
                .subscribe(new ProgressObserver<String>(context, "登录中……") {
                    @Override
                    public void onSuccess(String result) {
                        mView.showPost(String.valueOf(result));
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.showErrorMsg(errorMsg);
                    }
                });
    }

    @Override
    public void sendGet() {
        Observable<TestBean> responseBodyObservable = mHttpClient.getTest();
        responseBodyObservable.compose(RxSchedulers.observableIO2Main())
                .subscribe(new BaseObserver<TestBean>() {
                    @Override
                    public void onSuccess(TestBean result) {
                        Log.i("Main3", result.toString());
                        mView.showGet(String.valueOf(result));
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        Log.i("Main4", errorMsg);
                        mView.showErrorMsg(errorMsg);
                    }
                });
    }

    @Override
    public void getPathInfo(String deviceCode, String pathId) {
        Observable<BaseResponse<PathInfoBean>> responseObservable = mHttpClient.getPathInfo(deviceCode, pathId);
        responseObservable.compose(RxSchedulers.observableIO2Main())
                .subscribe(new BaseObserver<BaseResponse<PathInfoBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<PathInfoBean> result) {
                        mView.showPathInfo(result);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        mView.getPathInfoError(errorMsg);
                    }
                });
    }
}

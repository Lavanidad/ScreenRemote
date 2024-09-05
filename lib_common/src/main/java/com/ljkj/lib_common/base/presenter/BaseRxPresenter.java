package com.ljkj.lib_common.base.presenter;

import android.util.Log;

import com.ljkj.lib_common.base.BaseView;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者: fzy
 * 日期: 2024/9/5
 */
public class BaseRxPresenter<T extends BaseView> implements AbstractBasePresenter<T> {

    public static final String TAG = BaseRxPresenter.class.getSimpleName();

    protected T mView;
    private CompositeDisposable compositeDisposable;

    public BaseRxPresenter() {

    }

    protected void addEventSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
        if (mView != null) {
            Log.e(TAG, "mView 不为空" + mView.getClass());
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void addRxBindingSubscribe(Disposable disposable) {
        addEventSubscribe(disposable);
    }

    @Override
    public boolean isViewAttached() {
        return false;
    }

    @Override
    public void onDestroy() {

    }
}

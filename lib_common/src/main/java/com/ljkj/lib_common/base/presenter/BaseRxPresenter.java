package com.ljkj.lib_common.base.presenter;

import android.util.Log;

import com.ljkj.lib_common.base.BaseView;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者: fzy
 * 日期: 2024/9/5
 */
public class BaseRxPresenter<V extends BaseView> implements AbstractBasePresenter<V> {

    public static final String TAG = BaseRxPresenter.class.getSimpleName();

    protected V mView;
    private CompositeDisposable compositeDisposable;

    public BaseRxPresenter() {
        //tips:依赖倒置原则，子类自由按需注入
    }

    protected void addEventSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void attachView(V view) {
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
        return mView != null;
    }

    @Override
    public void onDestroy() {

    }
}

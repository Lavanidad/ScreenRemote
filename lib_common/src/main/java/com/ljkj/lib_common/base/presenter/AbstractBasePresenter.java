package com.ljkj.lib_common.base.presenter;

import com.ljkj.lib_common.base.BaseView;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者: fzy
 * 日期: 2024/9/5
 */
public interface AbstractBasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

    void addRxBindingSubscribe(Disposable disposable);


    boolean isViewAttached();

    void onDestroy();

}
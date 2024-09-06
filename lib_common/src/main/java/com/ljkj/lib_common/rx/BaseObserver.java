package com.ljkj.lib_common.rx;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public abstract class BaseObserver<T> implements Observer<T> {

    public abstract void onSuccess(T result);

    public abstract void onFailure(Throwable e, String errorMsg);

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e, RxExceptions.exceptionHandler(e));
    }

    @Override
    public void onComplete() {

    }
}

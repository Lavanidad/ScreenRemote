package com.ljkj.lib_common.rx;


import android.content.Context;

import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.components.RxActivity;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle4.components.support.RxFragmentActivity;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> observableIO2Main(final Context context) {
        return upstream -> {
            Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            return composeContext(context, observable);
        };
    }

    public static <T> ObservableTransformer<T, T> observableIO2Main() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static <T> ObservableSource<T> composeContext(Context context, Observable<T> observable) {
        if (context instanceof RxActivity) {
            return observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxFragmentActivity) {
            return observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else if (context instanceof RxAppCompatActivity) {
            return observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        } else {
            // Ensure that `observable` is returned as is if no known context is matched
            return observable;
        }
    }
}

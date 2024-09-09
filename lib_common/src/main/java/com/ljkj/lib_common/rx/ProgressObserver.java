package com.ljkj.lib_common.rx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public abstract class ProgressObserver<T> extends BaseObserver<T> {
    private ProgressDialog progressDialog;
    private Context mContext;
    private String mLoadingText;

    public ProgressObserver(Context context) {
        this(context, null);
    }

    public ProgressObserver(Context context, String loadingText) {
        this.mContext = context;
        this.mLoadingText = loadingText;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!d.isDisposed() && mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(mLoadingText == null ? "正在加载中..." : mLoadingText);
            progressDialog.show();
        }
    }


    @Override
    public void onComplete() {
        super.onComplete();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

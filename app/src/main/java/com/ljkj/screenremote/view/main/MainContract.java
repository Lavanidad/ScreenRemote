package com.ljkj.screenremote.view.main;

import android.content.Context;

import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public interface MainContract {

    interface MainView extends BaseView {
        void showPost(String msg);

        void showGet(String msg);
    }

    interface MainActivityPresenter extends AbstractBasePresenter<MainView> {
        void sendPost(Context context, String username, String pwd, String rpwd);

        void sendGet();
    }
}

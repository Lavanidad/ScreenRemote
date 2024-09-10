package com.ljkj.screenremote.view.main;

import android.content.Context;

import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.http.api.ApiResponse;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public interface MainContract {

    interface MainView extends BaseView {
        void showPost(String msg);

        void showGet(String msg);

        void showPost2(ApiResponse<SharingPathListBean> bean);
    }

    interface MainActivityPresenter extends AbstractBasePresenter<MainView> {
        void sendPost(Context context, String username, String pwd, String rpwd);

        void sendGet();

        void sendPost2(String page_index, String page_size, String lat, String lng, int path_type);
    }
}

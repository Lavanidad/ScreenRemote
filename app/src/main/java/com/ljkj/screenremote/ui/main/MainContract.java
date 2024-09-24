package com.ljkj.screenremote.ui.main;

import android.content.Context;

import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;
import com.ljkj.lib_common.bean.PathInfoBean;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.http.api.BaseResponse;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述:
 */
public interface MainContract {

    interface MainView extends BaseView {
        void showPathInfo(BaseResponse<PathInfoBean> response);

        void getPathInfoError(String msg);
    }

    interface MainActivityPresenter extends AbstractBasePresenter<MainView> {
        void getPathInfo(String deviceCode, String pathId);
    }
}

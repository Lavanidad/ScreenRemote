package com.ljkj.screenremote.ui.settings.contarct;

import android.content.Context;

import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;
import com.ljkj.lib_common.bean.AppVersionBean;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.http.api.BaseResponse;

import java.io.File;

/**
 * 作者: fzy
 * 日期: 2024/9/20
 * 描述:
 */
public interface UpdateFragmentContract {

    interface UpdateView extends BaseView {

        void showUpdateSuccess(BaseResponse<AppVersionBean> response);

        void showUpdateFailed(String msg);
    }

    interface UpdatePresenter extends AbstractBasePresenter<UpdateFragmentContract.UpdateView> {
        void checkAppVersion(String moduleName, String deviceType);
    }
}
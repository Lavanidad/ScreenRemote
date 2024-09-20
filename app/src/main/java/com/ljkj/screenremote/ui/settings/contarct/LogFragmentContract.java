package com.ljkj.screenremote.ui.settings.contarct;

import android.content.Context;

import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;
import com.ljkj.lib_common.bean.LogBean;
import com.ljkj.lib_common.http.api.BaseResponse;

import java.io.File;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public interface LogFragmentContract {
    interface LogView extends BaseView {
        void showUpLoadSuccess(BaseResponse<LogBean> response);

        void showUpLoadFailed(BaseResponse<LogBean> response);
    }

    interface LogPresenter extends AbstractBasePresenter<LogView> {
        void uploadLog(Context context, String sn, String logType, File file, String fileName, String jsonString);
    }
}
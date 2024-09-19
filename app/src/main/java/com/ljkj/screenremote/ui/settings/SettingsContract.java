package com.ljkj.screenremote.ui.settings;

import com.ljkj.lib_common.base.BaseView;
import com.ljkj.lib_common.base.presenter.AbstractBasePresenter;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public interface SettingsContract {

    interface SettingsView extends BaseView {

    }

    interface SettingsPresenter extends AbstractBasePresenter<SettingsContract.SettingsView> {

    }
}

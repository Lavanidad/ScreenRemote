package com.ljkj.lib_common.base;

/**
 * 作者: fzy
 * 日期: 2024/9/5
 */
public interface BaseView {
    void showNormal();
    void showError();
    void showLoading();
    void showErrorMsg(String errorMsg);
    void showLoginView();
    void showLogoutView();
    void pageReload();
}

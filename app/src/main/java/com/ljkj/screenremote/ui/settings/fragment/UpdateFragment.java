package com.ljkj.screenremote.ui.settings.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.Toaster;
import com.ljkj.lib_common.base.fragment.BaseFragment;
import com.ljkj.lib_common.bean.AppVersionBean;
import com.ljkj.lib_common.common.Constants;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.manager.DownLoadApkManager;
import com.ljkj.lib_common.utils.PermissionUtils;
import com.ljkj.lib_common.utils.UpdateUtils;
import com.ljkj.screenremote.databinding.FragmentUpdateBinding;
import com.ljkj.screenremote.ui.settings.contarct.UpdateFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.UpdateFragmentPresenter;

import java.io.File;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者: fzy
 * 日期: 2024/9/20
 * 描述:
 */
public class UpdateFragment extends BaseFragment<UpdateFragmentPresenter, FragmentUpdateBinding> implements UpdateFragmentContract.UpdateView {

    public static final String TAG = UpdateFragment.class.getSimpleName();

    private Disposable downloadDisposable;
    private DownLoadApkManager downLoadApkManager;
    private String apkName;

    @Override
    protected FragmentUpdateBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentUpdateBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        binding.tvAppName.setText(AppUtils.getAppName());
        binding.tvVersion.setText(AppUtils.getAppVersionName());
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    protected void initListener() {
        binding.btnUpdate.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                if (binding.btnUpdate.getText().toString().equals("安装")) {
                    checkPermissionsAndInstall(apkName);
                } else {
                    mPresenter.checkAppVersion("H12Pro", "remote_control");
                }
            }
        });
    }


    @Override
    public void showUpdateSuccess(BaseResponse<AppVersionBean> response) {
        if (response == null || response.getErr_data() == null) {
            Toaster.show("升级：响应数据为空，无法进行更新检查");
            return;
        }
        Log.e(TAG, response.toString());

        String currentVersion = AppUtils.getAppVersionName();
        String serverVersion = response.getErr_data().getVersion();

        if (UpdateUtils.isUpdateNeeded(currentVersion, serverVersion)) {
            String downloadUrl = response.getErr_data().getPath();
            apkName = "H12Pro_" + response.getErr_data().getVersion() + ".apk";

            PermissionUtils.requestPermissions(getActivity());

            // 检查是否拥有所需权限
            if (XXPermissions.isGranted(getActivity(), PermissionUtils.REQUIRED_PERMISSIONS_UPDATE)) {

                downLoadApkManager = DownLoadApkManager.getInstance(Constants.APK_DOWNLOAD_FILE_PATH, apkName);

                downloadDisposable = downLoadApkManager.downLoadApk(downloadUrl)
                        .subscribe(progress -> {
                            Log.d(TAG, "下载 Progress: " + progress + "%");
                            binding.tvProgress.setVisibility(View.VISIBLE);
                            binding.tvProgress.setText(progress + "%");

                            if (progress == 100) {
                                binding.btnUpdate.setText("安装");
                            } else {
                                binding.btnUpdate.setText("升级");
                            }
                        }, throwable -> {
                            Log.e(TAG, "下载失败: " + throwable.getMessage());
                            Toaster.show("下载失败，请重试");
                        }, () -> {
                            Log.i(TAG, "下载成功，准备安装");
                            Toaster.show("下载成功，准备安装...");
                            UpdateUtils.installApk(getActivity(), new File(Constants.APK_DOWNLOAD_FILE_PATH + apkName));
                        });

            } else {
                Toaster.show("请授予必要的权限以进行下载和安装");
            }
        } else {
            Log.i(TAG, "当前版本已是最新，无需更新");
        }
    }

    private void checkPermissionsAndInstall(String apkName) {
        if (XXPermissions.isGranted(getActivity(), PermissionUtils.REQUIRED_PERMISSIONS_UPDATE)) {
            // 权限已授予，直接安装
            UpdateUtils.installApk(getActivity(), new File(Constants.APK_DOWNLOAD_FILE_PATH + apkName));
        } else {
            // 权限未授予，跳转到权限设置页面
            PermissionUtils.requestPermissions(getActivity());
        }
    }


    @Override
    public void showUpdateFailed(String msg) {
        Toaster.show(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (downloadDisposable != null && !downloadDisposable.isDisposed()) {
            downloadDisposable.dispose();
        }
    }
}

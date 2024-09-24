package com.ljkj.lib_common.utils;

import android.Manifest;
import android.content.Context;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.Toaster;

import java.util.List;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class PermissionUtils {

    // 权限列表
    public static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_SETTINGS,
    };

    // 安装权限列表
    public static final String[] REQUIRED_PERMISSIONS_UPDATE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,       // 写入外部存储 (Android 10 及以下)
            Manifest.permission.REQUEST_INSTALL_PACKAGES
    };

    public static void requestPermissions(Context context, String[] permissions) {
        XXPermissions.with(context)
                .permission(permissions)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
//                            Toaster.show("获取部分权限成功，但部分权限未正常授予");
                            return;
                        }
//                        Toaster.show("获取所有权限成功");
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
//                            Toaster.show("被永久拒绝授权，请手动授予权限");
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
//                            Toaster.show("获取权限失败");
                        }
                    }
                });
    }


    //eg: PermissionUtils.requestSinglePermission(this, PermissionUtils.PERMISSION_CAMERA);
    //public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static void requestSinglePermission(Context context, String permission) {
        XXPermissions.with(context)
                .permission(permission)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (allGranted) {
                            Toaster.show("获取权限成功");
                        } else {
                            Toaster.show("获取部分权限成功，但部分权限未正常授予");
                        }
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            Toaster.show("被永久拒绝授权，请手动授予权限");
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            Toaster.show("获取权限失败");
                        }
                    }
                });
    }
}

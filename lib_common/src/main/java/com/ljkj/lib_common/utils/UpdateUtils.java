package com.ljkj.lib_common.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.ljkj.lib_common.boot.MyAccessibilityService;

import java.io.File;

/**
 * 作者: fzy
 * 日期: 2024/9/20
 * 描述:
 */
public class UpdateUtils {

    public static boolean isUpdateNeeded(String currentVersion, String serverVersion) {
        if (currentVersion == null || serverVersion == null) {
            return false;
        }

        String[] currentParts = currentVersion.split("\\.");
        String[] serverParts = serverVersion.split("\\.");

        int length = Math.max(currentParts.length, serverParts.length);

        for (int i = 0; i < length; i++) {
            int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int serverPart = i < serverParts.length ? Integer.parseInt(serverParts[i]) : 0;

            if (currentPart < serverPart) {
                return true;  // 当前版本小于线上版本，需要升级
            } else if (currentPart > serverPart) {
                return false;  // 当前版本大于线上版本，不需要升级
            }
        }

        return false;  // 版本相同，不需要升级
    }


    /**
     * 安装APK文件
     * @param context 上下文
     * @param apkFile APK文件
     */
    public static void installApk(Context context, File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            Log.e("UpdateUtils", "安装文件不存在");
            return;
        }

        // 创建URI，根据Android版本进行适配
        Uri apkUri = FileProvider.getUriForFile(context, "com.ljkj.screenremote.fileprovider", apkFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

        // 检查是否需要 "安装未知来源应用" 的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean canInstall = context.getPackageManager().canRequestPackageInstalls();
            if (!canInstall) {
                // 打开设置界面，提示用户允许安装未知来源应用
                Intent settingIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                settingIntent.setData(Uri.parse("package:" + context.getPackageName()));
                settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(settingIntent);
                return;
            }
        }

        // 启动安装流程
        context.startActivity(intent);

        // 检查并启动辅助功能服务
        if (!isAccessibilityServiceEnabled(context, MyAccessibilityService.class)) {
            Intent accessibilityIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(accessibilityIntent);
            Toast.makeText(context, "请启用辅助功能服务以自动安装 APK", Toast.LENGTH_LONG).show();
        }
    }

    // 检查辅助服务是否启用
    private static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> accessibilityService) {
        String service = context.getPackageName() + "/" + accessibilityService.getName();
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        String enabledServices = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServices != null) {
            splitter.setString(enabledServices);
            while (splitter.hasNext()) {
                String componentName = splitter.next();
                if (componentName.equalsIgnoreCase(service)) {
                    return true;
                }
            }
        }
        return false;
    }

}

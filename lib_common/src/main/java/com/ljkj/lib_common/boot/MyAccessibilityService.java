package com.ljkj.lib_common.boot;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * 作者: fzy
 * 日期: 2024/9/20
 * 描述:
 */
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 当系统发生可访问性事件时触发
        if (event == null) return;
        String eventPackage = (String) event.getPackageName();
        Log.d("AccessibilityService", "Event from package: " + eventPackage);

        // 遍历窗口内容，寻找“安装”按钮并点击
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            performAutoClick(rootNode);
        }
    }

    private void performAutoClick(AccessibilityNodeInfo node) {
        if (node == null) return;

        // 检查按钮内容
        if (node.getClassName().equals("android.widget.Button")) {
            String buttonText = node.getText() != null ? node.getText().toString() : "";
            if (buttonText.equals("安装") || buttonText.equals("确定") || buttonText.equals("完成")) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);  // 执行点击
                Log.d("AccessibilityService", "Clicked on button: " + buttonText);
            }
        }

        // 遍历子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            performAutoClick(node.getChild(i));
        }
    }

    @Override
    public void onInterrupt() {
        // 辅助服务中断时调用
    }
}

package com.ljkj.lib_common.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * 作者: fzy
 * 日期: 2024/9/18
 * 描述:
 */
public class BatteryLevelReceiver extends BroadcastReceiver {
    
    public static final String TAG = BatteryLevelReceiver.class.getSimpleName();
    
    private BatteryCallback callback;

    public interface BatteryCallback {
        void onBatteryLevelChanged(String percent);
    }

    public BatteryLevelReceiver(BatteryCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();

        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            Log.d(TAG,"receive the battery's change, action is: " + action);

            int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Log.d(TAG,"the battery level is: " + currentLevel);

            int maxLevel = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            Log.d(TAG,"the battery maxLevel is: " + maxLevel);

            String percent = new DecimalFormat("00%").format(currentLevel / (float) maxLevel);;
            Log.d(TAG,"battery percent: " + percent);

            if (callback != null) {
                callback.onBatteryLevelChanged(percent);
            }

        } else if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.d(TAG,"the usb is connected");

        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.d(TAG,"the usb is disconnected");
        }
    }
}

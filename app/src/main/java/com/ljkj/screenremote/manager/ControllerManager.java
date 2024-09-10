package com.ljkj.screenremote.manager;

import android.content.Context;
import android.util.Log;

import com.skydroid.rcsdk.*;
import com.skydroid.rcsdk.PipelineManager;
import com.skydroid.rcsdk.RCSDKManager;
import com.skydroid.rcsdk.SDKManagerCallBack;
import com.skydroid.rcsdk.comm.CommListener;
import com.skydroid.rcsdk.common.callback.CompletionCallbackWith;
import com.skydroid.rcsdk.common.callback.KeyListener;
import com.skydroid.rcsdk.common.error.SkyException;
import com.skydroid.rcsdk.common.pipeline.Pipeline;
import com.skydroid.rcsdk.common.remotecontroller.ControlMode;
import com.skydroid.rcsdk.key.AirLinkKey;
import com.skydroid.rcsdk.key.RemoteControllerKey;

import java.security.Key;
import java.util.Arrays;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class ControllerManager {

    public static final String TAG = ControllerManager.class.getSimpleName();
    private static final String PIPELINE_DEVICE = "/dev/ttyHS1";
    private static final int PIPELINE_BAUD_RATE = 921600;

    private final Context context;
    private static ControllerManager instance;

    private Pipeline mPipeline = null;

    public static synchronized ControllerManager getInstance(Context context) {
        if (instance == null) {
            instance = new ControllerManager(context);
        }
        return instance;
    }

    private ControllerManager(Context context) {
        this.context = context;
    }

    public void init() {
        RCSDKManager.INSTANCE.initSDK(context, new SDKManagerCallBack() {
            @Override
            public void onRcConnected() {
                Pipeline pipeline = PipelineManager.INSTANCE.createSerialPipeline(PIPELINE_DEVICE, PIPELINE_BAUD_RATE);
                pipeline.setOnCommListener(getCommListener());
                PipelineManager.INSTANCE.connectPipeline(pipeline);
                mPipeline = pipeline;
            }

            @Override
            public void onRcConnectFail(SkyException e) {
                Log.d(TAG, "onRcConnectFail e " + e.toString());
            }

            @Override
            public void onRcDisconnect() {
                Log.d(TAG, "onRcDisconnect");
            }
        });

        RCSDKManager.INSTANCE.connectToRC();

        startKeyManagerOperations();
    }

    private void startKeyManagerOperations() {
        KeyManager.INSTANCE.cancelListen(keySignalQualityListener);
        getControlMode();
        getChannels();
        getSerialNumber();
        getSignalQuality();
    }

    public void getControlMode() {
        KeyManager.INSTANCE.get(RemoteControllerKey.INSTANCE.getKeyControlMode(), new CompletionCallbackWith<>() {
            @Override
            public void onSuccess(ControlMode controlMode) {
                Log.d(TAG, "获取摇杆模式：" + controlMode.name());
            }

            @Override
            public void onFailure(SkyException e) {
                Log.d(TAG, "获取摇杆模式失败：" + e);
            }
        });
    }

    public void getChannels() {
        KeyManager.INSTANCE.get(RemoteControllerKey.INSTANCE.getKeyChannels(), new CompletionCallbackWith<>() {
            @Override
            public void onSuccess(int[] value) {
                Log.d(TAG, "获取摇杆杆量：" + Arrays.toString(value));
            }

            @Override
            public void onFailure(SkyException e) {
                Log.d(TAG, "获取摇杆杆量 SkyException: " + e);
            }
        });
    }

    public void getSerialNumber() {
        KeyManager.INSTANCE.get(RemoteControllerKey.INSTANCE.getKeySerialNumber(), new CompletionCallbackWith<>() {
            @Override
            public void onSuccess(String p0) {
                Log.d(TAG, "获取序列号：" + p0);
            }

            @Override
            public void onFailure(SkyException p0) {
                Log.d(TAG, "获取序列号 SkyException: " + p0);
            }
        });
    }

    public void getSignalQuality() {
        KeyManager.INSTANCE.cancelListen(keySignalQualityListener);
        //H12Pro/H16/H30/H20的信号强度为LISTEN方式,设置监听器后，会一直回调，直到取消监听
        KeyManager.INSTANCE.listen(AirLinkKey.INSTANCE.getKeySignalQuality(),keySignalQualityListener);
    }
    private final KeyListener<Integer> keySignalQualityListener = (oldValue, newValue) -> Log.e(TAG, "信号强度:" + newValue);



    private CommListener getCommListener() {
        return new CommListener() {
            @Override
            public void onConnectSuccess() {
                Log.d(TAG, "连接成功");
            }


            @Override
            public void onConnectFail(SkyException e) {
                Log.d(TAG, "连接失败:" + e);
            }

            @Override
            public void onDisconnect() {
                Log.d(TAG, "断开连接");
            }

            @Override
            public void onReadData(byte[] bytes) {
                Log.d(TAG, " 收到长度 " + bytes.length + " ,,, 数据 " + new String(bytes));

            }
        };
    }


    /**
     * 回收方法，必须调用
     */
    public void release() {
        RCSDKManager.INSTANCE.disconnectRC();
        KeyManager.INSTANCE.cancelListen(keySignalQualityListener);
        Pipeline p = mPipeline;
        if (p != null){
            PipelineManager.INSTANCE.disconnectPipeline(p);
        }
    }
}


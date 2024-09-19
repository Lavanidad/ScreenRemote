package com.ljkj.screenremote.manager;

import android.content.Context;
import android.util.Log;

import com.ljkj.screenremote.callback.RCDataCallback;
import com.ljkj.screenremote.helper.ReadRCButtonHelper;
import com.ljkj.screenremote.utils.ByteUtils;
import com.ljkj.screenremote.helper.SerialDataParserHelper;
import com.skydroid.rcsdk.*;
import com.skydroid.rcsdk.PipelineManager;
import com.skydroid.rcsdk.RCSDKManager;
import com.skydroid.rcsdk.SDKManagerCallBack;
import com.skydroid.rcsdk.comm.CommListener;
import com.skydroid.rcsdk.common.button.ButtonAction;
import com.skydroid.rcsdk.common.button.ButtonConfig;
import com.skydroid.rcsdk.common.button.ButtonHandler;
import com.skydroid.rcsdk.common.button.ButtonHelper;
import com.skydroid.rcsdk.common.button.HandleButtonMode;
import com.skydroid.rcsdk.common.callback.CompletionCallback;
import com.skydroid.rcsdk.common.callback.CompletionCallbackWith;
import com.skydroid.rcsdk.common.callback.KeyListener;
import com.skydroid.rcsdk.common.error.SkyException;
import com.skydroid.rcsdk.common.pipeline.Pipeline;
import com.skydroid.rcsdk.common.remotecontroller.ControlMode;
import com.skydroid.rcsdk.key.AirLinkKey;
import com.skydroid.rcsdk.key.RemoteControllerKey;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class ControllerManager {

    private static final String TAG = ControllerManager.class.getSimpleName();

    public static final boolean isOpen = false;

    private final Context context;
    private static ControllerManager instance;

    private static final String PIPELINE_DEVICE = "/dev/ttyHS1";
    private static final int PIPELINE_BAUD_RATE = 921600;

    private static final String FRAME_HEAD = "AA55";
    private static final int DEFINE_REPORT_LENGTH = 54;

    private static final int CHANNEL_LOW = 1050;
    private static final int CHANNEL_MIDDLE = 1500;
    private static final int CHANNEL_HIGH = 1950;
    private long currentTime = 0L;

    private Pipeline mPipeline = null;
    private boolean isReadingByte = true;
    private ByteBuffer remainBuffer;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private RCDataCallback rcSerialDataCallback;
    private RCDataCallback rcSignalQualityCallback;

    private ReadRCButtonHelper readRCButtonHelper;
    private ButtonHelper customButtonHelper;


    public static synchronized ControllerManager getInstance(Context context) {
        if (instance == null) {
            instance = new ControllerManager(context);
        }
        return instance;
    }

    private ControllerManager(Context context) {
        this.context = context;
    }

    /**
     * @param callback 串口数据读取回调
     */
    public void setRcSerialDataCallback(RCDataCallback callback) {
        this.rcSerialDataCallback = callback;
    }

    /**
     * @param callback 信号强度
     */
    public void setRcSignalQualityCallback(RCDataCallback callback) {
        this.rcSignalQualityCallback = callback;
    }

    /**
     * 初始化，连接遥控器
     */
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
                if (remainBuffer != null) {
                    remainBuffer.clear();
                    remainBuffer = null;
                }
            }
        });

        RCSDKManager.INSTANCE.connectToRC();

        startKeyManagerOperations();
    }

    /**
     * 统一管理初始化后的操作  TODO：0911 是否要默认开启对频
     */
    private void startKeyManagerOperations() {
        KeyManager.INSTANCE.cancelListen(keySignalQualityListener);//确保反复监听问题，SDK写法
        getControlMode();
        getChannels();
        getSerialNumber();
        getSignalQuality();
        readByte();
        registerKeyListener();
    }

    /**
     * 获取摇杆模式
     */
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

    /**
     * 获取摇杆量
     */
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

    /**
     * 获取序列号
     */
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

    /**
     * 获取信号强度
     */
    private void getSignalQuality() {
        KeyManager.INSTANCE.cancelListen(keySignalQualityListener);
        //H12Pro/H16/H30/H20的信号强度为LISTEN方式,设置监听器后，会一直回调，直到取消监听
        KeyManager.INSTANCE.listen(AirLinkKey.INSTANCE.getKeySignalQuality(), keySignalQualityListener);
    }

    private final KeyListener<Integer> keySignalQualityListener = (oldValue, newValue) -> {
        if (rcSignalQualityCallback != null) {
            rcSignalQualityCallback.onSignalQualityData(newValue);
        }
        Log.d(TAG, "信号强度:" + newValue);
    };


    /**
     * 对频
     */
    public void requestPairing() {
        KeyManager.INSTANCE.action(RemoteControllerKey.INSTANCE.getKeyRequestPairing(), new CompletionCallback() {
            @Override
            public void onResult(SkyException e) {
                Log.e(TAG, "对频:" + e.toString());
            }
        });
    }


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
                if (isOpen) {
                    Log.d(TAG, "【RCSDK】size: " + bytes.length + ", 原始data: " + Arrays.toString(bytes));
                }

                if (remainBuffer != null) {
                    int remaining = remainBuffer.capacity();
                    byte[] remainByte = remainBuffer.array();
                    ByteBuffer tempBuffer = ByteBuffer.allocate(remaining + bytes.length);
                    tempBuffer.put(remainByte, 0, remaining);
                    tempBuffer.put(bytes, 0, bytes.length);
                    remainBuffer = tempBuffer;
//                    Log.d(TAG, "【RCSDK】remainBuffer length：" + remainBuffer.capacity());
                } else {
                    ByteBuffer initBuffer = ByteBuffer.allocate(bytes.length);
                    initBuffer.put(bytes, 0, bytes.length);
                    remainBuffer = initBuffer;
//                    Log.d(TAG, "【RCSDK】remainBuffer length init：" + remainBuffer.capacity());
                }
            }
        };
    }


    private void readByte() {
        scheduler.scheduleWithFixedDelay(() -> {
            if (isReadingByte) {
                try {
                    if (remainBuffer != null) {
                        int capacity = remainBuffer.capacity();
                        byte[] array = remainBuffer.array();
                        if (isOpen) {
                            Log.d(TAG, "【RCSDK】开始读取=====： " + capacity + ",data:" + java.util.Arrays.toString(array));
                        }
                        // 查找帧头 AA55
                        String hexString = ByteUtils.byteToHexString(array);
                        if (isOpen) {
                            Log.d(TAG, "【RCSDK】hexString: " + hexString);
                        }
                        int bodyStartPosition = hexString.indexOf(FRAME_HEAD) / 3;
//                        Log.d(TAG, "【RCSDK】bodyStartPosition: " + bodyStartPosition);

                        if ((capacity >= bodyStartPosition + DEFINE_REPORT_LENGTH) && bodyStartPosition >= 0) {
                            remainBuffer.flip();
                            ByteBuffer dataBuffer = ByteBuffer.allocate(DEFINE_REPORT_LENGTH);
                            dataBuffer.put(array, bodyStartPosition, DEFINE_REPORT_LENGTH);
                            remainBuffer.position(bodyStartPosition + DEFINE_REPORT_LENGTH);

                            // 调用回调方法
                            if (rcSerialDataCallback != null) {
                                rcSerialDataCallback.onSerialDataParsed(SerialDataParserHelper.parseData(dataBuffer.array()));
                            }
                            if (isOpen) {
                                Log.d(TAG, "【RCSDK】remainBuffer.hasRemaining() " + remainBuffer.hasRemaining() + "capacity:" + remainBuffer.capacity() + ", position: " + remainBuffer.position() + ", limit: " + remainBuffer.limit());
                            }
                            if (remainBuffer.hasRemaining()) {
                                int length = capacity - DEFINE_REPORT_LENGTH - bodyStartPosition;
                                ByteBuffer tempBuffer = ByteBuffer.allocate(length);
                                tempBuffer.put(array, remainBuffer.position(), length);
                                remainBuffer = tempBuffer;
                            } else {
                                remainBuffer = null;
                            }
                        }
                    }
                } catch (Exception e) {
                    remainBuffer = null;
                    e.printStackTrace();
                    Log.d(TAG, "【RCSDK】read exception: " + e.getMessage());
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }


    /**
     * 注册自定义按键
     */
    private void registerKeyListener() {
        customButtonHelper = new ButtonHelper();

        // 采集遥控器按钮通道数据到自定义按钮工具类
        readRCButtonHelper = new ReadRCButtonHelper(RCSDKManager.INSTANCE.getDeviceType());
        readRCButtonHelper.setListener(buttons -> {
            if (customButtonHelper != null) {
                customButtonHelper.receiveButtonData(buttons);
            }
        });
        readRCButtonHelper.start();

        // 遥控器自定义按钮工具类-启用
        customButtonHelper.start();

        // 适用于 H12Pro 的自定义按钮事件
        List<ButtonConfig> customButtonConfigs = new ArrayList<>();
        // 6 通道（H12Pro F拨杆）-自定义1
        customButtonConfigs.add(new ButtonConfig(6, ButtonAction.CUSTOM_0, HandleButtonMode.CHANGE));
        // 7 通道（H12Pro A按钮）-自定义0
        customButtonConfigs.add(new ButtonConfig(7, ButtonAction.CUSTOM_1, HandleButtonMode.CHANGE));
        // 8 通道（H12Pro B按钮）-自定义2
        customButtonConfigs.add(new ButtonConfig(8, ButtonAction.CUSTOM_2, HandleButtonMode.CHANGE));
        // 9 通道（H12Pro B按钮）-自定义3
        customButtonConfigs.add(new ButtonConfig(9, ButtonAction.CUSTOM_3, HandleButtonMode.CHANGE));
        // 10 通道（H12Pro B按钮）-自定义4
        customButtonConfigs.add(new ButtonConfig(10, ButtonAction.CUSTOM_4, HandleButtonMode.CHANGE));
        // 11 通道（H12Pro B按钮）-自定义5
        customButtonConfigs.add(new ButtonConfig(11, ButtonAction.CUSTOM_5, HandleButtonMode.CHANGE));
        // 4 通道（H12Pro B按钮）-自定义6
        customButtonConfigs.add(new ButtonConfig(4, ButtonAction.CUSTOM_6, HandleButtonMode.CHANGE));
        // 5 通道（H12Pro B按钮）-自定义7
        customButtonConfigs.add(new ButtonConfig(5, ButtonAction.CUSTOM_7, HandleButtonMode.CHANGE));

        // 自定义按钮事件处理器
        ButtonHandler buttonHandler = (buttonAction, oldValue, newValue, ints, completionCallback) -> {
            Log.d(TAG, "处理自定义按钮事件4：" + buttonAction + ", " + oldValue + "，" + newValue);
            switch (buttonAction) {
                case CUSTOM_0: // A
                    if (newValue == CHANNEL_HIGH) {
                        Log.d(TAG, "左打药--打开");
                    } else {
                        Log.d(TAG, "左打药--关闭");
                    }
                    break;
                case CUSTOM_1: // B
                    if (newValue == CHANNEL_HIGH) {
                        Log.d(TAG, "右打药--打开");
                    } else {
                        Log.d(TAG, "右打药--关闭");
                    }
                    break;
                case CUSTOM_2: // C
                    if (newValue == CHANNEL_HIGH) {
                        Log.d(TAG, "发动机--打开");
                    } else {
                        Log.d(TAG, "发动机--关闭");
                    }
                    break;
                case CUSTOM_3: // D
                    if (newValue == CHANNEL_HIGH) {
                        Log.d(TAG, "喷药--打开");
                    } else {
                        Log.d(TAG, "喷药--关闭");
                    }
                    break;
                case CUSTOM_4: // G
                    if (oldValue == CHANNEL_MIDDLE) {
                        currentTime = 0;
                    }
                    if ((newValue - CHANNEL_MIDDLE) * 2 < (CHANNEL_LOW - CHANNEL_MIDDLE)) {
                        if (currentTime <= 0) {
                            currentTime = System.currentTimeMillis();
                        }
                        if (System.currentTimeMillis() - currentTime > 1500) {
                            Log.d(TAG, "A/B点切换");
                            currentTime = 0;
                        }
                    } else {
                        currentTime = 0;
                    }
                    break;
                case CUSTOM_5: // H
                    if (oldValue == CHANNEL_MIDDLE) {
                        currentTime = 0;
                    }
                    if ((newValue - CHANNEL_MIDDLE) * 2 > (CHANNEL_HIGH - CHANNEL_MIDDLE)) {
                        if (currentTime <= 0) {
                            currentTime = System.currentTimeMillis();
                        }
                        if (System.currentTimeMillis() - currentTime > 1500) {
                            Log.d(TAG, "遥控/无人切换");
                            currentTime = 0;
                        }
                    } else {
                        currentTime = 0;
                    }
                    break;
                case CUSTOM_6: // E
                    switch (newValue) {
                        case CHANNEL_LOW:
                            Log.d(TAG, "大灯关闭");
                            break;
                        case CHANNEL_MIDDLE:
                            Log.d(TAG, "大灯关闭");
                            break;
                        case CHANNEL_HIGH:
                            Log.d(TAG, "大灯打开");
                            break;
                    }
                    break;
                case CUSTOM_7: // F
                    switch (newValue) {
                        case CHANNEL_LOW:
                            Log.d(TAG, "锁定");
                            break;
                        case CHANNEL_MIDDLE:
                            Log.d(TAG, "解锁");
                            break;
                        case CHANNEL_HIGH:
                            Log.d(TAG, "急停");
                            break;
                    }
                    break;
                default:
                    break;
            }
        };

        // 关联配置与事件处理器
        customButtonHelper.setConfig(customButtonConfigs, buttonHandler);
    }


    /**
     * 回收方法，必须调用
     */
    public void release() {
        isReadingByte = false;
        RCSDKManager.INSTANCE.disconnectRC();
        KeyManager.INSTANCE.cancelListen(keySignalQualityListener);
        Pipeline p = mPipeline;
        if (p != null) {
            PipelineManager.INSTANCE.disconnectPipeline(p);
        }
        if (remainBuffer != null) {
            remainBuffer.clear();
            remainBuffer = null;
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                    if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                        Log.e(TAG, "Scheduler did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        if (readRCButtonHelper != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (readRCButtonHelper != null) {
                        readRCButtonHelper.stop();
                    }
                }
            }).start();
        }
    }
}




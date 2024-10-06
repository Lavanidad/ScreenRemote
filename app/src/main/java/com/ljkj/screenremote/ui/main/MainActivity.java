package com.ljkj.screenremote.ui.main;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.blankj.utilcode.util.ClickUtils;
import com.hjq.toast.Toaster;
import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.lib_common.bean.PathInfoBean;
import com.ljkj.lib_common.bean.PathPointBean;
import com.ljkj.lib_common.bean.WarnDataBean;
import com.ljkj.lib_common.common.Constants;
import com.ljkj.lib_common.event.WSBinaryMessageEvent;
import com.ljkj.lib_common.event.WSJsonMessageEvent;
import com.ljkj.lib_common.event.WSTextMessageEvent;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.boot.BatteryLevelReceiver;

import com.ljkj.lib_common.manager.FileManager;
import com.ljkj.lib_common.manager.TTSManager;
import com.ljkj.lib_common.manager.WebSocketClient;
import com.ljkj.lib_common.utils.CSVUtils;
import com.ljkj.screenremote.R;
import com.ljkj.lib_common.bean.SerialDataBean;
import com.ljkj.screenremote.adapter.WarningAdapter;
import com.ljkj.screenremote.callback.RCDataCallback;
import com.ljkj.screenremote.databinding.ActivityMainBinding;
import com.ljkj.screenremote.manager.ControllerManager;
import com.ljkj.screenremote.manager.FPVPlayerManager;
import com.ljkj.screenremote.manager.MapManager;
import com.ljkj.screenremote.test.FileCreator;
import com.ljkj.screenremote.ui.settings.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO xuan fu
 */
public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> implements MainContract.MainView {

    private final String TAG = MainActivity.class.getSimpleName();

    private ControllerManager controllerManager;
    private FPVPlayerManager fpvPlayerManager;
    private MapManager mapManager;
    private FileManager fileManager;
    private TTSManager ttsManager;

    private MapView mapView;
    private AMap aMap;
    private Marker marker;

    private View floatingView;
    private WarningAdapter warningAdapter;
    private List<WarnDataBean> warnDataBeanList = new ArrayList<>();

    private BatteryLevelReceiver batteryReceiver;
    private List<PathPointBean> pathList = new ArrayList<>();

    private double mlat = 32.81275597;
    private double mlng = 118.7267046;

    private int curDriveMode = Constants.DRIVE_MODE_CONTROL;
    private int curMedical = Constants.TURN_OFF;
    private int curStop = Constants.TURN_OFF;
    private int curEngine = Constants.TURN_OFF;
    private int curLiquid = 1;
    private int curLeftCode = 0;
    private int curRightCode = 0;
    private int curRtkCode = 0;
    private String curPathId = "";

    private WebSocketClient webSocketClient;


    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding = getBinding();

        controllerManager = ControllerManager.getInstance(this);
//        controllerManager.init();

        fpvPlayerManager = FPVPlayerManager.getInstance(binding.fpvWidget, Constants.RTSP);

        fileManager = FileManager.getInstance();
        fileManager.init(this);

        ttsManager = TTSManager.getInstance(this);

        // initFloatingView();
    }


    @Override
    protected void initData() {
        batteryReceiver = new BatteryLevelReceiver();
        registerBroadcast(batteryReceiver);
    }

    @Override
    protected void initListener() {
        controllerManager.setRcSerialDataCallback(new RCDataCallback() {
            @Override
            public void onSerialDataParsed(SerialDataBean data) {
                CSVUtils.writeCsvLogFile(MainActivity.this, data, "testNav");
                if (data.getLat() <= 0 || data.getLng() <= 0) {
                    Log.e(TAG, "非有效经纬度");
                    return;
                }
                mlat = data.getLat();
                mlng = data.getLng();
                LatLng latLng = mapManager.converLatlng(MainActivity.this, new LatLng(mlat, mlng));

                //画当前位置
                drawMark(latLng, data.getAngle());

                //无人情况:绘制轨迹
                if (data.getBroadStatus()[7] == Constants.DRIVE_MODE_UNMANNED) {
                    mapManager.mapToCamera(data.getAngle());
                    mapManager.addPolyline(latLng);
                    mapManager.moveTo(latLng);
                }

                //处理平板传输状态
                dealBroadStatus(data.getBroadStatus(), data.getLeftErrorCode(), data.getRightErrorCode(), data.getRtkStatus(), data.getDiff());
                updateStatusView(data);

                //同步路径
                if (data.getPathId() != null && !curPathId.equals(data.getPathId())) {
                    mPresenter.getPathInfo(Constants.SN, data.getPathId());
                }
            }

            @Override
            public void onSignalQualityData(int signalQuality) {
                binding.tvSignal.setText(signalQuality);
                Log.e(TAG, "setText signal");
            }
        });

        batteryReceiver.setOnBatteryChange(new BatteryLevelReceiver.BatteryCallback() {
            @Override
            public void onBatteryLevelChanged(final String percent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvBattery.setText(percent);
                    }
                });
            }
        });

        binding.ivSwitch.setOnClickListener(v -> fpvPlayerManager.switchVideoMap(mapView));

        binding.ivLocation.setOnClickListener(v -> mapManager.moveTo(mapManager.converLatlng(MainActivity.this, new LatLng(mlat, mlng))));

        binding.ivScaleBig.setOnClickListener(v -> mapManager.scaleLarge());

        binding.ivScaleSmall.setOnClickListener(v -> mapManager.scaleSmall());

        binding.ivSetting.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        binding.ivPing.setOnClickListener(v -> controllerManager.requestPairing());

        binding.ivPath.setOnClickListener(v -> {
            pathList = fileManager.loadLocalPath();
            List<LatLng> latLngList = new ArrayList<>();
            if (pathList != null) {
                for (int i = 0; i < pathList.size(); i++) {
                    latLngList.add(mapManager.converLatlng(MainActivity.this, new LatLng(pathList.get(i).getLat(), pathList.get(i).getLng())));
                }
                mapManager.setPolylineList(latLngList);
                mapManager.moveTo(mapManager.converLatlng(getApplication(), new LatLng(pathList.get(0).getLat(), pathList.get(0).getLng())), 0f);
            }
        });


        //TEST
        String directoryPath = getExternalFilesDir(null).getAbsolutePath();
        String mockFilePath = FileCreator.createMockCsvFile(directoryPath);
        webSocketClient = WebSocketClient.getInstance(Constants.WS_URL);
        webSocketClient.connect();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webSocketClient.sendFileAsJson(mockFilePath);
                JSONObject jsonMessage = new JSONObject();
                try {
                    jsonMessage.put("type", "test");

                    JSONObject dataObject = new JSONObject();
                    dataObject.put("key1", "value1");
                    dataObject.put("key2", 123);
                    dataObject.put("key3", true);
                    jsonMessage.put("data", dataObject);

                    webSocketClient.sendJsonMessage(jsonMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 5000);

    }

    @Override
    protected void initMapView(@Nullable Bundle savedInstanceState) {
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        mapManager = MapManager.getInstance(this);
        mapManager.init(aMap);
        mapManager.locationInTime();
        mapManager.initLocation(this);
    }

    // 订阅文本消息事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketTextMessage(WSTextMessageEvent event) {
        String message = event.getMessage();
        Log.e(TAG, "Received Text:" + event.getMessage());
    }

    // 订阅 JSON 消息事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketJsonMessage(WSJsonMessageEvent event) {
        JSONObject jsonMessage = event.getJsonMessage();
        Log.e(TAG, "Received JSON:" + jsonMessage.toString());

        try {
            String type = jsonMessage.getString("type"); // 获取消息类型

            switch (type) {
                case "messageType1":
                    handleMessageType1(jsonMessage.getJSONObject("data"));
                    break;
                case "messageType2":
                    handleMessageType2(jsonMessage.getJSONObject("data"));
                    break;
                case "messageType3":
                    handleMessageType3(jsonMessage.getJSONObject("data"));
                    break;
                default:
                    Log.e(TAG, "Unknown message type: " + type);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 处理不同消息类型的逻辑
    private void handleMessageType1(JSONObject data) {
        // 处理消息类型1的逻辑
        Log.e(TAG, "Handling message type 1 with data: " + data.toString());
    }

    private void handleMessageType2(JSONObject data) {
        // 处理消息类型2的逻辑
        Log.e(TAG, "Handling message type 2 with data: " + data.toString());
    }

    private void handleMessageType3(JSONObject data) {
        // 处理消息类型3的逻辑
        Log.e(TAG, "Handling message type 3 with data: " + data.toString());
    }

    // 订阅二进制消息事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketBinaryMessage(WSBinaryMessageEvent event) {
        byte[] data = event.getData();
        Log.e(TAG, "Received Binary Data of Length:: " + Arrays.toString(event.getData()));
    }


    @SuppressLint("SetTextI18n")
    private void updateStatusView(SerialDataBean dataBean) {
        if (dataBean.getBroadStatus()[7] == Constants.DRIVE_MODE_CONTROL) {
            binding.tvWorkMode.setText(getString(R.string.control_mode));
        } else {
            binding.tvWorkMode.setText(getString(R.string.unmanned_mode));
        }
        binding.tvDiff.setText(dataBean.getDiff());
        binding.tvDeviceId.setText(getString(R.string.device_id) + dataBean.getDeviceId());
        binding.tvMedicalPre.setText(dataBean.getMedicalPre());
        binding.tvOil.setText(dataBean.getOilCount());
        binding.tvSpeed.setText("" + dataBean.getSpeed());
        binding.tvRemainMedical.setText(dataBean.getLiquidLevelValue());
    }

    private void initFloatingView() {
        floatingView = LayoutInflater.from(this).inflate(R.layout.view_floating_warning, null);
        RecyclerView recyclerView = floatingView.findViewById(R.id.rv_warn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        warningAdapter = new WarningAdapter(this);
        recyclerView.setAdapter(warningAdapter);

        warningAdapter.setList(warnDataBeanList);
        warningAdapter.notify();
    }


    private void registerBroadcast(BatteryLevelReceiver batteryReceiver) {
        Log.e(TAG, "registerBroadcast");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver, filter);
    }

    private void drawMark(LatLng latLng, double angle) {
        if (marker == null) {
            marker = mapManager.addMarkCenter(latLng, R.mipmap.icon_location_marker, (float) (360 - angle));
        } else {
            marker.setPosition(latLng);
            marker.setRotateAngle((float) (360 - angle));
        }
    }

    private void dealBroadStatus(int[] broadStatus, int leftErrorCode, int rightErrorCode, int rtkStatus, int diff) {
        //无人遥控切换时语音播报
        if (curDriveMode != broadStatus[7]) {
            if (broadStatus[7] == Constants.DRIVE_MODE_UNMANNED) {
                if (diff <= 30) {
                    ttsManager.speakText("进入无人");
                } else {
                    ttsManager.speakText("进入无人,横差过大，车辆无法启动");
                }
            } else {
                ttsManager.speakText("进入遥控");
            }
            curDriveMode = broadStatus[7];
        }

        //打药切换时语音播报
        if (curMedical != broadStatus[6]) {
            if (broadStatus[6] == Constants.TURN_ON) {
                ttsManager.speakText("打药开启");
            } else {
                ttsManager.speakText("打药关闭");
            }
            curMedical = broadStatus[6];
        }

        //急停切换时语音播报
        if (curStop != broadStatus[5]) {
            if (broadStatus[5] == Constants.TURN_ON) {
                ttsManager.speakText("急停开启");
            } else {
                ttsManager.speakText("急停关闭");
            }
            curStop = broadStatus[5];
        }

        //发动机切换时语音播报
        if (curEngine != broadStatus[4]) {
            if (broadStatus[4] == Constants.TURN_ON) {
                notifyWarning("发动机异常");
            }
            curEngine = broadStatus[4];
        }
        //电驱不用报 broadStatus[3]

        //低液位过低语音播报
        if (curLiquid != broadStatus[2]) {
            if (broadStatus[2] == Constants.TURN_OFF) {
                notifyWarning("液位过低");
            }
            curLiquid = broadStatus[2];
        }

        //左电机码
        if (curLeftCode != leftErrorCode) {
            if (leftErrorCode > 0) {
                notifyWarning("左电机故障");
            }
            curLeftCode = leftErrorCode;
        }

        if (curRightCode != rightErrorCode) {
            if (rightErrorCode > 0) {
                notifyWarning("右电机故障");
            }
            curRightCode = rightErrorCode;
        }

        //rtk状态
        if (curRtkCode != rtkStatus) {
            if (rtkStatus == 0) {
                notifyWarning("rtk定位异常");
            }
            curRtkCode = rtkStatus;
        }
    }

    private void notifyWarning(String text) {
        ttsManager.speakText("$text,");
        warnDataBeanList.add(new WarnDataBean(text, System.currentTimeMillis()));
        warningAdapter.notifyDataSetChanged();
    }


    @Override
    public void showPathInfo(BaseResponse<PathInfoBean> response) {
        Toaster.show("路径同步成功");
        List<PathPointBean> pathList = new ArrayList<>(CSVUtils.readCsv(new File(response.getErr_data().getCsv_file_path())));
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < pathList.size(); i++) {
            LatLng convertedLatLng = mapManager.converLatlng(MainActivity.this, new LatLng(pathList.get(i).getLat(), pathList.get(i).getLng()));
            latLngs.add(convertedLatLng);
        }
        mapManager.setPolylineList(latLngs);
        mapManager.moveTo(mapManager.converLatlng(MainActivity.this, new LatLng(pathList.get(0).getLat(), pathList.get(0).getLng())), 0f);
    }

    @Override
    public void getPathInfoError(String msg) {
        Toaster.show(msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (controllerManager != null) {
            controllerManager.release();
        }
        if (fpvPlayerManager != null) {
            fpvPlayerManager.release();
        }
        if (mapManager != null) {
            mapManager.release();
        }
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
        if (ttsManager != null) {
            ttsManager.release();
        }
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
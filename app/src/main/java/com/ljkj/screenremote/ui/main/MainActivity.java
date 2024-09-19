package com.ljkj.screenremote.ui.main;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.ClickUtils;
import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.lib_common.bean.SharingPathListBean;
import com.ljkj.lib_common.common.Constants;
import com.ljkj.lib_common.http.api.BaseResponse;
import com.ljkj.lib_common.receiver.BatteryLevelReceiver;

import com.ljkj.screenremote.R;
import com.ljkj.screenremote.bean.SerialDataBean;
import com.ljkj.screenremote.callback.RCDataCallback;
import com.ljkj.screenremote.databinding.ActivityMainBinding;
import com.ljkj.screenremote.manager.ControllerManager;
import com.ljkj.screenremote.manager.FPVPlayerManager;
import com.ljkj.screenremote.manager.MapManager;
import com.ljkj.screenremote.ui.settings.SettingsActivity;


public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> implements MainContract.MainView {

    private final String TAG = MainActivity.class.getSimpleName();

    private ControllerManager controllerManager;
    private FPVPlayerManager fpvPlayerManager;
    private MapManager mapManager;

    private MapView mapView;
    private AMap aMap;

    private BatteryLevelReceiver batteryReceiver;


    double lat = 32.81275597;
    double lnt = 118.7267046;

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
    }

    @Override
    protected void initMapView(@Nullable Bundle savedInstanceState) {
        super.initMapView(savedInstanceState);

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

    @Override
    protected void initData() {
        registerBroadcast(batteryReceiver);


        mPresenter.sendPost2(0 + "", 50 + "", lat + "", lnt + "", -1);
    }

    @Override
    protected void initListener() {
        controllerManager.setRcSerialDataCallback(new RCDataCallback() {
            @Override
            public void onSerialDataParsed(SerialDataBean data) {

            }

            @Override
            public void onSignalQualityData(int signalQuality) {
                binding.tvSignal.setText("signalQuality");
                Log.e(TAG, "setText signal");
            }
        });

        batteryReceiver = new BatteryLevelReceiver(percent -> {
            binding.tvBattery.setText(percent);
            Log.e(TAG, "setText tvBattery");
        });


        binding.ivSwitch.setOnClickListener(v -> fpvPlayerManager.switchVideoMap(mapView));

        binding.ivLocation.setOnClickListener(v -> mapManager.moveTo(mapManager.converLatlng(MainActivity.this, new LatLng(lat, lnt))));

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

    }

    /**
     * 注册广播
     */
    private void registerBroadcast(BatteryLevelReceiver batteryReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryReceiver, filter);
    }


    @Override
    public void showPost(String msg) {

    }

    @Override
    public void showGet(String msg) {
        Log.i("Main", msg);
    }

    @Override
    public void showPost2(BaseResponse<SharingPathListBean> bean) {
        Log.e("Main2", bean.toString());
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Log.i("Main2", errorMsg);

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
    }
}
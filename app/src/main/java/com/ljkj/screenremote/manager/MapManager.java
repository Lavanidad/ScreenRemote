package com.ljkj.screenremote.manager;

/**
 * 作者: fzy
 * 日期: 2024/9/18
 * 描述:
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.hjq.toast.Toaster;
import com.ljkj.screenremote.R;

import java.util.ArrayList;
import java.util.List;


public class MapManager {

    public static final String TAG = MapManager.class.getSimpleName();

    private static MapManager instance;
    private AMap aMap;
    private AMapLocationClient mLocationClient;

    private PolylineOptions polylineOptions;
    private PolylineOptions polylineOptionsList;
    private Polyline polyline;
    private Polyline polylineBlue;
    private Marker markerA;
    private Marker markerB;
    private List<LatLng> points = new ArrayList<>();

    private final Context context;

    private MapManager(Context context) {
        this.context = context;
    }

    public static synchronized MapManager getInstance(Context context) {
        if (instance == null) {
            instance = new MapManager(context);
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(AMap aMap) {
        this.aMap = aMap;
        if (aMap == null) {
            Log.e(TAG, "AMap is null, initialization failed");
            return;
        } else {
            Log.d(TAG, "AMap initialized successfully");
        }
        this.aMap.setTrafficEnabled(true); // 显示实时交通状况
        this.aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        CameraPosition cameraPosition = aMap.getCameraPosition();
        float bearing = 0f; // 地图默认方向

        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, bearing)
        ));
//        Log.i(TAG, "Camera position set to: " + cameraPosition.target.latitude + ", " + cameraPosition.target.longitude);

        // 监控地图转的角度
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (markerA != null) {
                    markerA.setRotateAngle(360 - cameraPosition.bearing);
                }
                if (markerB != null) {
                    markerB.setRotateAngle(360 - cameraPosition.bearing);
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                Log.d(TAG, "Camera change finished at: " + cameraPosition.target.latitude + ", " + cameraPosition.target.longitude);
            }
        });
    }

    /**
     * 实时定位
     * 如需开启：aMap.setMyLocationEnabled(true);
     */
    public void locationInTime() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(1000); // 设置连续定位模式下的定位间隔

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);

        aMap.setMyLocationStyle(myLocationStyle); // 设置定位蓝点的Style

        aMap.setMyLocationEnabled(false); //隐藏定位蓝点并不进行定位

        aMap.getUiSettings().setMyLocationButtonEnabled(false); // 显示默认的定位按钮
        aMap.getUiSettings().setScaleControlsEnabled(false); // 控制比例尺控件是否展示
        aMap.getUiSettings().setZoomControlsEnabled(false); // 设置缩放控件是否显示

        moveToZoomTo(21f);
    }

    public void initLocation(Context context) {
        try {
            mLocationClient = new AMapLocationClient(context);
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (amapLocation != null) {
                        if (amapLocation.getErrorCode() == 0) {
                            Log.e(TAG, "location Success" + amapLocation.getErrorCode() +
                                    ", errInfo:" + amapLocation.getErrorInfo());
                            // 获取当前位置，并将地图的摄像头移到当前位置
//                            LatLng currentLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//                            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f)); // 移动地图到定位点
                        } else {
                            Log.e(TAG, "location Error, ErrCode:" + amapLocation.getErrorCode() +
                                    ", errInfo:" + amapLocation.getErrorInfo());
                        }
                    } else {
                        Log.e(TAG, "AMapLocation is null");
                    }
                }
            });

            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(1000);
            mLocationOption.setOnceLocationLatest(true);
            mLocationClient.setLocationOption(mLocationOption);
//            mLocationClient.startLocation();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to initialize location client: " + e.getMessage());
        }
    }

    /**
     * 设置Mark的icon
     */
    public Marker addMark(LatLng latLng, int res, float angle) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(context.getResources(), res)));
        markerOptions.rotateAngle(angle);
        markerOptions.setFlat(true);
        return aMap.addMarker(markerOptions);
    }

    /**
     * 设置Mark的icon
     */
    public Marker addMarkCenter(LatLng latLng, int res, float angle) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(context.getResources(), res)));
        markerOptions.rotateAngle(angle);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.setFlat(true);
        return aMap.addMarker(markerOptions);
    }

    /**
     * 设置Map地图语言
     */
    public void setMapLanguage(String language) {
        if (aMap != null) {
            aMap.setMapLanguage(language);
        }
    }

    /**
     * 坐标系转换
     *
     * @param context
     * @param sourceLatlng
     * @return
     */
    public LatLng converLatlng(Context context, LatLng sourceLatlng) {
        CoordinateConverter converter = new CoordinateConverter(context);
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatlng);
        return converter.convert();
    }

    public void addPolyline(LatLng latLng) {
        if (polylineOptions == null) {
            polylineOptions = new PolylineOptions();
            polylineOptions.width(20f).color(Color.BLUE);
        }

        // 添加点到已保存的 points 列表
        points.add(latLng);
        polylineOptions.setPoints(points);

        // 移除现有的 Polyline，如果存在
        if (polylineBlue != null) {
            polylineBlue.remove();
            Log.d(TAG, "polylineBlue remove: " + polylineBlue);
        }

        // 在地图上添加新的 Polyline
        polylineBlue = aMap.addPolyline(polylineOptions);
        Log.d(TAG, "addPolyline polylineBlue: " + polylineBlue);
    }

    public void removePolyline() {
        if (polylineBlue != null) {
            polylineBlue.remove();
        }
    }

    public void setPolylineList(List<LatLng> latLngs) {
        // 清空已存在的点
        points.clear();

        // 移除现有的 polylineBlue
        if (polylineBlue != null) {
            polylineBlue.remove();
        }
        Log.i(TAG, "setPolylineList polylineBlue: " + polylineBlue + " remove");

        // 检查是否有足够的点构成路径
        if (latLngs.size() < 2) {
            Toaster.show("非有效路径");
            return;
        }

        // 初始化 polylineOptionsList，如果尚未创建
        if (polylineOptionsList == null) {
            polylineOptionsList = new PolylineOptions();
            polylineOptionsList.width(8f).color(Color.RED);
        }

        // 设置折线的点
        polylineOptionsList.setPoints(latLngs);
        // polylineOptionsList.setDottedLine(true); //虚线
        // polylineOptionsList.geodesic(true); // 大地曲线

        // 移除现有的 polyline，如果存在
        if (polyline != null) {
            polyline.remove();
        }

        // 添加新的折线到地图
        polyline = aMap.addPolyline(polylineOptionsList);

        // 移除现有的标记点A和B
        if (markerA != null) {
            markerA.remove();
        }
        if (markerB != null) {
            markerB.remove();
        }
        // 添加A点标记
        markerA = addMark(latLngs.get(0), R.mipmap.icona, 0f);
        // 添加B点标记
        markerB = addMark(latLngs.get(latLngs.size() - 1), R.mipmap.iconb, 0f);
    }


    // 定位到目标位置
    public void moveTo(LatLng latlng) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(latlng, 15f, 0f, 0f)
        ));
    }

    // 定位到目标位置, 设置偏航角
    public void moveTo(LatLng latlng, float angle) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(latlng, 21f, 0f, angle)
        ));
    }

    // 地图的缩放级别
    public void moveToZoomTo(float zoom) {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    // 参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
    public void moveToCenter() {
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                new CameraPosition(
                        new LatLng(39.993167, 32.330134), // 坐标点
                        10, // 缩放级别
                        0, // 俯仰角度
                        0 // 偏航角度
                ));
        aMap.moveCamera(mCameraUpdate);
    }

    public void scaleLarge() {
        CameraPosition cameraPosition = aMap.getCameraPosition();
        float mapZoom = cameraPosition.zoom;
        LatLng mapTarget = cameraPosition.target;
//        Log.d(TAG, "onClick: large " + cameraPosition.target.latitude + ":" + mapZoom);
        scaleLargeMap(mapTarget, ++mapZoom);
    }

    public void scaleSmall() {
        CameraPosition cameraPosition = aMap.getCameraPosition();
        float mapZoom = cameraPosition.zoom;
        LatLng mapTarget = cameraPosition.target;
//        Log.d(TAG, "onClick: small " + cameraPosition.target.latitude + ":" + mapZoom);
        scaleLargeMap(mapTarget, --mapZoom);
    }

    private void scaleLargeMap(LatLng nowLocation, float scaleValue) {
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nowLocation, scaleValue));
    }

    // 旋转地图
    public void mapToCamera(float angle) {
        // 设置地图状态更新
        CameraUpdate rotateUpdate = CameraUpdateFactory.changeBearing(angle);
        // 更新地图状态
        aMap.animateCamera(rotateUpdate);
    }

    public void release() {
        if (aMap != null) {
            aMap.clear();
        }
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient = null;
        }
    }

}


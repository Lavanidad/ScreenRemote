package com.ljkj.lib_common.bean;

import java.util.Arrays;

/**
 * 作者: fzy
 * 日期: 2024/9/11
 * 描述:
 */
public class SerialDataBean {

    private long timestamp;
    private double latitude;
    private double longitude;
    private float speed;
    private int rtkStatus;
    private int satelliteCount;
    private int liquidLevelValue;
    private int oilCount;
    private int batteryCount;
    private int leftErrorCode;
    private int rightErrorCode;
    /**
     * 主板状态:
     * bit0: 无人/遥控   0->遥控 1->无人
     * bit1: 打药状态
     * bit2: 急停状态
     * bit3: 发动机状态
     * bit4: 电驱状态
     * bit5: 低液位状态
     * bit6: 雷达开启状态
     */
    private int[] broadStatus;
    private int diff;
    private int medicalPre;
    private String deviceId;
    private float angle;
    private String pathId;

    public SerialDataBean(long timestamp, double latitude, double longitude, float speed, int rtkStatus, int satelliteCount, int liquidLevelValue, int oilCount, int batteryCount, int leftErrorCode, int rightErrorCode, int[] broadStatus, int diff, int medicalPre, String deviceId, float angle, String pathId) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.rtkStatus = rtkStatus;
        this.satelliteCount = satelliteCount;
        this.liquidLevelValue = liquidLevelValue;
        this.oilCount = oilCount;
        this.batteryCount = batteryCount;
        this.leftErrorCode = leftErrorCode;
        this.rightErrorCode = rightErrorCode;
        this.broadStatus = broadStatus;
        this.diff = diff;
        this.medicalPre = medicalPre;
        this.deviceId = deviceId;
        this.angle = angle;
        this.pathId = pathId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getLat() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLng() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getRtkStatus() {
        return rtkStatus;
    }

    public void setRtkStatus(int rtkStatus) {
        this.rtkStatus = rtkStatus;
    }

    public int getSatelliteCount() {
        return satelliteCount;
    }

    public void setSatelliteCount(int satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    public int getLiquidLevelValue() {
        return liquidLevelValue;
    }

    public void setLiquidLevelValue(int liquidLevelValue) {
        this.liquidLevelValue = liquidLevelValue;
    }

    public int getOilCount() {
        return oilCount;
    }

    public void setOilCount(int oilCount) {
        this.oilCount = oilCount;
    }

    public int getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(int batteryCount) {
        this.batteryCount = batteryCount;
    }

    public int getLeftErrorCode() {
        return leftErrorCode;
    }

    public void setLeftErrorCode(int leftErrorCode) {
        this.leftErrorCode = leftErrorCode;
    }

    public int getRightErrorCode() {
        return rightErrorCode;
    }

    public void setRightErrorCode(int rightErrorCode) {
        this.rightErrorCode = rightErrorCode;
    }

    public int[] getBroadStatus() {
        return broadStatus;
    }

    public void setBroadStatus(int[] broadStatus) {
        this.broadStatus = broadStatus;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public int getMedicalPre() {
        return medicalPre;
    }

    public void setMedicalPre(int medicalPre) {
        this.medicalPre = medicalPre;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    @Override
    public String toString() {
        return "SerialDataBean{" +
                "timestamp=" + timestamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", rtkStatus=" + rtkStatus +
                ", satelliteCount=" + satelliteCount +
                ", liquidLevelValue=" + liquidLevelValue +
                ", oilCount=" + oilCount +
                ", batteryCount=" + batteryCount +
                ", leftErrorCode=" + leftErrorCode +
                ", rightErrorCode=" + rightErrorCode +
                ", broadStatus=" + Arrays.toString(broadStatus) +
                ", diff=" + diff +
                ", medicalPre=" + medicalPre +
                ", deviceId='" + deviceId + '\'' +
                ", angle=" + angle +
                ", pathId='" + pathId + '\'' +
                '}';
    }
}

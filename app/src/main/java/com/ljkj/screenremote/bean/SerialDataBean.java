package com.ljkj.screenremote.bean;

import java.util.Arrays;

/**
 * 作者: fzy
 * 日期: 2024/9/11
 * 描述:
 */
public class SerialDataBean {

    private final long timestamp;
    private final double latitude;
    private final double longitude;
    private final float speed;
    private final int rtkStatus;
    private final int satelliteCount;
    private final int liquidLevelValue;
    private final int oilCount;
    private final int batteryCount;
    private final int leftErrorCode;
    private final int rightErrorCode;
    private final int[] broadStatus;
    private final int diff;
    private final int medicalPre;
    private final String deviceId;
    private final float angle;
    private String pathId;

    private SerialDataBean(Builder builder) {
        this.timestamp = builder.timestamp;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.speed = builder.speed;
        this.rtkStatus = builder.rtkStatus;
        this.satelliteCount = builder.satelliteCount;
        this.liquidLevelValue = builder.liquidLevelValue;
        this.oilCount = builder.oilCount;
        this.batteryCount = builder.batteryCount;
        this.leftErrorCode = builder.leftErrorCode;
        this.rightErrorCode = builder.rightErrorCode;
        this.broadStatus = builder.broadStatus;
        this.diff = builder.diff;
        this.medicalPre = builder.medicalPre;
        this.deviceId = builder.deviceId;
        this.angle = builder.angle;
        this.pathId = builder.pathId;
    }

    public static class Builder {
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
        private int[] broadStatus;
        private int diff;
        private int medicalPre;
        private String deviceId;
        private float angle;
        private String pathId;

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder setRtkStatus(int rtkStatus) {
            this.rtkStatus = rtkStatus;
            return this;
        }

        public Builder setSatelliteCount(int satelliteCount) {
            this.satelliteCount = satelliteCount;
            return this;
        }

        public Builder setLiquidLevelValue(int liquidLevelValue) {
            this.liquidLevelValue = liquidLevelValue;
            return this;
        }

        public Builder setOilCount(int oilCount) {
            this.oilCount = oilCount;
            return this;
        }

        public Builder setBatteryCount(int batteryCount) {
            this.batteryCount = batteryCount;
            return this;
        }

        public Builder setLeftErrorCode(int leftErrorCode) {
            this.leftErrorCode = leftErrorCode;
            return this;
        }

        public Builder setRightErrorCode(int rightErrorCode) {
            this.rightErrorCode = rightErrorCode;
            return this;
        }

        public Builder setBroadStatus(int[] broadStatus) {
            this.broadStatus = broadStatus;
            return this;
        }

        public Builder setDiff(int diff) {
            this.diff = diff;
            return this;
        }

        public Builder setMedicalPre(int medicalPre) {
            this.medicalPre = medicalPre;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setAngle(float angle) {
            this.angle = angle;
            return this;
        }

        public Builder setPathId(String pathId) {
            this.pathId = pathId;
            return this;
        }

        public SerialDataBean build() {
            return new SerialDataBean(this);
        }
    }

    @Override
    public String toString() {
        return "H12SerialData{" +
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

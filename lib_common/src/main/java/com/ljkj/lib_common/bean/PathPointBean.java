package com.ljkj.lib_common.bean;

/**
 * 作者: fzy
 * 日期: 2024/9/24
 * 描述:
 */
public class PathPointBean {
    private double lat;  //纬
    private double lng;  //经
    private float yaw;  //航向角
    private int point = 0;//是否增加补给点 0否 1是
    private int turn = 0;//0 直线 1入弯 2出弯
    private long time;//当前时间戳，毫秒级
    private float speed;
    private float radius;
    private int id;

    public PathPointBean(double mlat, double mlng, float myaw) {
        this.lat = mlat;
        this.lng = mlng;
        this.yaw = myaw;
    }

    public PathPointBean(double mlat, double mlng, float myaw, int mpoint) {
        this.lat = mlat;
        this.lng = mlng;
        this.yaw = myaw;
        this.point = mpoint;
    }

    public PathPointBean(double mlat, double mlng, float myaw, int mpoint, long mtime) {
        this.lat = mlat;
        this.lng = mlng;
        this.yaw = myaw;
        this.point = mpoint;
        this.time = mtime;
    }

    public PathPointBean(double mlat, double mlng, float myaw, int mpoint, long mtime, float mspeed, float mradius, int mid) {
        this.lat = mlat;
        this.lng = mlng;
        this.yaw = myaw;
        this.point = mpoint;
        this.time = mtime;
        this.speed = mspeed;
        this.radius = mradius;
        this.id = mid;
    }

    public PathPointBean(double mlat, double mlng, float myaw, int mpoint, int mturn) {
        this.lat = mlat;
        this.lng = mlng;
        this.yaw = myaw;
        this.point = mpoint;
        this.turn = mturn;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lon) {
        this.lng = lon;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

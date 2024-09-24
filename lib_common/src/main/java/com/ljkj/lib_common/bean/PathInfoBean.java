package com.ljkj.lib_common.bean;

/**
 * 作者: fzy
 * 日期: 2024/9/24
 * 描述:
 */
public class PathInfoBean {


    private String path_id;
    private String name;
    private String farm_id;
    private String agv_car_id;
    private String csv_file_path;
    private String created_at;
    private int is_upload;
    private double length;

    public String getPath_id() {
        return path_id;
    }

    public void setPath_id(String path_id) {
        this.path_id = path_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getAgv_car_id() {
        return agv_car_id;
    }

    public void setAgv_car_id(String agv_car_id) {
        this.agv_car_id = agv_car_id;
    }

    public String getCsv_file_path() {
        return csv_file_path;
    }

    public void setCsv_file_path(String csv_file_path) {
        this.csv_file_path = csv_file_path;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getIs_upload() {
        return is_upload;
    }

    public void setIs_upload(int is_upload) {
        this.is_upload = is_upload;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "PathInfoBean{" +
                "path_id='" + path_id + '\'' +
                ", name='" + name + '\'' +
                ", farm_id='" + farm_id + '\'' +
                ", agv_car_id='" + agv_car_id + '\'' +
                ", csv_file_path='" + csv_file_path + '\'' +
                ", created_at='" + created_at + '\'' +
                ", is_upload=" + is_upload +
                ", length=" + length +
                '}';
    }
}

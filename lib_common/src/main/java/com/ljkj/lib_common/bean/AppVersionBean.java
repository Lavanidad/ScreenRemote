package com.ljkj.lib_common.bean;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class AppVersionBean {
    /**
     * 版本id
     */
    private String id;

    private String created_at;

    private String updated_at;

    private String ota_id;

    private String delete_dic;

    private String name;

    /**
     * 下载地址
     */
    private String path;

    /**
     * 版本号
     */
    private String version;

    private String is_upgrade;

    private String device_type;

    /**
     * 模块名称
     */
    private String module_name;

    private String modify_content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getOta_id() {
        return ota_id;
    }

    public void setOta_id(String ota_id) {
        this.ota_id = ota_id;
    }

    public String getDelete_dic() {
        return delete_dic;
    }

    public void setDelete_dic(String delete_dic) {
        this.delete_dic = delete_dic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIs_upgrade() {
        return is_upgrade;
    }

    public void setIs_upgrade(String is_upgrade) {
        this.is_upgrade = is_upgrade;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getModify_content() {
        return modify_content;
    }

    public void setModify_content(String modify_content) {
        this.modify_content = modify_content;
    }

    @Override
    public String toString() {
        return "CheckResponse2{" +
                "ID='" + id + '\'' +
                ", CreatedAt='" + created_at + '\'' +
                ", UpdatedAt='" + updated_at + '\'' +
                ", ota_id='" + ota_id + '\'' +
                ", delete_dic='" + delete_dic + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                ", is_upgrade='" + is_upgrade + '\'' +
                ", device_type='" + device_type + '\'' +
                ", module_name='" + module_name + '\'' +
                ", modify_content='" + modify_content + '\'' +
                '}';
    }
}

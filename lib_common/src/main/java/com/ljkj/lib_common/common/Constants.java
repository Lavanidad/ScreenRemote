package com.ljkj.lib_common.common;

import android.os.Environment;

/**
 * 作者: fzy
 * 日期: 2024/9/6
 * 描述：常量
 */
public class Constants {

    //public final static String BASE_URL = "https://www.wanandroid.com/";

    public static final String BASE_URL = "http://47.103.96.152/navigation/";


    public static final String Authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzM1MzQ0MjAsInVzZXJJZCI6IjE2NjYzMTY2ODQ4MTI4NjQyIiwidXNlck5hbWUiOiLnjovogarogaoifQ.B9CFcC2iUuS7YEDKTJjV1giu6SQfr7Yif0aMeXSg4UU";


    public static final String WS_URL = "ws://124.222.224.186:8800";

    //苏宁
    //double lat = 32.6155776;
    //double lnt = 118.0686208;

    public static final String RTSP = "rtsp://192.168.144.108:554/stream=0";

    public static String SN = "0000000000000000";


    //////////////////////////////////// 平板发送的状态
    public static final int DRIVE_MODE_CONTROL = 0;
    public static final int DRIVE_MODE_UNMANNED = 1;
    public static final int TURN_ON = 1;
    public static final int TURN_OFF = 0;

    //////////////////////////////////////////////////

    public static final String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ljkj_remote/";
    public static final String LOCAL_DOWNLOAD_FILE_PATH = LOCAL_FILE_PATH + "download/local/";
    public static final String APK_DOWNLOAD_FILE_PATH = LOCAL_FILE_PATH + "download/apk/";
    public static final String CLOUD_DOWNLOAD_FILE_PATH = LOCAL_FILE_PATH + "download/cloud/";
    public static final String CAN_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/CAN/";
    public static final String WORK_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/ACTION/";
    public static final String NAV_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/NAV/";
    public static final String ZIP_FILE_PATH = LOCAL_FILE_PATH + "ZIP/";
    public static final String MQTT_INFO_FILE_PATH = LOCAL_FILE_PATH + "Log/MQTT/";
    public static final String LOG_FILE_PATH = LOCAL_FILE_PATH + "Log";
    public static final String CRASH_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/CRASH/";
    public static final String OTHER_LOG_FILE_PATH = LOCAL_FILE_PATH + "Log/OTHER/";
    public static final String BLUETOOTH_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bluetooth/";

    //////////////////////////////////////////////////// SP
    public static final String SP_KEY_LANGUAGE = "KEY_LANGUAGE";
    public static final String SP_DEFAULT_LANGUAGE = "FS";


}


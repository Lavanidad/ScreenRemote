package com.ljkj.screenremote.callback;

import com.ljkj.lib_common.bean.SerialDataBean;

/**
 * 作者: fzy
 * 日期: 2024/9/11
 * 描述:
 */
public interface RCDataCallback {
    //串口数据
    void onSerialDataParsed(SerialDataBean data);

    //信号强度
    void onSignalQualityData(int signalQuality);
}


package com.ljkj.screenremote.callback;

import com.ljkj.screenremote.bean.SerialDataBean;

/**
 * 作者: fzy
 * 日期: 2024/9/11
 * 描述:
 */
public interface RCSerialDataCallback {
    void onSerialDataParsed(SerialDataBean data);
}


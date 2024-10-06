package com.ljkj.lib_common.event;

public class WSBinaryMessageEvent {
    private final byte[] data;

    public WSBinaryMessageEvent(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}

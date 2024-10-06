package com.ljkj.lib_common.event;

public class WSTextMessageEvent {
    private final String message;

    public WSTextMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

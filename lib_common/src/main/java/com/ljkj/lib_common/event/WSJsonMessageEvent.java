package com.ljkj.lib_common.event;

import org.json.JSONObject;

public class WSJsonMessageEvent {
    private final JSONObject jsonMessage;

    public WSJsonMessageEvent(JSONObject jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public JSONObject getJsonMessage() {
        return jsonMessage;
    }
}

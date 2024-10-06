package com.ljkj.lib_common.manager;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.ljkj.lib_common.event.WSBinaryMessageEvent;
import com.ljkj.lib_common.event.WSJsonMessageEvent;
import com.ljkj.lib_common.event.WSTextMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient extends WebSocketListener {

    private static final String TAG = "WebSocketClient";
    private static WebSocketClient instance;
    private WebSocket webSocket;
    private String serverUrl;
    private boolean isConnected = false;
    private OkHttpClient client;
    private int reconnectAttempts = 0;
    private Handler reconnectHandler = new Handler();

    // 单例模式
    private WebSocketClient(String serverUrl) {
        this.serverUrl = serverUrl;
        client = new OkHttpClient.Builder()
                .pingInterval(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static WebSocketClient getInstance(String serverUrl) {
        if (instance == null) {
            synchronized (WebSocketClient.class) {
                if (instance == null) {
                    instance = new WebSocketClient(serverUrl);
                }
            }
        }
        return instance;
    }

    public void connect() {
        if (webSocket == null || !isConnected) {
            Request request = new Request.Builder()
                    .url(serverUrl)
                    .addHeader("Authorization", "Bearer token")  // 自定义消息头
                    .build();

            webSocket = client.newWebSocket(request, this);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, okhttp3.Response response) {
        Log.d(TAG, "WebSocket Connected");
        isConnected = true;
        reconnectAttempts = 0;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(TAG, "Received text message: " + text);
        String temp = text.replace("<b>从服务端返回你发的消息：</b>","");
        // 判断消息是否为 JSON 格式
        if (isJsonFormat(temp)) {
            // 如果是 JSON 格式，则解析并发布 JSON 消息事件
            try {
                JSONObject jsonMessage = new JSONObject(temp);
                EventBus.getDefault().post(new WSJsonMessageEvent(jsonMessage));
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse JSON: " + e.getMessage());
            }
        } else {
            // 如果不是 JSON 格式，则发布普通文本消息事件
            EventBus.getDefault().post(new WSTextMessageEvent(temp));
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d(TAG, "Received binary message: " + bytes.hex());
        // 发布二进制消息事件
        EventBus.getDefault().post(new WSBinaryMessageEvent(bytes.toByteArray()));
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d(TAG, "WebSocket Closing: " + reason);
        isConnected = false;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d(TAG, "WebSocket Closed: " + reason);
        isConnected = false;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
        Log.d(TAG, "WebSocket Failure: " + t.getMessage());
        isConnected = false;
        attemptReconnect();
    }

    // 发送文件内容作为 JSON
    public void sendFileAsJson(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();

            String base64FileContent = Base64.encodeToString(fileBytes, Base64.NO_WRAP);

            JSONObject json = new JSONObject();
            json.put("type", "LoadFullPath");
            json.put("data", base64FileContent);
            json.put("total_size", fileBytes.length);

            // 发送 JSON 消息
            sendTextMessage(json.toString());
            Log.d(TAG, "File sent as JSON successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to read or send file as JSON: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Failed to construct JSON object: " + e.getMessage());
        }
    }

    public void sendTextMessage(String message) {
        if (webSocket != null && isConnected) {
            webSocket.send(message);
        } else {
            Log.d(TAG, "Unable to send text message, WebSocket not connected");
        }
    }

    public void sendBinaryMessage(byte[] data) {
        if (webSocket != null && isConnected) {
            webSocket.send(ByteString.of(data));
        } else {
            Log.d(TAG, "Unable to send binary message, WebSocket not connected");
        }
    }

    public void sendJsonMessage(JSONObject jsonMessage) {
        if (webSocket != null && isConnected) {
            String message = jsonMessage.toString();
            webSocket.send(message);
            Log.d(TAG, "Sent JSON message: " + message);
        } else {
            Log.d(TAG, "Unable to send JSON message, WebSocket not connected");
        }
    }

    private void attemptReconnect() {
        if (reconnectAttempts < 5) {
            reconnectAttempts++;
            Log.d(TAG, "Reconnecting... Attempt: " + reconnectAttempts);
            reconnectHandler.postDelayed(() -> connect(), 3000 * reconnectAttempts);
        } else {
            Log.d(TAG, "Reconnect attempts exceeded");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Normal Closure");
            webSocket = null;
        }
    }

    // 判断是否为 JSON 格式的消息
    private boolean isJsonFormat(String message) {
        try {
            new JSONObject(message);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}

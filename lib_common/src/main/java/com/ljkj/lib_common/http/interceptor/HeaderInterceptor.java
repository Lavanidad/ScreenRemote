package com.ljkj.lib_common.http.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ljkj.lib_common.common.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
public class HeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();

        long ts = System.currentTimeMillis();
        int rd = new Random().nextInt(20_000_000 - 10_000_000) + 10_000_000;
        String sign = generateSign(originalRequest, ts, rd);

        requestBuilder.header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .addHeader("Authorization", Constants.Authorization)
                .addHeader("Content-SN", sign)
                .addHeader("Content-TS", String.valueOf(ts))
                .addHeader("Content-RD", String.valueOf(rd));

        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

    private String generateSign(Request request, long ts, int rd) {
        String method = request.method();
        HttpUrl url = request.url();

        // 处理查询参数
        Map<String, String> queryParams = new TreeMap<>();
        for (String query : url.queryParameterNames()) {
            queryParams.put(query, url.queryParameter(query));
        }

        String bodyString = "";
        RequestBody requestBody = request.body();
        if (requestBody != null && (method.equals("POST") || method.equals("PUT") || method.equals("DELETE"))) {
            if (requestBody instanceof MultipartBody) {
                // 处理 MultipartBody
                MultipartBody multipartBody = (MultipartBody) requestBody;
                StringBuilder multipartBodyString = new StringBuilder();
                for (MultipartBody.Part part : multipartBody.parts()) {
                    RequestBody partBody = part.body();
                    try (Buffer buffer = new Buffer()) {
                        partBody.writeTo(buffer);
                        multipartBodyString.append(buffer.readUtf8());
                    } catch (IOException e) {
                        Log.e("HeaderInterceptor", "Error reading multipart body", e);
                    }
                }
                bodyString = multipartBodyString.toString();
            } else {
                // 处理普通 RequestBody
                try (Buffer buffer = new Buffer()) {
                    requestBody.writeTo(buffer);
                    bodyString = buffer.readUtf8();
                } catch (IOException e) {
                    Log.e("HeaderInterceptor", "Error reading request body", e);
                }
            }
        }

        StringBuilder tempStr = new StringBuilder();
        if (method.equals("GET")) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                tempStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            tempStr.append("ts=").append(ts).append("&rd=").append(rd).append("&body=");
        } else {
            tempStr.append("&ts=").append(ts).append("&rd=").append(rd).append("&body=").append(MD5(bodyString));
        }

        return MD5(tempStr.toString());
    }

    private static String MD5(String content) {
        byte[] hash;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(content.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }

}
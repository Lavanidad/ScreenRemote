package com.ljkj.lib_common.http.interceptor;

/*
 * 作者: fzy
 * 日期: 2024/9/10
 * 描述:
 */
import androidx.annotation.NonNull;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class CookieInterceptor implements Interceptor {

    private final CookieJar cookieJar;

    public CookieInterceptor() {
        this.cookieJar = new CookieJar() {
            private final List<Cookie> cookies = new ArrayList<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                this.cookies.clear();
                this.cookies.addAll(cookies);
            }

            @NonNull
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return cookies;
            }
        };
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        List<Cookie> cookies = cookieJar.loadForRequest(request.url());
        if (!cookies.isEmpty()) {
            String cookieHeader = Cookie.parse(request.url(), String.valueOf(cookies.get(0))).toString();
            request = request.newBuilder()
                    .addHeader("Cookie", cookieHeader)
                    .build();
        }

        Response response = chain.proceed(request);

        List<Cookie> receivedCookies = Cookie.parseAll(response.request().url(), response.headers());
        if (!receivedCookies.isEmpty()) {
            cookieJar.saveFromResponse(response.request().url(), receivedCookies);
        }

        return response;
    }
}


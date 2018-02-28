package com.example.app.base.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Package: com.example.app.base.interceptor
 * Class: RequestInterceptor
 * Description: 请求拦截器：可以添加公共的请求参数
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class RequestInterceptor implements Interceptor {

    private final String mApiKey = "111111";

    public RequestInterceptor() {

    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = originalRequest.url()
                .newBuilder()
                .scheme(originalRequest.url().scheme())
                .host(originalRequest.url().host())
                .addQueryParameter("mApiKey", mApiKey);

        // 新的请求
        Request newRequest = originalRequest.newBuilder()
                .method(originalRequest.method(), originalRequest.body())
                .url(authorizedUrlBuilder.build())
                .addHeader("User-Agent","android")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("Cookie", "add cookies here")
                .build();

        return chain.proceed(newRequest);
    }
}

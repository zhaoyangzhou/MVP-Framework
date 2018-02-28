package com.example.app.base.okhttp;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Package: com.example.app.base.okhttp
 * Class: OkHttpInterface
 * Description: OKHttp单例接口
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface OkHttpInterface {
    /**
     * Description: 异步Get请求
     * @param url
     * @param cls
     * @param callback
     */
    void getAsyn(String url, final Class cls, OkHttpCallback callback);

    /**
     * Description: 异步Post请求
     * @param url
     * @param requestBody
     * @param cls
     * @param callback
     */
    void postAsyn(String url, RequestBody requestBody, final Class cls, OkHttpCallback callback);

    /**
     * Description: 同步Get请求
     * @param url
     * @return
     * @throws IOException
     */
    Response get(String url) throws IOException;

    /**
     * Description: 同步Post请求
     * @param url
     * @param requestBody
     * @return
     * @throws IOException
     */
    Response post(String url, RequestBody requestBody) throws IOException;
}

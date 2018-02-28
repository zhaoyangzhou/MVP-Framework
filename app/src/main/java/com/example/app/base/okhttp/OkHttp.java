package com.example.app.base.okhttp;

import com.example.app.AppApplication;
import com.example.app.base.cookie.CookiesManager;
import com.example.app.base.interceptor.CacheInterceptor;
import com.example.app.base.interceptor.GzipRequestInterceptor;
import com.example.app.base.interceptor.RequestInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Package: com.example.app.base.okhttp
 * Class: OkHttp
 * Description: OKHttp单例
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class OkHttp implements OkHttpInterface {
    private static OkHttp instance;
    private static OkHttpClient client;
    /**
     * okHttp time out 定义（单位：秒）
     */
    public static final long OKHTTP_READ_TIME_OUT = 5;
    public static final long OKHTTP_CONNECT_TIME_OUT = 5;
    public static final long OKHTTP_WRITE_TIME_OUT = 5;

    public static OkHttp getInstance() {
        if(instance == null) {
            synchronized (OkHttp.class) {
                if(instance == null) {
                    instance = new OkHttp();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public OkHttp() {
        // Log信息
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        // 拦截器，在请求中增加额外参数
        RequestInterceptor requestInterceptor = new RequestInterceptor();
        // Gzip压缩拦截器
        GzipRequestInterceptor gzipInterceptor = new GzipRequestInterceptor();
        // 缓存拦截器
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        //设置缓存路径
        File httpCacheDirectory = new File(AppApplication.getInstance().getCacheDir(), "responses");
        //设置缓存大小 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        // OkHttp3.0的使用方式
        client = new OkHttpClient.Builder()
                .cookieJar(new CookiesManager())
                .addInterceptor(requestInterceptor)
                .addInterceptor(gzipInterceptor)//开启Gzip压缩
                .addInterceptor(loggingInterceptor)
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .cache(cache)
                .readTimeout(OKHTTP_READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(OKHTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(OKHTTP_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * get请求方式，异步回调方式返回请求数据
     *
     * @param url      请求的URL地址
     * @param cls      javabean(json转成javabean)
     * @param callback 返回请求结果的回调函数
     */
    @Override
    public void getAsyn(String url, final Class cls, final OkHttpCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + ": " + headers.value(i));
                }
                String str = response.body().string();
                callback.onResponse(str);
            }
        });
    }

    /**
     * get请求方式，同步阻塞方式请求数据
     *
     * @param url 请求的URL地址
     */
    public Response get(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return response;
    }

    /**
     * post请求方式，异步回调方式返回请求数据
     *
     * @param url      请求的URL地址
     * @param cls      javabean(json转成javabean)
     * @param callback 返回请求结果的回调函数
     */
    @Override
    public void postAsyn(String url, RequestBody requestBody, final Class cls, final OkHttpCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                callback.onResponse(str);
            }
        });
    }

    /**
     * post请求方式，同步阻塞方式请求数据
     *
     * @param url 请求的URL地址
     */
    @Override
    public Response post(String url, RequestBody requestBody) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return response;
    }
}

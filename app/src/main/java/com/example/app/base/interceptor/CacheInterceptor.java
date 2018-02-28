package com.example.app.base.interceptor;

import com.example.app.AppApplication;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Package: com.example.app.base.interceptor
 * Class: CacheInterceptor
 * Description: 缓存拦截器：有网络时，发起网络请求 无网络时先从缓存中取值，如果缓存过期，则发起网络请求
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class CacheInterceptor implements Interceptor {

    //private int maxAge = 0;// 有网络时 设置缓存超时时间0秒
    private int maxStale = 60 * 60 * 24 * 28;// 无网络时，设置超时为4周

    public CacheInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(!AppApplication.getInstance().isNetConnect){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if(AppApplication.getInstance().isNetConnect){
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }else{
            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}

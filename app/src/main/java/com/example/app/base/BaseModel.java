package com.example.app.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.app.base.okhttp.OkHttp;
import com.example.app.bean.picture.Picture;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Package: com.example.app.base
 * Class: BaseModel
 * Description: Module基类（业务层）
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public abstract class BaseModel {
    private static Retrofit.Builder BUILDER;
    private static OkHttp okHttp;

    protected static Retrofit.Builder getRetrofitBuilder() {
        if (BUILDER == null) {
            // OkHttp3.0的使用方式
            OkHttpClient client = getOkHttp().getClient();

            // 适配器
            BUILDER = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client);
        }
        return BUILDER;
    }

    protected static OkHttp getOkHttp() {
        if (okHttp == null) {
            okHttp = OkHttp.getInstance();
        }
        return okHttp;
    }

    protected Retrofit initRetrofit(String url, Type type) {
        // 对返回的数据进行解析
        Gson gsonInstance = new GsonBuilder()
                .registerTypeAdapter(type,
                        new ResultsDeserializer<Picture>())
                .create();

        // 适配器
        Retrofit mRetrofit = getRetrofitBuilder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gsonInstance))
                .build();
        return mRetrofit;
    }
}

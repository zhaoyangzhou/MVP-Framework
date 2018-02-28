package com.example.app.communication;

import com.example.app.bean.weather.WeatherListData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Package: com.example.app.communication
 * Class: WeatherApi
 * Description: 通信层接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface WeatherApi {
    String URL = "http://api.openweathermap.org";

    /**
     * Description: 获取气象数据
     * @param lat
     * @param lon
     * @param cnt
     * @param appId
     * @return
     */
    @GET("/data/2.5/find")
    Observable<WeatherListData> getWeatherList(@Query("lat") String lat, @Query("lon") String lon, @Query("cnt") String cnt, @Query("APPID") String appId);
}

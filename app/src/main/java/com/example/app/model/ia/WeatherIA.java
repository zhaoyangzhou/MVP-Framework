package com.example.app.model.ia;

import com.example.app.bean.weather.WeatherData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Package: com.example.app.module.main.model.ia
 * Class: WeatherIA
 * Description: 气象业务接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface WeatherIA {
    /**
     * Description: 获取气象数据
     * @param lat
     * @param lon
     * @param cnt
     * @return
     */
    Observable<List<WeatherData>> getServerData(final String lat, final String lon, final String cnt);
}

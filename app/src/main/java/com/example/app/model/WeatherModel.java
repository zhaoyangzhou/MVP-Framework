package com.example.app.model;

import com.google.gson.reflect.TypeToken;
import com.example.app.base.BaseModel;
import com.example.app.bean.weather.WeatherData;
import com.example.app.bean.weather.WeatherListData;
import com.example.app.communication.WeatherApi;
import com.example.app.model.ia.WeatherIA;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;

/**
 * Package: com.example.app.module.main.model
 * Class: WeatherModel
 * Description: 气象业务实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class WeatherModel extends BaseModel implements WeatherIA {
    private final WeatherApi mService;

    public WeatherModel() {
        // 适配器
        Retrofit mRetrofit = initRetrofit(WeatherApi.URL, new TypeToken<WeatherListData>() {}.getType());
        // 服务
        mService = mRetrofit.create(WeatherApi.class);
    }

    public Observable<List<WeatherData>> getServerData(final String lat, final String lon, final String cnt) {
        return mService.getWeatherList(lat, lon, cnt, "82baf3673f8b23cb70d1165d3ce73b9c")
                .flatMap(new Function<WeatherListData, ObservableSource<List<WeatherData>>>() {
                    @Override
                    public ObservableSource<List<WeatherData>> apply(WeatherListData data) {
                        List<WeatherData> list = data.getList();
                        return Observable.just(list);
                    }
                });
    }
}

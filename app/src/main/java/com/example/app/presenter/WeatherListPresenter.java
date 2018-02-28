package com.example.app.presenter;

import com.example.app.base.BaseListViewIA;
import com.example.app.base.BasePresenter;
import com.example.app.bean.weather.WeatherData;
import com.example.app.model.WeatherModel;
import com.example.app.model.ia.WeatherIA;

import java.util.List;

/**
 * Package: com.example.app.module.main.presenter
 * Class: WeatherListPresenter
 * Description: 气象界面控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class WeatherListPresenter extends BasePresenter {
    private BaseListViewIA mWeatherListViewIA;
    private WeatherIA mWeatherIA;
    private static final String LAT = "55.5", LON = "37.5", CNT = "10";
    private int pageNum;

    public WeatherListPresenter(BaseListViewIA mWeatherListViewIA) {
        super(mWeatherListViewIA);
        this.mWeatherListViewIA = mWeatherListViewIA;
        this.mWeatherIA = new WeatherModel();
    }

    public void loadData(final int pageNum) {
        this.pageNum = pageNum;
        doRequest(mWeatherIA.getServerData(LAT, LON, CNT));
    }

    @Override
    protected void onNextImpl(Object data) {
        List<WeatherData> list = (List<WeatherData>) data;
        if (pageNum == 0) {
            mWeatherListViewIA.refresh(list);
        } else {
            mWeatherListViewIA.loadNews(list);
        }
    }

    @Override
    protected void onErrorImpl(Throwable e) {

    }

    @Override
    protected void onCompletedImpl() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWeatherListViewIA = null;
    }
}

package com.example.app.presenter;

import com.example.app.base.BasePresenter;
import com.example.app.model.ChartModel;
import com.example.app.model.ia.ChartIA;
import com.example.app.ui.ia.ChartViewIA;

import java.util.List;

import cn.limc.androidcharts.entity.DateValueEntity;

/**
 * Package: com.example.app.module.user.presenter
 * Class: UserPresenter
 * Description: 用户信息界面控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class ChartPresenter extends BasePresenter {
    private ChartViewIA mChartViewIA;
    private ChartIA mChartIA;

    public ChartPresenter(ChartViewIA mChartViewIA) {
        super(mChartViewIA);
        this.mChartViewIA = mChartViewIA;
        this.mChartIA = new ChartModel();
    }

    public void getData() {
        doRequest(mChartIA.getData());
    }

    @Override
    public void onNextImpl(Object data) {
        List<DateValueEntity> list = (List<DateValueEntity>) data;
        mChartViewIA.updateView(list);
    }

    @Override
    public void onErrorImpl(Throwable e) {

    }

    @Override
    public void onCompletedImpl() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChartViewIA = null;
    }
}

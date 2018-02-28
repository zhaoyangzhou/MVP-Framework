package com.example.app.widget.presenter;

import com.example.app.AppApplication;
import com.example.app.base.BasePresenter;
import com.example.app.bean.picture.Data;
import com.example.app.bean.picture.Picture;
import com.example.app.widget.UILWidgetProvider;
import com.example.app.widget.model.WidgetModel;
import com.example.app.widget.model.ia.WidgetIA;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Package: com.example.app.widget.presenter
 * Class: WidgetPresenter
 * Description: 桌面插件控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class WidgetPresenter extends BasePresenter {
    private UILWidgetProvider widgetProvider;
    private WidgetIA mWidgetIA;
    private int totalNum;

    public WidgetPresenter(UILWidgetProvider widgetProvider) {
        super(widgetProvider);
        this.widgetProvider = widgetProvider;
        this.mWidgetIA = new WidgetModel();
    }

    public void loadData() {
        if (!AppApplication.getInstance().isNetConnect) {
            return;
        }
        doRequest(mWidgetIA.getServerData(0)
                .flatMap(new Function<Picture, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@NonNull Picture picture) {
                        totalNum = picture.getTotalNum();
                        List<String> list = getImageList(picture);
                        return Observable.just(list);
                    }
                }));
    }

    /**
     * 得到网页中图片的地址
     */
    private ArrayList<String> getImageList(Picture picture) {
        ArrayList<String> imgList = new ArrayList<String>();
        List<Data> list = picture.getData();
        for(int i = 0, len = list.size(); i < len; i++) {
            imgList.add(list.get(i).getThumbnail_url());
        }
        return imgList;
    }

    @Override
    protected void onNextImpl(Object data) {
        widgetProvider.getData((ArrayList<String>) data);
    }

    @Override
    protected void onErrorImpl(Throwable e) {

    }

    @Override
    protected void onCompletedImpl() {

    }
}

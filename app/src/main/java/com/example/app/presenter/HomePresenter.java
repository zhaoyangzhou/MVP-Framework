package com.example.app.presenter;

import com.example.app.base.BaseListViewIA;
import com.example.app.base.BasePresenter;
import com.example.app.bean.picture.Data;
import com.example.app.bean.picture.Picture;
import com.example.app.model.ChartModel;
import com.example.app.model.PictureModel;
import com.example.app.model.ia.ChartIA;
import com.example.app.model.ia.PictureIA;
import com.example.app.ui.ia.ChartViewIA;
import com.example.app.ui.ia.HomeViewIA;

import java.util.ArrayList;
import java.util.List;

import cn.limc.androidcharts.entity.DateValueEntity;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Package: com.example.app.module.user.presenter
 * Class: UserPresenter
 * Description: 用户信息界面控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class HomePresenter extends BasePresenter {
    private HomeViewIA mHomeViewIA;
    private PictureIA mPictureIA;
    private int totalNum;
    private int pageNum;

    public HomePresenter(HomeViewIA mHomeViewIA) {
        super(mHomeViewIA);
        this.mHomeViewIA = mHomeViewIA;
        this.mPictureIA = new PictureModel();
    }

    public void loadData(final int pageNum) {
        if(pageNum > 0 && pageNum * PictureModel.ROWNUM >= totalNum) {
            return;
        }
        this.pageNum = pageNum;
        doRequest(mPictureIA.getPictures(pageNum)
                .flatMap(new Function<Picture, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Picture picture) {
                        totalNum = picture.getTotalNum();
                        List<String> list = getImageList(picture);
                        return Observable.just(list);
                    }
                }));
    }

    /**
     * 得到网页中图片的地址
     */
    private List<String> getImageList(Picture picture) {
        List<String> imgList = new ArrayList<String>();
        List<Data> list = picture.getData();
        for(int i = 0, len = list.size() > 4 ? 4 : list.size(); i < len; i++) {
            imgList.add(list.get(i).getThumbnail_url());
        }
        return imgList;
    }

    @Override
    protected void onNextImpl(Object data) {
        List<String> list = (List<String>) data;
        mHomeViewIA.notifyBanner(list);
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
        mHomeViewIA = null;
    }
}

package com.example.app.presenter;

import com.example.app.base.BaseListViewIA;
import com.example.app.base.BasePresenter;
import com.example.app.bean.picture.Data;
import com.example.app.bean.picture.Picture;
import com.example.app.model.PictureModel;
import com.example.app.model.ia.PictureIA;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Package: com.example.app.module.main.presenter
 * Class: WaterfallPresenter
 * Description: 图片界面控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class WaterfallPresenter extends BasePresenter {
    private BaseListViewIA mWaterfallViewIA;
    private PictureIA mPictureIA;
    private int totalNum;
    private int pageNum;

    public WaterfallPresenter(BaseListViewIA mWaterfallViewIA) {
        super(mWaterfallViewIA);
        this.mWaterfallViewIA = mWaterfallViewIA;
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
        for(int i = 0, len = list.size(); i < len; i++) {
            imgList.add(list.get(i).getThumbnail_url());
        }
        return imgList;
    }

    @Override
    protected void onNextImpl(Object data) {
        List<String> list = (List<String>) data;
        if(pageNum == 0) {
            mWaterfallViewIA.refresh(list);
        } else {
            mWaterfallViewIA.loadNews(list);
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
        mWaterfallViewIA = null;
    }
}

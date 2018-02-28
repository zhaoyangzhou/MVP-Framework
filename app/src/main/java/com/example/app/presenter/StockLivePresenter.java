package com.example.app.presenter;

import com.example.app.base.BaseListViewIA;
import com.example.app.base.BasePresenter;
import com.example.app.bean.StockLive;
import com.example.app.model.StockLiveModel;
import com.example.app.model.ia.StockLiveIA;

import java.util.List;

/**
 * Package: com.example.app.module.main.presenter
 * Class: StockLivePresenter
 * Description: 股票界面控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class StockLivePresenter extends BasePresenter {
    private BaseListViewIA mStockLiveViewIA;
    private StockLiveIA mStockLiveIA;
    private int pageNum;

    public StockLivePresenter(BaseListViewIA mStockLiveViewIA) {
        super(mStockLiveViewIA);
        this.mStockLiveViewIA = mStockLiveViewIA;
        this.mStockLiveIA = new StockLiveModel();
    }

    public void loadData(final int pageNum) {
        this.pageNum = pageNum;
        doRequest(mStockLiveIA.getServerData());
    }

    @Override
    protected void onNextImpl(Object data) {
        List<StockLive> list = (List<StockLive>) data;
        if (pageNum == 0) {
            mStockLiveViewIA.refresh(list);
        } else {
            mStockLiveViewIA.loadNews(list);
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
        mStockLiveViewIA = null;
    }
}

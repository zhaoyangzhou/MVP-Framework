package com.example.app.model.ia;

import com.example.app.bean.StockLive;

import java.util.List;

import io.reactivex.Observable;

/**
 * Package: com.example.app.module.main.model.ia
 * Class: StockLiveIA
 * Description: 股票业务接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface StockLiveIA {
    /**
     * Description: 获取股票数据
     * @return
     */
    Observable<List<StockLive>> getServerData();
}

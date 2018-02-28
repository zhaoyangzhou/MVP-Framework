package com.example.app.model;

import com.example.app.base.BaseModel;
import com.example.app.bean.StockLive;
import com.example.app.model.ia.StockLiveIA;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Package: com.example.app.module.main.model
 * Class: StockLiveModel
 * Description: 股票业务实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class StockLiveModel extends BaseModel implements StockLiveIA {

    public StockLiveModel() {

    }

    @Override
    public Observable<List<StockLive>> getServerData() {
        List<StockLive> list = new ArrayList<StockLive>();
        for(int i = 0; i < 30; i++) {
            StockLive bean = new StockLive();
            bean.setCode("600097");
            bean.setName("开创国际");
            bean.setYesterdayEnd("15.370");
            bean.setColor("绿");
            bean.setStart("16.150");
            bean.setHightest("16.280");
            list.add(bean);
        }
        return Observable.just(list);
    }

}

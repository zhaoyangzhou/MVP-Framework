package com.example.app.model.ia;

import java.util.List;

import cn.limc.androidcharts.entity.DateValueEntity;
import io.reactivex.Observable;


/**
 * Package: com.example.app.module.user.model.ia
 * Class: ChartIA
 * Description: 图形数据业务接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/22 9:44
 */
public interface ChartIA {
    /**
     * Description: 获取数据信息
     * @return
     */
    Observable<List<DateValueEntity>> getData();
}

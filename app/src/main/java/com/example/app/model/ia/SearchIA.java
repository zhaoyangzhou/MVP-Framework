package com.example.app.model.ia;

import java.util.List;

import io.reactivex.Observable;


/**
 * Package: com.example.app.module.user.model.ia
 * Class: SearchIA
 * Description: 搜索业务接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface SearchIA {
    /**
     * Description: 搜索
     * @return
     */
    Observable<List<String>> onSearch(String text);
}

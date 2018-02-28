package com.example.app.ui.ia;

import com.example.app.base.BaseViewIA;
import com.example.app.bean.picture.Picture;

import java.util.List;

import cn.limc.androidcharts.entity.DateValueEntity;

/**
 * Package: com.example.app.module.user.view.ia
 * Class: UserViewIA
 * Description: 首页界面接口
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface HomeViewIA extends BaseViewIA {
    /**
     * Description: 更新广告横幅
     * @param list
     */
    void notifyBanner(List<String> list);
}

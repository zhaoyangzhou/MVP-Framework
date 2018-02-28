package com.example.app.widget.model.ia;

import com.example.app.bean.picture.Picture;

import io.reactivex.Observable;

/**
 * Package: com.example.app.widget.model.ia
 * Class: WidgetIA
 * Description: 桌面插件业务接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface WidgetIA {
    /**
     * Description: 获取图片数据
     * @return
     */
    Observable<Picture> getServerData(int pageNum);
}

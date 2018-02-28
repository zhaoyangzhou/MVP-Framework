package com.example.app.model.ia;

import com.example.app.bean.picture.Picture;

import io.reactivex.Observable;


/**
 * Package: com.example.app.module.main.model.ia
 * Class: PictureIA
 * Description: 图片业务接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface PictureIA {
    /**
     * Description: 获取图片数据
     * @param pageNum
     * @return
     */
    Observable<Picture> getPictures(int pageNum);
}

package com.example.app.communication;

import com.example.app.bean.picture.Picture;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Package: com.example.app.communication
 * Class: ImageApi
 * Description: 通信层接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface ImageApi {
    String API_URL = "http://image.baidu.com";

    /**
     * Description: 获取图片数据
     * @param pn
     * @param rn
     * @param tag1
     * @param tag2
     * @param ie
     * @return
     */
    @GET("/channel/listjson")
    Observable<Picture> getData(@Query("pn") int pn, @Query("rn") int rn, @Query("tag1") String tag1, @Query("tag2") String tag2, @Query("ie") String ie);
}

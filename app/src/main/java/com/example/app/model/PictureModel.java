package com.example.app.model;

import com.google.gson.reflect.TypeToken;
import com.example.app.base.BaseModel;
import com.example.app.bean.picture.Picture;
import com.example.app.communication.ImageApi;
import com.example.app.model.ia.PictureIA;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Package: com.example.app.module.main.model
 * Class: PictureModel
 * Description: 图片业务实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class PictureModel extends BaseModel implements PictureIA {
    private ImageApi mService = null;
    public static final int ROWNUM = 30;
    public static int PAGENUM = 0;
    public static int TOTALNUM = 0;

    public PictureModel() {
        // 适配器
        Retrofit mRetrofit = initRetrofit(ImageApi.API_URL, new TypeToken<Picture>() {}.getType());
        // 服务
        mService = mRetrofit.create(ImageApi.class);
    }

    @Override
    public Observable<Picture> getPictures(int pageNum) {
        return mService.getData(pageNum, ROWNUM, "宠物", "全部", "utf8");
    }
}

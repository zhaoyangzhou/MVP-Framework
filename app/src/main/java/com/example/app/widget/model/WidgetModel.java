package com.example.app.widget.model;

import com.google.gson.reflect.TypeToken;
import com.example.app.base.BaseModel;
import com.example.app.bean.picture.Picture;
import com.example.app.communication.ImageApi;
import com.example.app.widget.model.ia.WidgetIA;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Package: com.example.app.widget.model
 * Class: WidgetModel
 * Description: 桌面插件业务实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class WidgetModel extends BaseModel implements WidgetIA {
    private ImageApi mService = null;
    public static final int ROWNUM = 30;

    public WidgetModel() {
        // 适配器
        Retrofit mRetrofit = initRetrofit(ImageApi.API_URL, new TypeToken<Picture>() {}.getType());
        // 服务
        mService = mRetrofit.create(ImageApi.class);
    }

    @Override
    public Observable<Picture> getServerData(int pageNum) {
        return mService.getData(pageNum, ROWNUM, "宠物", "全部", "utf8");
    }
}

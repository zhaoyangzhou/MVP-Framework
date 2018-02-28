package com.example.app.base.okhttp;


import java.io.IOException;

import okhttp3.Call;

/**
 * Package: com.example.app.base.okhttp
 * Class: OkHttpCallback
 * Description: OKHttp回调接口
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface OkHttpCallback {
    /**
     * Description: 失败回调
     * @param call
     * @param e
     */
    void onFailure(Call call, IOException e);

    /**
     * Description: 成功回调
     * @param data
     */
    void onResponse(Object data);

}

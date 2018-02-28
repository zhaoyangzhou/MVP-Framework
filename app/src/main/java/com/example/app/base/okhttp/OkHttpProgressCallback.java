package com.example.app.base.okhttp;


import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Package: com.example.app.base.okhttp
 * Class: OkHttpProgressCallback
 * Description: OKHttp进度条回调接口
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface OkHttpProgressCallback  extends OkHttpCallback {
    /**
     * Description: 开始显示进度
     */
    void onStart();
    /**
     * Description: 响应进度更新
     * @param total
     * @param current
     */
    void onProgress(long total, long current);
}

package com.example.app.base;

import java.util.List;

/**
 * Package: com.example.app.base
 * Class: BaseListViewIA
 * Description: ListView基础接口类（视图层）
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface BaseListViewIA extends BaseViewIA {

    /**
     * Description: 刷新列表
     * @param data
     */
    void refresh(List data);

    /**
     * Description: 加载新数据
     * @param data
     */
    void loadNews(List data);
}

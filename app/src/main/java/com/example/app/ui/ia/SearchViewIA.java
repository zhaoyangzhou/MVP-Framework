package com.example.app.ui.ia;

import com.example.app.base.BaseViewIA;
import com.example.app.bean.User;

import java.util.List;

/**
 * Package: com.example.app.module.user.view.ia
 * Class: SearchViewIA
 * Description: 搜索界面接口
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface SearchViewIA extends BaseViewIA {
    /**
     * Description: 更新界面
     * @param list
     */
    void notifySearch(List<String> list);
}

package com.example.app.ui.ia;

import com.example.app.base.BaseViewIA;
import com.example.app.bean.User;

/**
 * Package: com.example.app.module.login.view.ia
 * Class: LoginViewIA
 * Description: 登录界面接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface LoginViewIA extends BaseViewIA {

    /**
     * Description: 获取用户信息
     * @return
     */
    User getInfo();

    /**
     * Description: 跳转界面
     */
    void toNextView();

}

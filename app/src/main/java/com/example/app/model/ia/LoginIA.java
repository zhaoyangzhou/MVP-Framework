package com.example.app.model.ia;

import com.example.app.bean.User;

import io.reactivex.Observable;

/**
 * Package: com.example.app.module.login.model.ia
 * Class: LoginIA
 * Description: 登录业务接口类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface LoginIA {
    /**
     * Description: 校验用户信息是否合法
     * @param user
     * @return
     */
    Observable<Boolean> validUserInfo(final User user);

    /**
     * Description: 用户信息服务端验证
     * @param email
     * @param password
     * @return
     */
    Observable<Boolean> login(final String email, final String password);
}

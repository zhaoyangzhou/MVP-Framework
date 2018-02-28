package com.example.app.model;

import com.example.app.bean.User;
import com.example.app.model.ia.LoginIA;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Package: com.example.app.module.login.model
 * Class: LoginModel
 * Description: 登录业务实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class LoginModel implements LoginIA {
    public Observable<Boolean> validUserInfo(final User user) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                if(user.getName().length() < 1) {
                    emitter.onNext(false);
                } else if(user.getPassword().length() < 1) {
                    emitter.onNext(false);
                } else {
                    emitter.onNext(true);
                }
                emitter.onComplete();
            }
        });
    }
    public Observable<Boolean> login(final String account, final String password) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                if(password.equals("1")) {
                    emitter.onNext(true);
                } else {
                    emitter.onNext(false);
                }
                emitter.onComplete();
            }
        });
    }
}

package com.example.app.presenter;

import com.example.app.R;
import com.example.app.base.BasePresenter;
import com.example.app.bean.User;
import com.example.app.model.LoginModel;
import com.example.app.model.ia.LoginIA;
import com.example.app.ui.view.activity.LoginActivity;
import com.example.app.ui.ia.LoginViewIA;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Package: com.example.app.module.login.presenter
 * Class: LoginPresenter
 * Description: 登录业务控制层
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class LoginPresenter extends BasePresenter {
    private LoginViewIA mLoginViewIA;
    private LoginIA mLoginIA;

    public LoginPresenter(LoginViewIA mLoginViewIA) {
        super(mLoginViewIA);
        this.mLoginViewIA = mLoginViewIA;
        this.mLoginIA = new LoginModel();
    }

    public void login() {
        final User user = mLoginViewIA.getInfo();
        doRequest(mLoginIA.validUserInfo(user)
                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean isValid) {
                        if (isValid) {
                            return mLoginIA.login(user.getName(), user.getPassword());
                        }
                        return null;
                    }
                }));
    }

    @Override
    public void onNextImpl(Object data) {
        Boolean isValid = (Boolean) data;
        if (isValid != null && isValid) {
            mLoginViewIA.toNextView();
        } else {
            mLoginViewIA.showError(((LoginActivity) mLoginViewIA).getString(R.string.error_invalid_email));
        }
    }

    @Override
    public void onErrorImpl(Throwable e) {

    }

    @Override
    public void onCompletedImpl() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginViewIA = null;
    }
}

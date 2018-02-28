package com.example.app.ui.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.app.R;
import com.example.app.base.BaseActivity;
import com.example.app.bean.User;
import com.example.app.presenter.LoginPresenter;
import com.example.app.ui.ia.LoginViewIA;
import com.zzy.learn.aspectj.annotation.DebugTrace;
import com.zzy.learn.aspectj.annotation.Permission;

/**
 * Package: com.example.app.module.login.view
 * Class: LoginActivity
 * Description: 登录界面
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class LoginActivity extends BaseActivity implements LoginViewIA {
    private Context context;

    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    private View mLoginFormView;

    private LoginPresenter presenter;

    @Override
    protected void onCreateImpl(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        context = this;
        // Set up the login form.
        mAccountView = (AutoCompleteTextView) findViewById(R.id.account);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        Button mAccountSignInButton = (Button) findViewById(R.id.account_sign_in_button);
        mAccountSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doRequest();
            }
        });
        // TODO: 从Cookie中获取登录状态参数，判断是否直接跳转到内容页
    }

    @Override
    protected void initPresenter() {
        presenter = new LoginPresenter(this);
    }

    @Override
    protected void destroyPresenter() {
        presenter.onDestroy();
    }

    @Override
    protected void doRequest() {
        presenter.login();
    }

    @DebugTrace
    @Override
    public User getInfo() {
        User user = new User();
        user.setName(mAccountView.getText().toString());
        user.setPassword(mPasswordView.getText().toString());
        return user;
    }

    @Permission({Manifest.permission.READ_PHONE_STATE})
    @DebugTrace
    @Override
    public void toNextView() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


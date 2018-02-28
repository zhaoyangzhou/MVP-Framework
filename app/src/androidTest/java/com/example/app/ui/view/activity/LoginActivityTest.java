package com.example.app.ui.view.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.app.ui.view.activity.LoginActivity;
import com.robotium.solo.Solo;

/**
 * Package: com.example.app.module.login.view
 * Class: LoginActivity
 * Description: 登录界面
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;//初始化一个solo对象

    public LoginActivityTest() {
        super(LoginActivity.class);
    }
    @Override
    public void setUp() throws Exception {//在测试开始之前会调用这个方法，这里来创建一个Solo对象
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testcase001() throws Exception {
        //等待  Activity　启动
        assertTrue(String.format("无法启动%s", "LoginActivity"), solo.waitForActivity("LoginActivity", 30000));

        solo.clearEditText((EditText)solo.getView("account"));
        solo.clearEditText((EditText)solo.getView("password"));
        solo.enterText((EditText)solo.getView("account"), "1");
        solo.sleep(500);

        solo.enterText((EditText)solo.getView("password"), "1");
        solo.sleep(500);

        solo.clickOnText("登入");
        solo.sleep(1000);
    }

    @Override
    public void tearDown() throws Exception {//一个测试用例结束的时候会调用这个方法
        //solo.finishOpenedActivities();//这个方法将结束掉所有在测试执行过程中打开的activity
    }
}


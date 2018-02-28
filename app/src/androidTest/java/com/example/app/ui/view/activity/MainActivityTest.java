package com.example.app.ui.view.activity;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Package: com.example.app.module.main.view
 * Class: MainActivity
 * Description: 主界面视图
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;//初始化一个solo对象

    public MainActivityTest() {
        super(MainActivity.class);
    }
    @Override
    public void setUp() throws Exception {//在测试开始之前会调用这个方法，这里来创建一个Solo对象
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testcase001() throws Exception {
        //等待  Activity　启动
        assertTrue(String.format("无法启动%s", "MainActivity"), solo.waitForActivity("MainActivity", 30000));

        solo.clickOnText("列表");
        solo.sleep(500);
        solo.scrollDownRecyclerView(10);
        solo.sleep(500);
        solo.clickInRecyclerView(5);
        solo.sleep(3000);

        solo.goBack();
    }

    @Override
    public void tearDown() throws Exception {//一个测试用例结束的时候会调用这个方法
        solo.finishOpenedActivities();//这个方法将结束掉所有在测试执行过程中打开的activity
    }
}


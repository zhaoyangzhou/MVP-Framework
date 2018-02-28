package com.example.app.presenter;


import com.example.app.base.BasePresenter;
import com.example.app.model.MainModel;
import com.example.app.model.ia.MainIA;
import com.example.app.ui.ia.MainViewIA;

/**
 * Package: com.example.app.module.main.presenter
 * Class: MainPresenter
 * Description: 主界面控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class MainPresenter extends BasePresenter {
    private MainViewIA mMainViewIA;
    private MainIA mMainIA;

    public MainPresenter(MainViewIA mMainViewIA) {
        super(mMainViewIA);
        this.mMainViewIA = mMainViewIA;
        this.mMainIA = new MainModel();
    }

    public void getCodelist() {
        mMainIA.getCodelist();
    }

    @Override
    protected void onNextImpl(Object data) {

    }

    @Override
    protected void onErrorImpl(Throwable e) {

    }

    @Override
    protected void onCompletedImpl() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMainViewIA = null;
    }
}

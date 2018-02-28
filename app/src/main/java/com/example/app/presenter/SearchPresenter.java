package com.example.app.presenter;

import com.example.app.base.BasePresenter;
import com.example.app.bean.User;
import com.example.app.model.SearchModel;
import com.example.app.model.ia.SearchIA;
import com.example.app.ui.ia.SearchViewIA;

import java.util.List;

/**
 * Package: com.example.app.module.user.presenter
 * Class: SearchPresenter
 * Description: 搜索界面控制器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class SearchPresenter extends BasePresenter {
    private SearchViewIA mViewIA;
    private SearchIA mSearchIA;

    public SearchPresenter(SearchViewIA mUserViewIA) {
        super(mUserViewIA);
        this.mViewIA = mUserViewIA;
        this.mSearchIA = new SearchModel();
    }

    public void doRequest(String text) {
        doRequest(mSearchIA.onSearch(text));
    }

    @Override
    public void onNextImpl(Object data) {
        mViewIA.notifySearch((List<String>) data);
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
        mViewIA = null;
    }
}

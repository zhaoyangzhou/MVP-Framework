package com.example.app.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Package: com.example.app.base
 * Class: BaseFragment
 * Description: Fragment基类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public abstract class BaseFragment extends Fragment implements BaseViewIA {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = onCreateViewImpl(inflater, container, savedInstanceState);
        doRequest();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyPresenter();
    }
    /**
     * Method: showProgressDialog
     * Description: 显示加载进度
     * @return  void
     */
    @Override
    public void showProgressDialog() {
        if (!isDetached()) {
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }
    /**
     * Method: hideProgressDialog
     * Description: 隐藏加载进度
     * @return  void
     */
    @Override
    public void hideProgressDialog() {
        if (!isDetached()) {
            ((BaseActivity) getActivity()).hideProgressDialog();
        }
    }
    /**
     * Method: showError
     * Description: 显示加载进度
     * @param msg 错误信息
     * @return  void
     */
    @Override
    public void showError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * Method: onCreateViewImpl
     * Description: 初始化界面
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return  View
     */
    protected abstract View onCreateViewImpl(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    /**
     * Method: initPresenter
     * Description: 初始化Presenter
     * @return  void
     */
    protected abstract void initPresenter();
    /**
     * Method: destroyPresenter
     * Description: 销毁Presenter
     * @return  void
     */
    protected abstract void destroyPresenter();
    /**
     * Method: doRequest
     * Description: 发送请求
     * @return  void
     */
    protected abstract void doRequest();
}

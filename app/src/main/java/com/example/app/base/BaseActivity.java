package com.example.app.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;
import com.zzy.learn.aspectj.util.MPermissionUtil;

/**
 * Package: com.example.app.base
 * Class: BaseActivity
 * Description: Activity基类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseViewIA {
    /**
     * 进度条
     */
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateImpl(savedInstanceState);
        initPresenter();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载，请稍后..");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyPresenter();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    /**
     * Method: showProgressDialog
     * Description: 显示加载进度
     * @return  void
     */
    @Override
    public void showProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }
    /**
     * Method: hideProgressDialog
     * Description: 隐藏加载进度
     * @return  void
     */
    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method: onRequestPermissionsResult
     * Description: 授权结果回调方法
     * @param requestCode 请求参数
     * @param permissions 权限列表
     * @param grantResults 授权结果
     * @return  void
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * Method: initActionBar
     * Description: 初始化顶部ActionBar
     * @param title 标题
     * @param displayHomeAsUpEnabled 是否显示返回按钮
     * @return  void
     */
    protected void initActionBar(String title, boolean displayHomeAsUpEnabled) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);//隐藏头部导航按钮
    }

    /**
     * Method: showActionBar
     * Description: 显示顶部ActionBar
     * @param title 标题
     * @return  void
     */
    public void showActionBar(String title) {
        if (getSupportActionBar() != null) {
            TextView textView = (TextView) findViewById(R.id.toolbar_title);
            textView.setText(title);
            getSupportActionBar().show();

            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
            ((CoordinatorLayout.LayoutParams) (findViewById(R.id.container)).getLayoutParams()).topMargin = actionBarHeight;
        }
    }

    /**
     * Method: hideActionBar
     * Description: 隐藏顶部ActionBar
     * @return  void
     */
    public void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            ((CoordinatorLayout.LayoutParams) (findViewById(R.id.container)).getLayoutParams()).topMargin = 0;
        }
    }

    /**
     * Method: onCreateImpl
     * Description: 初始化界面
     * @param savedInstanceState
     * @return  void
     */
    protected abstract void onCreateImpl(Bundle savedInstanceState);
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

package com.example.app.base;

/**
 * Package: com.example.app.base
 * Class: BaseViewIA
 * Description: View基础接口类（视图层）
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public interface BaseViewIA {

    /**
     * 显示加载提示
     */
    void showProgressDialog();

    /**
     * 隐藏加载提示
     */
    void hideProgressDialog();

    /**显示错误信息
     * @param msg
     */
    void showError(String msg);
}

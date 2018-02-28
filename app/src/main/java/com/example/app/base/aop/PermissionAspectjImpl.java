package com.example.app.base.aop;

import android.app.Activity;

import com.zzy.learn.aspectj.aspect.PermissionAspectj;
import com.zzy.learn.aspectj.util.ActivityManager;

import org.aspectj.lang.annotation.Aspect;

import java.lang.ref.WeakReference;

/**
 * Package: com.example.app.base.aop
 * Class: PermissionAspectjImpl
 * Description: 动态权限申请AOP实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/18 12:59
 */
@Aspect
public class PermissionAspectjImpl extends PermissionAspectj {
    /**
     * Method: getCurrentActivity
     * Description: 获取当前显示的Activity引用
     * @return  WeakReference<Activity>
     */
    @Override
    public WeakReference<Activity> getCurrentActivity() {
        return ActivityManager.getInstance().getCurrentActivity();
    }
}

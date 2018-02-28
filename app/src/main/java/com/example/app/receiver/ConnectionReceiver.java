package com.example.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.app.AppApplication;

/**
 * Package: com.example.app.receiver
 * Class: ConnectionReceiver
 * Description: 检测网络连接状态
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class ConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = conn.getActiveNetworkInfo();
            if (info != null) {
                AppApplication.getInstance().isNetConnect = info.isConnected();
            } else {
                AppApplication.getInstance().isNetConnect = false;
            }
        }
    }
}

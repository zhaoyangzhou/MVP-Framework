package com.example.app.base;

import android.os.Environment;

/**
 * Package: com.example.app.base
 * Class: Constants
 * Description: 常量类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class Constants {
	// 接收数据超时时间302秒
	public static final int READ_IDLE_TIME = 302;
	//发送数据超时时间302秒
	public static final int WRITE_IDLE_TIME = 302;
	// 总超时时间300秒
	public static final int ALL_IDLE_TIME = 300;
	// 隔30秒后重连
	public static final int RE_CONN_WAIT_SECONDS = 30;
	//PushService Action
	public static final String PACKAGE = "com.example.app";
	public static final String PUSH_SERVICE_ACTION = "com.example.app.service.intent.PushService";
	
	public static final String IM_IP = "192.168.0.100";
	
	public static final int IM_PORT = 2121;

	public static final String APPDIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ssa/";
}

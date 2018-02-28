package com.example.app.base.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.app.base.Constants;
import com.example.app.service.PushService;

/**
 * Package: com.example.app.base.netty
 * Class: IMUtil
 * Description: 自定义工具类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class IMUtil {
	private static final String TAG = "IMMessage-IMUtil";
	
	public static Channel CHANNEL;

	private static Context APPLICATION;

	public static Context getContext() {
		return APPLICATION;
	}

	/**
	 * <p>
	 * Method ：init
	 * <p>
	 * Description : 方法功能描述
	 * 
	 * @param context
	 * @author 周朝阳-zhaoyangzhou@126.com
	 *         <p>
	 *         --------------------------------------------------------------<br>
	 *         修改履历：<br>
	 *         <li>2015-7-23，zhaoyangzhou@126.com，创建方法；<br>
	 *         --------------------------------------------------------------
	 *         <br>
	 *         </p>
	 */
	public static void init(Context context) {
		APPLICATION = context;
	}

	/**
	 * <p>
	 * Method ：setTagsAndAliasName
	 * <p>
	 * Description : 设置标签并启动推送服务
	 * 
	 * @author 周朝阳-zhaoyangzhou@126.com
	 *         <p>
	 *         --------------------------------------------------------------<br>
	 *         修改履历：<br>
	 *         <li>2015-7-23，zhaoyangzhou@126.com，创建方法；<br>
	 *         --------------------------------------------------------------
	 *         <br>
	 *         </p>
	 */
	public static void startPush() {
		setBooleanSharedPreferences(APPLICATION, "IM", "enablePush", true);
		setStringSharedPreferences(APPLICATION, "IM", "selfDeviceId", getDeviceUUID(APPLICATION));

		Intent intent = new Intent();
		intent.setAction(Constants.PUSH_SERVICE_ACTION);
		intent.setPackage(Constants.PACKAGE);
		APPLICATION.startService(intent);
	}

	/**
	 * <p>
	 * Method ：stopPush
	 * <p>
	 * Description : 停止推送服务
	 * 
	 * @author 周朝阳-zhaoyangzhou@126.com
	 *         <p>
	 *         --------------------------------------------------------------<br>
	 *         修改履历：<br>
	 *         <li>2015-7-23，zhaoyangzhou@126.com，创建方法；<br>
	 *         --------------------------------------------------------------
	 *         <br>
	 *         </p>
	 */
	public static void stopPush() {
		setBooleanSharedPreferences(APPLICATION, "IM", "enablePush", false);
		IMUtil.CHANNEL.writeAndFlush("stop");
		PushService.stop();
		APPLICATION.stopService(new Intent(Constants.PUSH_SERVICE_ACTION));
	}

	/**
	 * <p>
	 * Method ：sendMsg
	 * <p>
	 * Description : 发送消息
	 * 
	 * @param msg
	 *            消息，MessageModel
	 * @author 周朝阳-zhaoyangzhou@126.com
	 *         <p>
	 *         --------------------------------------------------------------<br>
	 *         修改履历：<br>
	 *         <li>2015-7-30，zhaoyangzhou@126.com，创建方法；<br>
	 *         --------------------------------------------------------------
	 *         <br>
	 *         </p>
	 */
	public static <T> void sendMsg(T msg) {
		if(IMUtil.CHANNEL != null){  
			try {
				IMUtil.CHANNEL.writeAndFlush(msg).sync().addListener(new ChannelFutureListener() {
					public void operationComplete(ChannelFuture f)
							throws Exception {
						if (f.isSuccess()) {
							Log.i(TAG, "client send message successful!");
						} else {
							Log.e(TAG, "client send message fail!");
						}
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
        } else {  
            Log.w(TAG, "消息发送失败,连接尚未建立!");  
        }  
	}

	static ParameterizedType type(final Class raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}

	// 以当前时间作为文件名
	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String getMetaValue(String metaName) {
		ApplicationInfo appInfo;
		String metaValue = null;
		try {
			appInfo = APPLICATION.getPackageManager()
					.getApplicationInfo(APPLICATION.getPackageName(),
							PackageManager.GET_META_DATA);
			metaValue = appInfo.metaData.getString(metaName);
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return metaValue;
	}
	
	/**获取设备唯一值*/
	public static String getDeviceUUID(Context context) {

		@SuppressWarnings("static-access")
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, tmPhone, androidId;

		tmDevice = "" + tm.getDeviceId();

		tmSerial = "" + tm.getSimSerialNumber();

		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

		String uniqueId = deviceUuid.toString();

		Log.d("debug", "uuid=" + uniqueId);

		return uniqueId;
	}
	
	//获取String类型的应用变量
	public static String getStringSharedPreferences(Context context,String name,String key,String defaultValue){
		SharedPreferences preferences = context.getSharedPreferences(name, 0);
		return preferences.getString(key, defaultValue == null ? "" : defaultValue);
	}
	
	//获取boolean类型的应用变量
	public static boolean getBooleanSharedPreferences(Context context,String name,String key,boolean defaultValue){
		SharedPreferences preferences = context.getSharedPreferences(name, 0);
		return preferences.getBoolean(key, defaultValue);
	}
	
	//获取int类型的应用变量
	public static int getIntegerSharedPreferences(Context context,String name,String key,int defaultValue){
		SharedPreferences preferences = context.getSharedPreferences(name, 0);
		return preferences.getInt(key, defaultValue);
	}
	
	//设置String类型的应用变量
	public static void setStringSharedPreferences(Context context,String name,String key,String value){
		SharedPreferences preferences = context.getSharedPreferences(name, 0);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(key, value == null ? "" : value);
		editor.commit();
	}
	
	//设置boolean类型的应用变量
	public static void setBooleanSharedPreferences(Context context,String name,String key,boolean value){
		SharedPreferences preferences = context.getSharedPreferences(name, 0);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	//设置int类型的应用变量
	public static void setIntegerSharedPreferences(Context context,String name,String key,int value){
		SharedPreferences preferences = context.getSharedPreferences(name, 0);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

}

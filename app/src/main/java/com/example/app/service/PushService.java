package com.example.app.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.app.base.netty.ClientInitializer;
import com.example.app.base.netty.IMUtil;

/**
 * Package: com.example.app.service
 * Class: PushService
 * Description: 推送服务
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class PushService extends Service {
	private static final String TAG = "IMMessage-PushService";
	
	private static final String SERVERIP = "192.168.0.100";
	private static final int SERVERPORT = 2121;
	// 隔30秒后重连
	public static final int RE_CONN_WAIT_SECONDS = 30;

	/**ScheduledExecutorService executorService: 失去连接时，负责与服务器建立连接的轮询任务*/
	private static ScheduledExecutorService executorService;

	private static Bootstrap bootstrap;

	private static ChannelFuture CHANNEL_FUTURE;

	/**boolean isConnSucc: 是否与推送服务器仍保持连接状态*/
	public static boolean isConnSucc = false;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		boolean enablePush = IMUtil.getBooleanSharedPreferences(getApplicationContext(), "IM", "enablePush", false);
		if(enablePush) {
			// 连接推送 服务器
			connServer();
		}
		
		/*
		 * 这里返回状态有三个值，分别是:
		 * 1、START_STICKY：当服务进程在运行时被杀死，系统将会把它置为started状态，但是不保存其传递的Intent对象
		 * ，之后，系统会尝试重新创建服务;
		 * 2、START_NOT_STICKY：当服务进程在运行时被杀死，并且没有新的Intent对象传递过来的话，
		 * 系统将会把它置为started状态， 但是系统不会重新创建服务，直到startService(Intent intent)方法再次被调用;
		 * 3、START_REDELIVER_INTENT：当服务进程在运行时被杀死，它将会在隔一段时间后自动创建，
		 * 并且最后一个传递的Intent对象将会再次传递过来。
		 */
		return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public static void connServer() {
		bootstrap = new Bootstrap();
		bootstrap
				.group(new NioEventLoopGroup())
				.channel(NioSocketChannel.class)
				.option(ChannelOption.ALLOCATOR,
						UnpooledByteBufAllocator.DEFAULT)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120000)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_REUSEADDR, true)
				.handler(new ClientInitializer());

		if (executorService != null) {
			executorService.shutdown();
		}
		executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				try {
					if (!isConnSucc) {
						// 连接服务端
						if (CHANNEL_FUTURE != null && CHANNEL_FUTURE.channel() != null && CHANNEL_FUTURE.channel().isOpen()) {
							CHANNEL_FUTURE.channel().close();
						}
						
						CHANNEL_FUTURE = bootstrap.connect(SERVERIP, SERVERPORT).sync();
						IMUtil.CHANNEL = CHANNEL_FUTURE.channel();
						//CHANNEL_FUTURE.channel().writeAndFlush("ping");
					}
				} catch (Exception e) {
					Log.e(TAG, "无法与IM服务器建立连接");
					isConnSucc = false;
				} finally {

				}
			}
		}, 0, RE_CONN_WAIT_SECONDS, TimeUnit.SECONDS);
	}

	public static void stop() {
		if (CHANNEL_FUTURE.channel() != null && CHANNEL_FUTURE.channel().isOpen()) {
			CHANNEL_FUTURE.channel().close();
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}

}
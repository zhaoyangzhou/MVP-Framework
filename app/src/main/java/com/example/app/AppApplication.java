package com.example.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.example.app.base.netty.IMUtil;
import com.example.app.receiver.ConnectionReceiver;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.zzy.learn.aspectj.util.ActivityManager;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AppApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static AppApplication CONTEXT;
    /*网络是否连接*/
    public boolean isNetConnect;/*网络连接状态监听*/
    private ConnectionReceiver connectionReceiver = new ConnectionReceiver();
    //APP是否Crash
    public boolean isCrash = false;
    public ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();//单一任务线程池

    public static AppApplication getInstance() {
        return CONTEXT;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Create global configuration and initialize ImageLoader with this config
        CONTEXT = this;
        Future future = AppApplication.getInstance().singleThreadPool.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                initImageLoader(getApplicationContext());
                registerNetReceiver();

                FlowManager.init(CONTEXT);
                IMUtil.init(CONTEXT);
            }
        }));
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            future.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
            future.cancel(true);
        }
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)//配置加载图片的线程数
                .threadPriority(Thread.NORM_PRIORITY - 2)//配置线程的优先级
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheExtraOptions(200, 200)//配置内存缓存图片的尺寸
                .memoryCacheSize(2 * 1024 * 1024)//配置内存缓存的大小
                .discCacheExtraOptions(480, 800, null)//每个缓存文件的最大长宽
                .diskCacheFileCount(50)//配置sdcard缓存文件的数量
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//MD5这种方式生成缓存文件的名字
                .diskCacheSize(50 * 1024 * 1024) // 50 MiB
                .discCache(new UnlimitedDiskCache(new File(Environment.getExternalStorageDirectory()+ "/ssa/fund/imgCache")))// 自定义缓存路径
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(getDisplayOptions())
                .writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    private DisplayImageOptions getDisplayOptions() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_empty)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                //.delayBeforeLoading(int delayInMillis)//delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                .build();// 构建完成
        return options;
    }

    /**
     * 注册网络连接状态监听
     */
    public void registerNetReceiver() {
        IntentFilter connectionIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, connectionIntentFilter);
    }

    /**
     * 注销网络连接状态监听
     */
    public void unregisterNetReceiver() {
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        ActivityManager.getInstance().setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}

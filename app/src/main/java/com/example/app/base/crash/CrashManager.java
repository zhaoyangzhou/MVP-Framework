package com.example.app.base.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.app.AppApplication;
import com.example.app.base.Constants;
import com.example.app.base.util.LogUtil;
import com.example.app.base.util.SysUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by linlong on 2017/8/26.
 */
public class CrashManager implements Thread.UncaughtExceptionHandler {
    // CrashHandler实例
    private static CrashManager instance;
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashManager() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashManager getInstance() {
        CrashManager inst = instance;
        if (inst == null) {
            synchronized (CrashManager.class) {
                inst = instance;
                if (inst == null) {
                    inst = new CrashManager();
                    instance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!AppApplication.getInstance().isCrash) {
            AppApplication.getInstance().isCrash = true;
            if (!handleException(ex) && mDefaultHandler != null) {
                //如果用户没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(getClass().getName(), "error : ", e);
                }
                //退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }

        FutureTask<Throwable> future =  new FutureTask<>(new FutrueCallable(ex));
        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.submit(future);
        try {
            future.get();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(AppApplication.getInstance(), "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.getInstance().e(getClass().getName(), "an error occured when collect package info " + e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                LogUtil.getInstance().e(getClass().getName(), field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogUtil.getInstance().e(getClass().getName(), "an error occured when collect crash info " + e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        String time = formatter.format(new Date());
        long timestamp = System.currentTimeMillis();
        String fileName = "crash-" + time + "-" + timestamp + ".log";
        final String path = Constants.APPDIR + "crash/" + fileName;
        FileOutputStream fos = null;

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        LogUtil.getInstance().e(getClass().getName(), sb.toString());
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                SysUtil.mkDirs(path);
                fos = new FileOutputStream(path);
                fos.write(sb.toString().getBytes());
            }
            return path;
        } catch (Exception e) {
            LogUtil.getInstance().e(getClass().getName(), "an error occured while writing file... " + e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public class FutrueCallable implements Callable<Throwable> {

        private Throwable ex;

        public FutrueCallable(Throwable ex) {
            super();
            this.ex = ex;
        }

        @Override
        public Throwable call() throws Exception {
            //收集设备参数信息
            collectDeviceInfo(AppApplication.getInstance());
            //保存日志文件
            String filePath = saveCrashInfo2File(ex);
            if(!TextUtils.isEmpty(filePath)) {
                //doUploadErrorString(filePath);
            }
            return ex;
        }

    }
}

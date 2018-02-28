package com.example.app.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.app.AppApplication;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * Created by linlong on 2016/10/10.
 */
public class SysUtil {
    /*录音权限状态*/
    public static final int STATE_RECORDING = -11;
    public static final int STATE_NO_PERMISSION = -12;
    public static final int STATE_SUCCESS = 1;

    public static void showSoftInput(WeakReference<Activity> context) {
        if(null == context || null == context.get().getCurrentFocus()) return;
        //显示软键盘的代码
        ((InputMethodManager) context.get().getSystemService(context.get().INPUT_METHOD_SERVICE))
                .showSoftInput(context.get().getCurrentFocus(), 0);
    }

    public static void hideSoftInput(WeakReference<Activity> context) {
        if(null == context || null == context.get().getCurrentFocus()) return;
        //隐藏软键盘的代码
        ((InputMethodManager) context.get().getSystemService(context.get().INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(context.get().getCurrentFocus().getWindowToken(), 0);
        //HIDE_IMPLICIT_ONLY HIDE_NOT_ALWAYS
    }

    public static void hideSoftInput(WeakReference<Activity> context, HideSoftInputCallback hideSoftInputCallback) {
        if(null == context || null == context.get().getCurrentFocus()) return;
        //隐藏软键盘的代码
        ((InputMethodManager) context.get().getSystemService(context.get().INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(context.get().getCurrentFocus().getWindowToken(), 0);
        //HIDE_IMPLICIT_ONLY HIDE_NOT_ALWAYS
        if(hideSoftInputCallback != null)
            hideSoftInputCallback.onComplete();
    }

    private static int getScreenRotation() {
        WindowManager wm = (WindowManager) AppApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        try {
            Method m = display.getClass().getDeclaredMethod("getRotation");
            return (Integer) m.invoke(display);
        } catch (Exception e) {
            return Surface.ROTATION_0;
        }
    }

    private static int getScreenOrientation() {
        switch (getScreenRotation()) {
            case Surface.ROTATION_0:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            case Surface.ROTATION_90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            case Surface.ROTATION_180:
                return (Build.VERSION.SDK_INT >= 8 ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            case Surface.ROTATION_270:
                return (Build.VERSION.SDK_INT >= 8 ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            default:
                return 0;
        }
    }

    /**是否允许屏幕旋转
     * @return
     */
    public static boolean isAllowRotate() {
        //是否允许屏幕旋转
        boolean autoRotateOn = (Settings.System.getInt(AppApplication.getInstance().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) ;
        return autoRotateOn;
    }

    /**
     * 用于检测是否具有录音权限
     *
     * @return
     */
    public static int checkRecordAudioPermission() {
        int minBuffer = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        short[] point = new short[minBuffer];
        int readSize = 0;

        AudioRecord audioRecord = null;
        try {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    (minBuffer * 100));
            // 开始录音
            audioRecord.startRecording();// 检测是否可以进入初始化状态

        } catch (Exception e) {
            if (audioRecord != null) {
                audioRecord.release();
                audioRecord = null;
                LogUtil.getInstance().d("CheckAudioPermission", "无法进入录音初始状态");
            } else {
                Log.i("CheckAudioPermission", "catch, 返回对象非空");
            }
            return STATE_NO_PERMISSION;
        }

        // 检测是否在录音中
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            // 6.0以下机型都会返回此状态，故使用时需要判断bulid版本
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                Log.e("CheckAudioPermission", "无法启动录音, 无法录音");
            }
            return STATE_NO_PERMISSION;

        } else {// 正在录音
            readSize = audioRecord.read(point, 0, point.length);
            // 检测是否可以获取录音结果
            if (readSize <= 0) {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }
                Log.e("CheckAudioPermission", "没有获取到录音数据，无录音权限");
                return STATE_NO_PERMISSION;
            } else {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }
                Log.i("CheckAudioPermission", "获取到录音数据, 有录音权限");
                return STATE_SUCCESS;
            }
        }
    }

    public static void mkDirs(String filesDir) {
        //如果有多级目录，则先创建目录
        String[] dirs = filesDir.split(File.separator);
        for(int i = 0, len = dirs.length - 1; i < len; i++) {
            StringBuffer dir = new StringBuffer();
            for(int j = 0; j <= i; j++) {
                dir.append(dirs[j]).append(File.separator);
            }
            File file = new File(dir.toString());
            if(!file.exists()) {
                file.mkdirs();
            }
        }
    }

    public interface HideSoftInputCallback {
        void onComplete();
    }

    /**获取IMSI 国际移动用户识别码
     * @return
     */
    public static String getIMSI() {
        // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
        // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
        String IMSI =  ((TelephonyManager) AppApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        return IMSI == null ? "" : IMSI;
    }

    /**获取服务提供商名字
     * @return
     */
    public static String getSPN() {
        String SPN = ((TelephonyManager) AppApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE)).getSimOperatorName();
        return SPN;
    }
}

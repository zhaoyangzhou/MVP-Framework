package com.example.app.base.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.app.BuildConfig;
import com.example.app.base.Constants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LogUtil {
	private volatile static LogUtil instance;
	private static Logger LOGGER;

	public static synchronized LogUtil getInstance() {
		if(instance == null) {
			synchronized (LogUtil.class) {
				if(instance == null) {
					instance = new LogUtil();
				}
			}
		}
		return instance;
	}

	public void init() {
		if (LOGGER == null) {
			String dir = Constants.APPDIR + "/log/ssa_tgm.log";
			//如果有多级目录，则先创建目录
			SysUtil.mkDirs(dir);
			final LogConfigurator logConfigurator = new LogConfigurator();
			logConfigurator.setFileName(dir);
			// Set the root log level
			logConfigurator.setRootLevel(Level.DEBUG);
			// Set log level of a specific logger
			logConfigurator.setLevel("org.apache", Level.DEBUG);
			logConfigurator.configure();
			//gLogger = Logger.getLogger(this.getClass());
			LOGGER = Logger.getLogger("SSA");
		}
	}
	public void d(String tag, String content) {
		if(BuildConfig.LOG_OUTPUT) {
			if (!TextUtils.isEmpty(content)) {
				Log.d(tag, content);
				LOGGER.debug(content);
			}
		}
	}

	public void e(String tag, String content) {
		if(BuildConfig.LOG_OUTPUT) {
			if (!TextUtils.isEmpty(content)) {
				Log.d(tag, content);
				LOGGER.error(String.format("%s:%s", tag, content));
			}
		}
	}

}

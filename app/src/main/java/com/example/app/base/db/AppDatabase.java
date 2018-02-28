package com.example.app.base.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Package: com.example.app.base.db
 * Class: AppDatabase
 * Description: DBFlow数据库
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION,
        sqlHelperClass = SQLCipherHelperImpl.class)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "com_linlong_fund_db";
    //数据库版本号
    public static final int VERSION = 1;
}
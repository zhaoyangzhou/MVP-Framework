package com.example.app.base.db;

import com.raizlabs.android.dbflow.DatabaseHelperListener;
import com.raizlabs.android.dbflow.config.BaseDatabaseDefinition;
import com.raizlabs.dbflow.android.sqlcipher.SQLCipherOpenHelper;

/**
 * Package: com.linlong.ssa.platform.db
 * Class: SQLCipherHelperImpl
 * Description:
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2018/1/29 13:09
 */
public class SQLCipherHelperImpl extends SQLCipherOpenHelper {

    public SQLCipherHelperImpl(BaseDatabaseDefinition databaseDefinition, DatabaseHelperListener listener) {
        super(databaseDefinition, listener);
    }

    @Override
    protected String getCipherSecret() {
        return "dbflow-rules";
    }
}

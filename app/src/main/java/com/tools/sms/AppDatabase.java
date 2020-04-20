package com.tools.sms;

/**
 * @author wjb（H）
 * @date describe
 */

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = AppDatabase.name, version = AppDatabase.version)
public class AppDatabase {
    public static final String name = "sms";
    public static final int version = 4;
}

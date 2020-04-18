package com.tools.sms.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tools.sms.MyApp;
import com.tools.sms.db.dbs.DbManager;

/**
 * @author wjb（H）
 * @date describe
 */
public class EndSendIntentService extends IntentService {
    private static final String TAG = EndSendIntentService.class.getSimpleName();

    public EndSendIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            int mainId = intent.getIntExtra("mainId", -100);
            int sucessSize = intent.getIntExtra("sucessSize", 0);
            int failedSize = intent.getIntExtra("failedSize", 0);
            SQLiteDatabase db = DbManager.getInstance(MyApp.getInstance()).getReadableDatabase();
            DbManager.updatedMain(db, mainId, sucessSize, failedSize);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("====>", "onDestroy: 主表更新完成！");

    }
}


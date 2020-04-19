package com.tools.sms.db.dbs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tools.sms.MyApp;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.Main;
import com.tools.sms.bean.SendResultBean;
import com.tools.sms.bean.Template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wjb
 * describe
 */
public class DbManager {
    private static MyDatabaseHelper helper;

    public static MyDatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MyDatabaseHelper(context);
        }
        return helper;
    }

    public static Cursor queryBySQL(SQLiteDatabase db, String sql, String[] selectionArgs) {
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, selectionArgs);
        }
        return cursor;
    }

    public static List<Template> getTemplate(Cursor cursor) {
        List<Template> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String time = cursor.getString(cursor.getColumnIndex(Constants.TIME));
            String content = cursor.getString(cursor.getColumnIndex(Constants.CONTENT));
            int id = cursor.getInt(cursor.getColumnIndex(Constants.ID));
            Template template = new Template();
            template.setContent(content);
            template.setTime(time);
            template.setId(id);
            list.add(template);
        }
        return list;
    }

    public static List<SendResultBean> cursorToDetail(Cursor cursor) {
        List<SendResultBean> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndex(Constants.CONTENT));
                int tag = cursor.getInt(cursor.getColumnIndex(Constants.TAG));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(Constants.PHOTO_NUMBER));
                int mainId = cursor.getInt(cursor.getColumnIndex(Constants.MAIN_ID));
                SendResultBean template = new SendResultBean(phoneNumber, content, tag, mainId);
                list.add(template);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }


    public static List<Main> getMainList(Cursor cursor) {
        List<Main> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Main main = new Main();
            int success_count = cursor.getInt(cursor.getColumnIndex(Constants.SUCESS_SEND));
            int failed_count = cursor.getInt(cursor.getColumnIndex(Constants.FAIED_SEND));
            String time = cursor.getString(cursor.getColumnIndex(Constants.TIME));
            int mainId = cursor.getInt(cursor.getColumnIndex(Constants.MAIN_ID));
            main.setSucess(success_count);
            main.setFailed(failed_count);
            main.setTime(time);
            main.setMainId(mainId);
            list.add(main);
        }
        return list;
    }


    public static void insertSendResult(SQLiteDatabase db, SendResultBean resultBean) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String sql = "insert into senDetail(phoneNumber,content,time,tag,mainId) " +
                "values(?,?,?,?,?)";
        if (db.isOpen()) {
            db.execSQL(sql, new String[]{resultBean.getPhoneNumber(), resultBean.getContent()
                    , dt1.format(date), resultBean.getTag() + "", resultBean.getMianId() + ""});
        }
    }


    public static void insertMain(SQLiteDatabase db, Main main) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String sql = "insert into sendMain(mainId,time) " + "values(?,?)";
        if (db.isOpen()) {
            db.execSQL(sql, new String[]{main.getMainId() + "", dt1.format(date)});
        }
    }

    public static int creatMainId(SQLiteDatabase db) {
        String sql = "select * from " + Constants.TABBLE_MAIN_SEND;
        Cursor cursor = DbManager.queryBySQL(db, sql, null);
        if (cursor == null || cursor.getCount() <= 0) {
            return 1;
        }

        String sql1 = "select mainId from sendMain order by id DESC limit 1";
        Cursor cursor1 = DbManager.queryBySQL(db, sql1, null);
        if (cursor1 == null) {
            return 1;
        }

        if (cursor1.moveToNext()) {
            int mainId = cursor1.getInt(cursor1.getColumnIndex("mainId"));
            return mainId + 1;
        }
        return 1;
    }

    public static void updataSendResult(SQLiteDatabase db, int id) {
        String sql = "update sendresult set tag=? where id=?";
        db.execSQL(sql, new String[]{"success", id + ""});
    }

    public static void updatedMain(SQLiteDatabase db, int mainID, int sucessCount, int failedCount) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String sql_query = "select * from sendMain where mainId=?";
        Cursor cursor = queryBySQL(db, sql_query, new String[]{mainID + ""});
        if (cursor == null) {
            String sql = "insert into sendMain(mainId,time,failed_count,success_count) " + "values(?,?,?,?)";
            db.execSQL(sql, new String[]{mainID + "", dt1.format(date), failedCount + "", sucessCount + ""});
        } else {
            String sql = "update sendMain set success_count=?,failed_count=? where mainId=?";
            db.execSQL(sql, new String[]{sucessCount + "", failedCount + "", mainID + ""});
        }
    }

    public static int getTotalNum(SQLiteDatabase db, String table_name) {
        int count = 0;
        if (db != null) {
            Cursor cursor = db.rawQuery("select * from " + table_name, null);
            count = cursor.getCount();
        }
        return count;
    }


    public static List<SendResultBean> getListByCurrentPage(SQLiteDatabase db, String table_name
            , int currentPage, int pageSize, int mainId) {
        int index = (currentPage - 1) * pageSize; // 获取当前页码第一条数据的下标
        Cursor cursor = null;
        if (db != null) {
            // 两个参数，一个是当前页的第一个数据下标，第二个是当前页的数量
            String sql = "select * from " + table_name + " where mainId='" + mainId + "' limit ?,?";
            cursor = db.rawQuery(sql, new String[]{index + "", pageSize + ""});
            return cursorToDetail(cursor);
        }

        return new ArrayList<>();
    }


    public static void deleteTableAndCreateTable() {
        String mainTable = Constants.TABBLE_MAIN_SEND;
        String detailTable = Constants.TABBLE_RESULT_SEND;
        String TempTable = Constants.TABBLE_NAME_TEMPLATE;
        String[] tables = new String[]{mainTable, detailTable, TempTable};

        SQLiteDatabase writableDatabase = DbManager.getInstance(MyApp.getInstance()).getWritableDatabase();

        writableDatabase.beginTransaction();
        for (int i = 0; i < tables.length; i++) {
            writableDatabase.execSQL("delete from " + tables[i]);
        }
//        String sql_1 = "create table " + Constants.TABBLE_NAME_TEMPLATE + "(" + Constants.ID
//                + " integer primary key autoincrement," + Constants.CONTENT + "," + Constants.TIME + ")";
//        String sql_2 = "create table " + Constants.TABBLE_RESULT_SEND +
//                "(" + Constants.ID + " integer primary key autoincrement," + Constants.CONTENT + "," + Constants.MAIN_ID + ","
//                + Constants.TIME + "," + Constants.TAG + "," + Constants.PHOTO_NUMBER + ")";
//
//        String sql_3 = "create table " + Constants.TABBLE_MAIN_SEND + "(" + Constants.ID
//                + " integer primary key autoincrement," + Constants.FAIED_SEND + "," + Constants.MAIN_ID + ","
//                + Constants.SUCESS_SEND + "," + Constants.TIME + ")";
//
//
//        writableDatabase.execSQL(sql_1);
//        writableDatabase.execSQL(sql_2);
//        writableDatabase.execSQL(sql_3);
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
    }


}

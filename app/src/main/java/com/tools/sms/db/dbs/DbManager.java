package com.tools.sms.db.dbs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tools.sms.base.Constants;
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

    public static List<SendResultBean> getSendResultList(Cursor cursor) {
        List<SendResultBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndex(Constants.CONTENT));
            int id = cursor.getInt(cursor.getColumnIndex(Constants.ID));
            String tag = cursor.getString(cursor.getColumnIndex(Constants.TAG));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(Constants.PHOTO_NUMBER));
            SendResultBean template = new SendResultBean(phoneNumber, content, tag);
            template.setId(id);
            list.add(template);
        }
        return list;
    }

    public static void insertSendResult(SQLiteDatabase db, SendResultBean resultBean) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/M/Mdd");
        Date date = new Date();
        String sql = "insert into sendresult(phoneNumber,content,time,tag) " +
                "values(?,?,?,?)";
        if (db.isOpen()) {
            db.execSQL(sql, new String[]{resultBean.getPhoneNumber(), resultBean.getContent(), dt1.format(date), resultBean.getTag()});
        }
    }

    public static void updataSendResult(SQLiteDatabase db, int id) {
        String sql = "update sendresult set tag=? where id=?";
        db.execSQL(sql, new String[]{"success", id + ""});
    }


    public static int getTotalNum(SQLiteDatabase db, String table_name) {
        int count = 0;
        if (db != null) {
            Cursor cursor = db.rawQuery("select * from " + table_name, null);
            count = cursor.getCount();
        }
        return count;
    }

//    //当前页码数据的集合
//    public static List<Person> getListByCurrentPage(SQLiteDatabase db, String table_name, int currentPage, int pageSize) {
//        int index = (currentPage - 1) * pageSize; // 获取当前页码第一条数据的下标
//        Cursor cursor = null;
//        if (db != null) {
//            String sql = "select * from " + table_name + " limit ?,?";  // 两个参数，一个是当前页的第一个数据下标，第二个是当前页的数量
//            cursor = db.rawQuery(sql, new String[]{index + "", pageSize + ""});
//        }
//        return cursorToPerson(cursor);
//    }

}

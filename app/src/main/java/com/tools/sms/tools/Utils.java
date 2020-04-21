package com.tools.sms.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tools.sms.base.Constants.FILE_SELECTOR_CODE;

/**
 * @author wjb
 * describe
 */
public class Utils {

  public static void openFileSelector(Activity context) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("text/plain");
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    context.startActivityForResult(intent, FILE_SELECTOR_CODE);
  }

  public static Spanned htmlTextUtils(String st) {
    // String st = "TextView内的文本:<u>下划线</u> <i>斜体字</i> <font color='#ff0000'>设置字体颜色为红色</font><strong>加粗</strong> ";
    Spanned result;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      //androidN+之后废除了Html.fromHtml(String),用Html.fromHtml(String,flag)代替
      result = Html.fromHtml(st, Html.FROM_HTML_MODE_LEGACY);
    } else {
      result = Html.fromHtml(st);
    }
    return result;
  }


  public static String Date2TimeStamp(String dateStr, String format) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }


  //计算两个时间戳间隔多少天
  public static int equation(long startTime, long endTime) {
    startTime = dateToStamp(stampToDate(startTime));
    endTime = dateToStamp(stampToDate(endTime));
    int newL = (int) ((endTime - startTime) / (1000 * 3600 * 24));
    return newL;

  }

  /*
   * 将时间戳转换为时间
   */
  public static String stampToDate(long l) {
    String res;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    long lt = l;
    Date date = new Date(lt);
    res = simpleDateFormat.format(date);
    return res;
  }

  /*
   * 将时间转换为时间戳
   */
  public static long dateToStamp(String s) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = simpleDateFormat.parse(s);
      return date.getTime();
    } catch (java.text.ParseException e) {
      // TODO Auto-generated catch block

      e.printStackTrace();
      return -1;
    }

  }


  public static int caculateTotalTime(String startTime, String endTime) {
    if (startTime == null || endTime == null) {
      return 0;
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = null;
    Date date = null;
    Long l = 0L;
    try {
      date = formatter.parse(startTime);
      long ts = date.getTime();
      date1 = formatter.parse(endTime);
      long ts1 = date1.getTime();

      l = (ts - ts1) / (1000 * 60 * 60 * 24);

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return l.intValue();
  }

  public static int getLocalVersion(Context ctx) {
    int localVersion = 0;
    try {
      PackageInfo packageInfo = ctx.getApplicationContext()
              .getPackageManager()
              .getPackageInfo(ctx.getPackageName(), 0);
      localVersion = packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return localVersion;
  }

  public static String getLocalVersionName(Context ctx) {
    String localVersion = "";
    try {
      PackageInfo packageInfo = ctx.getApplicationContext()
              .getPackageManager()
              .getPackageInfo(ctx.getPackageName(), 0);
      localVersion = packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return localVersion;
  }


}
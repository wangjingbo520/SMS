package com.tools.sms.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.tools.sms.bean.XLSUserBean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author wjb
 * describe
 */
public class FileUtils {

  public static String getRealFilePath(final Context context, final Uri uri) {
    if (null == uri) return null;
    final String scheme = uri.getScheme();
    String data = null;
    if (scheme == null)
      data = uri.getPath();
    else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
      data = uri.getPath();
    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
      Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
      if (null != cursor) {
        if (cursor.moveToFirst()) {
          int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
          if (index > -1) {
            data = cursor.getString(index);
          }
        }
        cursor.close();
      }
    }
    return data;
  }


  public static ArrayList<String> readFile(String path) throws IOException {
    ArrayList<String> list = new ArrayList<>();
    FileInputStream fis = new FileInputStream(path);
    InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
    BufferedReader br = new BufferedReader(isr);
    String line = "";
    while ((line = br.readLine()) != null) {
      //  if (line.lastIndexOf("---") < 0) {
      list.add(line);
      //  }
    }
    br.close();
    isr.close();
    fis.close();
    return list;
  }


  public static String getPath(Context context, Uri uri) {
    if ("content".equalsIgnoreCase(uri.getScheme())) {
      String[] projection = {"_data"};
      Cursor cursor = null;
      try {
        cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        if (cursor.moveToFirst()) {
          return cursor.getString(column_index);
        }
      } catch (Exception e) {
        // Eat it
      }
    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }


  public static ArrayList<XLSUserBean> getDataFromXlsFile(String path) {
    ArrayList<XLSUserBean> beans = new ArrayList<>();
    Workbook readwb = null;
    try {
      InputStream instream = new FileInputStream(path);
      try {
        readwb = Workbook.getWorkbook(instream);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (BiffException e) {
        e.printStackTrace();
      }

      //Sheet的下标是从0开始
      //获取第一张Sheet表
      Sheet readsheet = readwb.getSheet(0);
      //获取Sheet表中所包含的总行数
      int rsRows = readsheet.getRows();
      //列数
      int column = readsheet.getColumns();
      Log.e("列数", "getDataFromXlsFile: " + column);
      //获取指定单元格的对象引用
      for (int j = 0; j < rsRows; j++) {
        XLSUserBean XLSUserBean = new XLSUserBean();
        //遍历列数，从第一列开始
        for (int i = 0; i < column; i++) {
          Cell cell = readsheet.getCell(i, j);
          //第一列
          String content = cell.getContents();
          if (i == 0) {
            if (!TextUtils.isEmpty(content)) {
              XLSUserBean.setXlsOne(content);
            } else {
              XLSUserBean.setXlsOne("扩展字段1(异常)");
            }
          } else if (i == 1) {
            if (!TextUtils.isEmpty(content)) {
              XLSUserBean.setXlsTwo(content);
            } else {
              XLSUserBean.setXlsTwo("扩展字段2(异常)");
            }

          } else if (i == 2) {
            if (!TextUtils.isEmpty(content)) {
              XLSUserBean.setXlsThree(content);
            } else {
              XLSUserBean.setXlsThree("扩展字段3(异常)");
            }

          } else if (i == 3) {
            if (!TextUtils.isEmpty(content)) {
              XLSUserBean.setXlsFour(content);
            } else {
              XLSUserBean.setXlsFour("扩展字段4(异常)");
            }

          } else if (i == 4) {
            if (!TextUtils.isEmpty(content)) {
              XLSUserBean.setXlsFive(content);
            } else {
              XLSUserBean.setXlsFive("扩展字段5(异常)");
            }
          }

        }

        Cell cell = readsheet.getCell(0, j);
        String phone = cell.getContents();
        String regExp = "^[0-9]{11}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(cell.getContents());
        if (m.find()) {
          XLSUserBean.setPhoneNumber(phone);
        }
        beans.add(XLSUserBean);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return beans;
  }


}
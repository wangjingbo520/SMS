package com.tools.sms;

import android.app.Activity;
import android.app.Application;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tools.sms.base.Constants;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 * @author wjb
 * describe
 */
public class MyApp extends Application {
    public static MyApp myApp;
    List<Activity> activityList = new LinkedList();//Activity 容器

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public static int openning = 0;


    public static void setOpenning(int openning) {
        MyApp.openning = openning;
    }

    public static int getOpenning() {
        return openning;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;

        api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID, false);
        api.registerApp(Constants.WEIXIN_APPID);

        SPUtils.getInstance().put(Constants.TIEM_INTERVAL, 5);

    }


    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public static MyApp getInstance() {
        return myApp;
    }


}

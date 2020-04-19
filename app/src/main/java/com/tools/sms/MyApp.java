package com.tools.sms;

import android.app.Activity;
import android.app.Application;

import com.tools.sms.base.Constants;
import com.tools.sms.tools.SPUtils;

import java.util.LinkedList;
import java.util.List;


/**
 * @author wjb
 * describe
 */
public class MyApp extends Application {
    public static MyApp myApp;
    List<Activity> activityList = new LinkedList();//Activity 容器

    public static int openning;


    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;

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

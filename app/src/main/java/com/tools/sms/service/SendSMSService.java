package com.tools.sms.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.tools.sms.R;
import com.tools.sms.activity.ExcellSendActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.UserBean;
import com.tools.sms.bean.XLSUserBean;
import com.tools.sms.tools.SPUtils;

import java.util.ArrayList;

/**
 * @author wjb
 * describe
 */
public class SendSMSService extends Service {

    private boolean isStopService = false;

    private ArrayList<XLSUserBean> xlsUserBeans = new ArrayList<>();

    private SmsThread smsThread;

    private String msg = "";

    private static Object lock = new Object();

    //暂停
    public static boolean pause = false;

    private Intent pause_intent;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent newIntent = new Intent(this, ExcellSendActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, newIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.fasong))
                .setContentTitle("正在发送")
                .setSmallIcon(R.mipmap.fasong)
                .setContentText("短信正在发送中...")
                .setWhen(System.currentTimeMillis());

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(110, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getExtras() != null) {
                smsThread = new SmsThread();
                Bundle bundle = intent.getExtras();
                ArrayList<XLSUserBean> beans = (ArrayList<XLSUserBean>) bundle.getSerializable("beans");
                msg = bundle.getString("content", "未知短信内容");
                if (beans != null) {
                    xlsUserBeans.addAll(beans);
                    pause_intent = new Intent();
                    pause_intent.setAction("pause");
                    smsThread.start();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private class SmsThread extends Thread {

        @Override
        public void run() {
            SmsManager sms = SmsManager.getDefault();
            for (int i = 0; i < xlsUserBeans.size(); i++) {
                if (isStopService) {
                    //退出服务，退出页面
                    break;
                }

                while (pause) {
                    sendBroadcast(pause_intent);
                    onPause();
                }

                String result = msg;
                if (msg.contains("{|e1|}")) {
                    result = result.replace("{|e1|}", xlsUserBeans.get(i).getXlsOne());
                }

                if (msg.contains("{|e2|}")) {
                    result = result.replace("{|e2|}", xlsUserBeans.get(i).getXlsTwo());
                }

                if (msg.contains("{|e3|}")) {
                    result = result.replace("{|e3|}", xlsUserBeans.get(i).getXlsThree());

                }
                if (msg.contains("{|e4|}")) {
                    result = result.replace("{|e4|}", xlsUserBeans.get(i).getXlsFour());

                }
                if (msg.contains("{|e5|}")) {
                    result = result.replace("{|e5|}", xlsUserBeans.get(i).getXlsFive());

                }


                String number = xlsUserBeans.get(i).getPhoneNumber();
                Intent sendIntent = new Intent(
                        AppConstants.ACTION_SMS_SEND_ACTIOIN);
                sendIntent.putExtra("number", number);
                sendIntent.putExtra("numberIndex", i + 1);
                sendIntent.putExtra("content", result);

                PendingIntent sendPendingIntent = PendingIntent
                        .getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(),
                                sendIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                Intent deliveredIntent = new Intent(
                        AppConstants.ACTION_SMS_DELIVERED_ACTION);
                deliveredIntent.putExtra("number", number);
                PendingIntent deliveredPendingIntent = PendingIntent
                        .getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(),
                                deliveredIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    int stopTime = SPUtils.getInstance().getInt(Constants.TIEM_INTERVAL);
                    Log.e("发送间隔是：", "run: " + stopTime);
                    //第一条短信的时候暂停
                    if (xlsUserBeans.size() > 1 && i == 0) {
                        //sleep(1000);
                    } else {
                        sleep(stopTime * 1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                ArrayList<String> msgs = sms.divideMessage(result);
                for (String msg : msgs) {
                    sms.sendTextMessage(number, null, msg,
                            sendPendingIntent, deliveredPendingIntent);
                }
                Intent intent = new Intent();
                //默认姓名在第一列
                intent.putExtra("number", xlsUserBeans.get(i).getPhoneNumber());
                //默认姓名在第二列
                intent.putExtra("name", xlsUserBeans.get(i).getXlsTwo());
                intent.setAction(AppConstants.ACTION_SMS_SEND_NUMBER);
                sendBroadcast(intent);
            }
            super.run();
        }
    }


    void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //暂停发送
    public static void pauseThread() {
        pause = true;
    }

    //恢复发送
    public static void resumeThread() {
        pause = false;
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStopService = true;
        stopForeground(true);
    }

}

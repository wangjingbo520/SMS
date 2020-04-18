package com.tools.sms.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tools.sms.R;
import com.tools.sms.bean.Main;
import com.tools.sms.bean.SendResultBean;
import com.tools.sms.service.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wjb（C）
 * describe
 */
public class SendResultAdapter extends BaseAdapter {
    private List<Main> list;

    private Activity context;

    public int index = -1;

    public SendResultAdapter(Activity context, List<Main> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Main getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = View.inflate(context, R.layout.listview_item_senr_result, null);
            holder.sucess = arg1.findViewById(R.id.sucess);
            holder.failed = arg1.findViewById(R.id.failed);
            holder.time = arg1.findViewById(R.id.time);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }

        holder.sucess.setText("发送成功：" + getItem(arg0).getSucess());
        holder.failed.setText("发送失败：" + getItem(arg0).getFailed());
        holder.time.setText("时间：" + getItem(arg0).getTime());


//        if (list.get(arg0).getTag().equals("sucess")) {
//            holder.tvResult.setText("成功");
//            holder.tvResult.setBackground(context.getResources().getDrawable(R.drawable.btn_shape2));
//            holder.tvResult.setClickable(false);
//        } else {
//            holder.tvResult.setBackground(context.getResources().getDrawable(R.drawable.btn_shape));
//            holder.tvResult.setText("失败重发");
//            holder.tvResult.setClickable(true);
//            holder.tvResult.setOnClickListener(v -> {
//                //失败重发
//                this.index = arg0;
//                SmsManager sms = SmsManager.getDefault();
//                Intent sendIntent = new Intent(
//                        AppConstants.ACTION_SMS_SEND_ACTIOIN);
//                sendIntent.putExtra("number", list.get(arg0).getPhoneNumber());
//                sendIntent.putExtra("numberIndex", arg0);
//                PendingIntent sendPendingIntent = PendingIntent
//                        .getBroadcast(context, (int) System.currentTimeMillis(),
//                                sendIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//
//                Intent deliveredIntent = new Intent(
//                        AppConstants.ACTION_SMS_DELIVERED_ACTION);
//                deliveredIntent.putExtra("number", list.get(arg0).getPhoneNumber());
//                PendingIntent deliveredPendingIntent = PendingIntent
//                        .getBroadcast(context, (int) System.currentTimeMillis(),
//                                deliveredIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                ArrayList<String> msgs = sms.divideMessage(list.get(arg0).getContent());
//                for (String msg : msgs) {
//                    sms.sendTextMessage(list.get(arg0).getPhoneNumber(), null, msg,
//                            sendPendingIntent, deliveredPendingIntent);
//                }
//
//            });
//        }


        return arg1;
    }

    static class ViewHolder {
        private TextView failed;
        private TextView sucess;
        private TextView time;
    }
}
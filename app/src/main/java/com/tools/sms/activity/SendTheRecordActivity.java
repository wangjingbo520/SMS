package com.tools.sms.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.sms.R;
import com.tools.sms.adapter.SendResultAdapter;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.SendResultBean;
import com.tools.sms.db.dbs.DbManager;
import com.tools.sms.service.AppConstants;
import com.tools.sms.tools.DialogToastUtil;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import java.util.List;

import butterknife.BindView;

/**
 * @author w（C）
 * describe
 */
public class SendTheRecordActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.tvTotol)
    TextView tvTotol;
    private List<SendResultBean> list;

    private SendResultAdapter adapter;

    private SMSStatusReceiver receiver;

    private SQLiteDatabase db;
    private Cursor cursor;

    private AlertDialog.Builder builder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        String sql = "select * from " + Constants.TABBLE_RESULT_SEND;
        cursor = DbManager.queryBySQL(db, sql, null);
        list = DbManager.getSendResultList(cursor);
        if (list.size() < 1) {
            ToastUtil.showMessage(getString(R.string.hasno));
            tvTotol.setText("发送记录：无");
            return;
        }
        tvTotol.setText("发送记录：" + list.size() + "条");

        adapter = new SendResultAdapter(this, list);
        listView.setAdapter(adapter);
        receiver = new SMSStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.ACTION_SMS_SEND_NUMBER);
        filter.addAction(AppConstants.ACTION_SMS_DELIVERED_ACTION);
        filter.addAction(AppConstants.ACTION_SMS_SEND_ACTIOIN);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void initView() {
        titleView.setTitle("发送记录");
        titleView.getBackView().setOnClickListener(v -> finish());
        titleView.setRightTitle("删除");
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("确定要删除发送记录吗？");
        db = DbManager.getInstance(this).getReadableDatabase();
        titleView.getRightView().setOnClickListener(v -> {
            delete();
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_send_the_record;
    }

    public class SMSStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AppConstants.ACTION_SMS_SEND_NUMBER.equals(action)) {
                ToastUtil.showMessage("正在发送中...");
            } else if (AppConstants.ACTION_SMS_SEND_ACTIOIN.equals(action)) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ToastUtil.showMessage("向" + intent.getStringExtra("number") + " 发送短信成功");
                        if (adapter != null) {
                            if (adapter.index != -1) {
                                DbManager.updataSendResult(db, list.get(adapter.index).getId());
                                list.get(adapter.index).setTag("sucess");
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    default:
                        ToastUtil.showMessage("向" + intent.getStringExtra("number") + " 发送失败");
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        if (db != null) {
            db.close();
        }

        if (cursor != null) {
            cursor.close();
        }
    }


    private void delete() {
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (list != null) {
                if (list.size() < 1) {
                    ToastUtil.showMessage("您还没有发送记录");
                    return;
                }
            }
            db.execSQL("DELETE FROM sendresult");
            list.clear();
            String sql = "select * from " + Constants.TABBLE_RESULT_SEND;
            cursor = DbManager.queryBySQL(db, sql, null);
            list.addAll(DbManager.getSendResultList(cursor));
            if (adapter == null) {
                return;
            }
            adapter.notifyDataSetChanged();
            dialog.dismiss();
            tvTotol.setText("发送记录：无");
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setNeutralButton("以后再删除", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        builder.show();
    }

}

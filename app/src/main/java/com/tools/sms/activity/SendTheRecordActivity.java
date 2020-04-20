package com.tools.sms.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.tools.sms.AppDatabase;
import com.tools.sms.R;
import com.tools.sms.adapter.SendResultAdapter;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.bean.Main;
import com.tools.sms.bean.Main_Table;
import com.tools.sms.bean.SendReultBean;
import com.tools.sms.bean.SendReultBean_Table;
import com.tools.sms.service.AppConstants;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import java.util.List;
import java.util.Random;

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
    private List<Main> list;

    private SendResultAdapter adapter;


    private AlertDialog.Builder builder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        OrderBy orderBy = OrderBy.fromNameAlias(NameAlias.of("mainId")).descending();
        list = SQLite.select()
                .from(Main.class).orderBy(orderBy)
                .queryList();

        for (int i = 0; i < list.size(); i++) {
            Log.e("====>", "initData: "+list.get(i).toString() );
        }

        if (list.size() < 1) {
            ToastUtil.showMessage(getString(R.string.hasno));
            tvTotol.setText("发送记录：无");
            return;
        }
        tvTotol.setText("发送记录：" + list.size() + "次");

        adapter = new SendResultAdapter(this, list);
        listView.setAdapter(adapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.ACTION_SMS_SEND_NUMBER);
        filter.addAction(AppConstants.ACTION_SMS_DELIVERED_ACTION);
        filter.addAction(AppConstants.ACTION_SMS_SEND_ACTIOIN);
    }

    @Override
    protected void initView() {
        titleView.setTitle("发送记录");
        titleView.getBackView().setOnClickListener(v -> finish());
        titleView.setRightTitle("清空");
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setIcon(R.mipmap.logo);
        builder.setMessage("确定要删除发送记录吗？");
        titleView.getRightView().setOnClickListener(v -> {
            delete();
        });

        listView.setOnItemClickListener((adapterView, view, i, l) ->
                DetailSendActivity.start(SendTheRecordActivity.this, list.get(i).getMainId()));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_send_the_record;
    }


    private void delete() {
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (list != null) {
                if (list.size() < 1) {
                    ToastUtil.showMessage("您还没有发送记录");
                    return;
                }
            }

            FlowManager.getDatabase(AppDatabase.class).executeTransaction(databaseWrapper -> {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).delete();
                    SQLite.delete(SendReultBean.class)
                            .where(SendReultBean_Table.mianId.eq(list.get(i).getMainId()));
                }
            });


            list.clear();
            list.addAll(SQLite.select().from(Main.class).queryList());
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

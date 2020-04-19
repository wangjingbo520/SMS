package com.tools.sms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.sms.R;
import com.tools.sms.adapter.Templatedapter;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.Template;
import com.tools.sms.bean.XLSUserBean;
import com.tools.sms.db.dbs.DbManager;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tools.sms.base.Constants.NUMBER_SELECTOR_TEMPLATE;

/**
 * @author wjb
 * describe
 */
public class TemplateTextActivity extends BaseActivity implements Templatedapter.OnItemClickListener, Templatedapter.OnItemLongClickListenser {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.tv_add)
    TextView tv_add;
    private Templatedapter templatedapter;
    private List<Template> templates;
    private AlertDialog.Builder builder;

    private int tag = 0;

    private XLSUserBean xlsUserBean;

    public static void start(Activity context, int tag, boolean isStartForResult, XLSUserBean xlsUserBean) {
        Intent starter = new Intent(context, TemplateTextActivity.class);
        starter.putExtra("tag", tag);
        if (isStartForResult) {
            starter.putExtra("xlsUserBean", xlsUserBean);
            context.startActivityForResult(starter, NUMBER_SELECTOR_TEMPLATE);
        } else {
            context.startActivity(starter);
        }
    }

    @Override
    protected void initData() {
        if (tag == 0) {
            titleView.setTitle("我的模板短信");
        } else if (tag == 1) {
            titleView.setTitle("模板短信(长按可删除)");
        } else if (tag == 2) {
            titleView.setTitle("选择模板短信");
            xlsUserBean = (XLSUserBean) getIntent().getSerializableExtra("xlsUserBean");
        } else {
            titleView.setTitle("我的模板短信");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = DbManager.getInstance(this).getReadableDatabase();
        String sql = "select * from " + Constants.TABBLE_NAME_TEMPLATE;
        Cursor cursor = DbManager.queryBySQL(db, sql, null);
        templates = DbManager.getTemplate(cursor);
        templatedapter = new Templatedapter(this, templates);
        recyclerview.setAdapter(templatedapter);

        if (tag == 1) {
            templatedapter.setOnItemLongClickListener(this);
        } else if (tag == 2) {
            templatedapter.setOnItemClickListener(this);
        }
    }

    @Override
    protected void initView() {
        titleView.getBackView().setOnClickListener(v -> finish());

        if (getIntent() != null) {
            tag = getIntent().getIntExtra("tag", 0);
            if (tag == 2) {
                xlsUserBean = (XLSUserBean) getIntent().getSerializableExtra("xlsUserBean");
            }
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("确定要删除这个短信模板吗？");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_send_record;
    }

    @Override
    public void onItemClick(int position) {
        if (templates != null) {
            if (templates.get(position) != null) {
                String content = templates.get(position).getContent();
                Intent intent = new Intent();
                intent.putExtra("content", content);
                setResult(NUMBER_SELECTOR_TEMPLATE, intent);
                finish();
            }
        }
    }

    @Override
    public void onItemLongClick(int id, int position) {
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (templates != null) {
                if (templates.get(position) != null) {
                    SQLiteDatabase db = DbManager.getInstance(TemplateTextActivity.this).getWritableDatabase();
                    int result = db.delete("template", "id=?", new String[]{String.valueOf(id)});
                    if (result > 0) {
                        templates.remove(position);
                        templatedapter.notifyDataSetChanged();
                        ToastUtil.showMessage("删除成功");
                    }
                }
            }
            dialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setNeutralButton("以后再删除", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        builder.show();
    }


    @OnClick(R.id.tv_add)
    public void onViewClicked() {
        if (tag == 2) {
            SmsEditActivity.startXLS(this, xlsUserBean);
        } else {
            SmsEditActivity.start(this);
        }

    }
}

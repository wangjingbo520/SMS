package com.tools.sms.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.bean.XLSUserBean;
import com.tools.sms.db.dbs.DbManager;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.BubbleTextView;
import com.tools.sms.views.TitleView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tools.sms.tools.Utils.htmlTextUtils;

/**
 * @author w（C）
 * describe
 */
public class SmsEditActivity extends BaseActivity {

    @BindView(R.id.etContent)
    EditText editText;
    @BindView(R.id.bubble)
    BubbleTextView bubble;
    @BindView(R.id.tvTextSize)
    TextView tvTextSize;
    @BindView(R.id.titleView)
    TitleView titleView;

    private static final int MAX_NUM = 70;

    private String content;
    private Editable editable;
    private int index;
    private Calendar now;
    private String day;

    private XLSUserBean xlsUserBean;

    //判断标识
    private int tag = 100;

    public static void start(Activity context) {
        Intent starter = new Intent(context, SmsEditActivity.class);
        context.startActivity(starter);
    }

    public static void startXLS(Activity context, XLSUserBean xlsUserBean) {
        Intent starter = new Intent(context, SmsEditActivity.class);
        starter.putExtra("xlsUserBean", xlsUserBean);
        context.startActivity(starter);
    }

    @Override
    protected void initData() {
        now = Calendar.getInstance();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initView() {
        titleView.setTitle("编辑短信模板");
        titleView.setBackImageGone(false);
        titleView.getBackView().setOnClickListener(v -> finish());

        if (getIntent() != null) {
            if (getIntent().getSerializableExtra("xlsUserBean") != null) {
                xlsUserBean = (XLSUserBean) getIntent().getSerializableExtra("xlsUserBean");
                tag = 1;
            }
        }

        editText.addTextChangedListener(watcher);
        day = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sms_edit;
    }


    @OnClick({R.id.tv_expand_1, R.id.tv_expand_2, R.id.tv_sure, R.id.tv_date, R.id.tv_time, R.id.tv_expand_3, R.id.tv_expand_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_expand_1:
                //扩展字段1
                editable = editText.getText();
                index = editText.getSelectionStart();
                if (tag == 1) {
                    editable.insert(index, xlsUserBean.getXlsOne());
                } else {
                    editable.insert(index, "{|e1|}");
                }
                break;
            case R.id.tv_expand_2:
                //扩展字段2
                editable = editText.getText();
                index = editText.getSelectionStart();
                if (tag == 1) {
                    editable.insert(index, xlsUserBean.getXlsTwo());
                } else {
                    editable.insert(index, "{|e2|}");
                }
                break;
            case R.id.tv_expand_3:
                //扩展字段3
                editable = editText.getText();
                index = editText.getSelectionStart();
                if (tag == 1) {
                    editable.insert(index, xlsUserBean.getXlsThree());
                } else {
                    editable.insert(index, "{|e3|}");
                }
                break;
            case R.id.tv_expand_4:
                //扩展字段4
                editable = editText.getText();
                index = editText.getSelectionStart();
                if (tag == 1) {
                    editable.insert(index, xlsUserBean.getXlsFour());
                } else {
                    editable.insert(index, "{|e4|}");
                }
                break;
            case R.id.tv_sure:
                commit();
                break;
            case R.id.tv_date:
                editable = editText.getText();
                index = editText.getSelectionStart();
                editable.insert(index, day);
                break;
            case R.id.tv_time:
                editable = editText.getText();
                index = editText.getSelectionStart();
                editable.insert(index, now.get(Calendar.HOUR_OF_DAY)
                        + "时" + now.get(Calendar.MINUTE) + "分" + now.get(Calendar.SECOND) + "秒");
                break;
            default:
                break;
        }
    }

    private void commit() {
        String content = editText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showMessage("短信发送模板不能为空");
            return;
        }
        save(content);

    }

    private void save(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showMessage("您还没有输入短信内容！");
            return;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String sql = "insert into template(content,time) " +
                "values(?,?)";
        SQLiteDatabase db = DbManager.getInstance(this).getReadableDatabase();
        db.execSQL(sql, new String[]{content, dt1.format(date)});
        db.close();
        ToastUtil.showMessage("恭喜您，短信模板添加成功，可以在“我的短信模板”中查看”");
        finish();
    }

    TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            tvTextSize.setText(s.length() + "/70");
            if (s.length() > MAX_NUM) {
                ToastUtil.showMessage("发送内容已经超过70个字，短信将分开发出");
            }
            showResult(s.toString());
        }
    };


    private void showResult(String inputStr) {

        this.content = inputStr;
        if (inputStr.contains("{|t|}")) {
            content = content.replace("{|t|}", day);
        }

        if (inputStr.contains("{|e1|}")) {
            content = content.replace("{|e1|}", "12345678910");
        }

        if (inputStr.contains("{|e2|}")) {
            content = content.replace("{|e2|}", "张三");
        }

        if (inputStr.contains("{|e3|}")) {
            content = content.replace("{|e3|}", "2000");
        }

        if (inputStr.contains("{|e4|}")) {
            content = content.replace("{|e4|}", "4000");
        }

        bubble.setText(htmlTextUtils(content));
    }


}

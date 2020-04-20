package com.tools.sms.activity;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.bean.XLSUserBean;
import com.tools.sms.bean.Template;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wjb
 * describe
 */
public class AddTemplateActivity extends BaseActivity {
    private static final int MAX_NUM = 70;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvTextSize)
    TextView tvTextSize;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleView.setTitle("添加短信模板");
        titleView.setRightTitle("查看");
        titleView.getBackView().setOnClickListener(v -> finish());
        titleView.getRightView().setOnClickListener(v ->
                TemplateTextActivity.start(this, 0, false, new XLSUserBean()));
        etContent.addTextChangedListener(watcher);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_template;
    }


    @OnClick(R.id.btnSure)
    public void onViewClicked() {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showMessage("您还没有输入短信内容！");
            return;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        Template template = new Template();
        template.setTime(dt1.format(date));
        template.setContent(content);
        template.save();
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
            Log.i("afterTextChanged", s.toString());
            if (s.length() > MAX_NUM) {
                ToastUtil.showMessage("发送内容已经超过70个字");
            }
            //   int num = MAX_NUM - s.length();
            tvTextSize.setText(s.length() + "/70");
        }
    };

}

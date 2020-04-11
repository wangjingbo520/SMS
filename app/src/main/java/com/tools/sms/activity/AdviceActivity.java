package com.tools.sms.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * @author w（C）
 * describe
 */
public class AdviceActivity extends BaseActivity {
    private static final int MAX_NUM = 200;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvTextSize)
    TextView tvTextSize;

    @Override
    protected void initData() {
        titleView.setTitle("建议反馈");
        titleView.setBackfinishListenser(this);
    }

    @Override
    protected void initView() {
        etContent.addTextChangedListener(watcher);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_advice;
    }

    @OnClick(R.id.btnCommit)
    public void onViewClicked() {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showMessage("您还没输入内容");
            return;
        }
        postData(content);
    }

    private void postData(String content) {
        Map<String, String> params = new HashMap<>();
        params.put("username", SPUtils.getInstance().getString(Constants.USER_NAME,""));
        params.put("advice", content);
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params, null, true, InterfaceMethod.USER_ADVICE);
    }

    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        ToastUtil.showMessage("恭喜您，提交成功");
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
                ToastUtil.showMessage("发送内容已经超过200个字");
            }
            //   int num = MAX_NUM - s.length();
            tvTextSize.setText(s.length() + "/200");
        }
    };
}

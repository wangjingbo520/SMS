package com.tools.sms.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.bean.UserBean;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * @author w（C）
 * describe
 */
public class FogetPasswordActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.etUsername)
    TextView etUsername;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        String username = getIntent().getStringExtra("username");
        etUsername.setText(username);
        titleView.setTitle("找回密码");
        titleView.getBackView().setOnClickListener(v -> finish());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_foget_password;
    }


    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        UserBean userBean = gson.fromJson(response, UserBean.class);
        ToastUtil.showMessage(userBean.getMsg());
        finish();
    }


    @OnClick(R.id.tv_commit)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:

                break;
            default:
                break;
        }
    }
}

package com.tools.sms.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.android.volley.Request;
import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.UserBean;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.MD5;
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
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPasword)
    EditText etPasword;
    @BindView(R.id.titleView)
    TitleView titleView;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleView.setTitle("注册群发短信");
        titleView.setBackImageGone(false);
        titleView.getBackView().setOnClickListener(v -> finish());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.tv_register)
    public void onViewClicked() {
        register();
    }

    private void register() {
        String userName = etUsername.getText().toString().trim();
        String password = etPasword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showMessage("用户名名不能为空！");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtil.showMessage("密码不能为空");
            return;
        }


        if (TextUtils.isEmpty(device_id)) {
            ToastUtil.showMessage(getString(R.string.notfind));
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", userName);
        params.put("password", MD5.md5(password));
        params.put("device_id", device_id);
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params, null, true, InterfaceMethod.REGISTER);
    }

    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        UserBean userBean = gson.fromJson(response, UserBean.class);
        SPUtils.getInstance().clear();
        String password = etPasword.getText().toString().trim();
        SPUtils.getInstance().put(Constants.USER_NAME, userBean.getData().getUsername());
        SPUtils.getInstance().put(Constants.USER_PASSWORD, password);

        ToastUtil.showMessage("恭喜您，注册成功");
        finish();
    }


}

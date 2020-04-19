package com.tools.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.tools.sms.MyApp;
import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.UserBean;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.MD5;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.EditDialog;
import com.tools.sms.views.TitleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.tools.sms.base.Constants.USER_ID;

/**
 * @author w（C）
 * describe
 */
public class LoginActivity extends BaseActivity implements EditDialog.LisentenserDilog {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPasword)
    EditText etPasword;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.yj)
    ImageView yj;

    private EditDialog editDialog;

    private boolean isShow = false;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleView.setBackImageGone(true);
        titleView.setTitle("登录群发短信");
        etPasword.setSelection(etPasword.length());
        yj.setOnClickListener(v -> {
            if (isShow) {
                etPasword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                yj.setImageResource(R.mipmap.yc);
                isShow = false;
            } else {
                yj.setImageResource(R.mipmap.xs);
                etPasword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isShow = true;
            }

        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    private void login() {
        String userName = etUsername.getText().toString().trim();
        String password = etPasword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showMessage("用户名不能为空！");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtil.showMessage("密码不能为空");
            return;
        }


        if (TextUtils.isEmpty(device_id)) {
            show("未获取到您的设备id,无法进行登录！您可以检查下是否设置了权限！");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", userName);
        params.put("password", MD5.md5(password));
        params.put("device_id", device_id);
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params, null, true, InterfaceMethod.LOGIN);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String userName = SPUtils.getInstance().getString(Constants.USER_NAME, "");
        etUsername.setText(userName);
        String passWord = SPUtils.getInstance().getString(Constants.USER_PASSWORD, "");
        etPasword.setText(passWord);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userName = SPUtils.getInstance().getString(Constants.USER_NAME, "");
        String passWord = SPUtils.getInstance().getString(Constants.USER_PASSWORD, "");
        if (!TextUtils.isEmpty(userName)) {
            etUsername.setText(userName);
        }
        if (!TextUtils.isEmpty(passWord)) {
            etPasword.setText(passWord);
        }
    }

    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        if (interfaceMethod.equals(InterfaceMethod.LOGIN)) {
            UserBean userBean = gson.fromJson(response, UserBean.class);
            if (device_id.equals(userBean.getData().getDevice_id())) {
                ToastUtil.showMessage(userBean.getMsg());
                SPUtils.getInstance().put(Constants.USER_NAME, userBean.getData().getUsername());
                SPUtils.getInstance().put(USER_ID, userBean.getData().getUser_id());
                SPUtils.getInstance().put(Constants.USER_PASSWORD, etPasword.getText().toString().trim());
                MyApp.openning = userBean.getData().getOpening();

                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.logo);
                builder.setTitle("提示！");
                builder.setMessage("当前账号在其他设备上登录，挤下线则对方无法使用短信群发功能！");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    Map<String, String> params = new HashMap<>();
                    params.put("username", userBean.getData().getUsername());
                    params.put("device_id", device_id);
                    RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                            params, null, true, InterfaceMethod.USER_RELOGIN);

                });
                builder.show();
            }
        } else if (interfaceMethod.equals(InterfaceMethod.UPDATE_PASSWORD)) {
            SPUtils.getInstance().put(Constants.USER_PASSWORD, "");
            ToastUtil.showMessage("修改密码成功，请重新登录！");
            etPasword.setText("");
        } else if (interfaceMethod.equals(InterfaceMethod.USER_RELOGIN)) {
            UserBean userBean = gson.fromJson(response, UserBean.class);
            ToastUtil.showMessage(userBean.getMsg());
            SPUtils.getInstance().put(Constants.USER_NAME, userBean.getData().getUsername());
            SPUtils.getInstance().put(USER_ID, userBean.getData().getUser_id());
            SPUtils.getInstance().put(Constants.USER_PASSWORD, etPasword.getText().toString().trim());
            MyApp.openning = userBean.getData().getOpening();

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @OnClick({R.id.tv_sure, R.id.tv_register, R.id.tv_findpassword, R.id.tvSilence})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                int type = SPUtils.getInstance().getInt(Constants.XIEYI, 0);
                if (type == 1) {
                    login();
                } else {
                    ToastUtil.showMessage("请先阅读用户服务协议");
                }
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_findpassword:
                sendCode(this);
                break;
            case R.id.tvSilence:
                AgreementActivity.start(this);
                break;
            default:
                break;
        }
    }


    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String phone = (String) phoneMap.get("phone");
                    if (phone == null) {
                        return;
                    }
                    showEditDialog(phone);
                } else {
                    ToastUtil.showMessage("短信操作失败");
                }
            }
        });
        page.show(context);
    }


    private void showEditDialog(String username) {
        editDialog = new EditDialog(this, this, username);
        editDialog.show();
    }


    @Override
    public void updatePassword(String username, String password) {
        if (editDialog != null) {
            editDialog.dismiss();
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", MD5.md5(password));
        RequestHandler.addRequest(Request.Method.POST, LoginActivity.this, mHandler, Constants.CODE_RESULT,
                params, null, true, InterfaceMethod.UPDATE_PASSWORD);

    }


    private void show(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("提示！");
        builder.setMessage(message);
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }


}

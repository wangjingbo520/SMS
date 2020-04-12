package com.tools.sms.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.tools.sms.MyApp;
import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.UserBean;
import com.tools.sms.bean.VersionApp;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.tools.Utils;
import com.tools.sms.views.TitleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author w（C）
 * describe
 */
public class MyActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDays)
    TextView tvDays;
    @BindView(R.id.tv1)
    TextView tv1;

    private int serverVersion;
    private int currentVersionCode;

    private boolean isOpenning = false;

    private String contetent = "";

    @Override
    protected void initData() {
        titleView.setTitle("我的");
        getUserinfo();
    }

    private void getUserinfo() {
        Map<String, String> params = new HashMap<>();
        params.put("username", SPUtils.getInstance().getString(Constants.USER_NAME));
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params, null, false, InterfaceMethod.QUERY_USER_INFO);
    }

    @Override
    protected void initView() {
        titleView.setBackfinishListenser(this);
        tv1.setText("V " + Utils.getLocalVersionName(this));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my;
    }

    private void checkVersion() {
        Map<String, String> params1 = new HashMap<>();
        params1.put("versionCode", Utils.getLocalVersion(this) + "");
        params1.put("versionName", Utils.getLocalVersionName(this));
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params1, null, false, InterfaceMethod.APDATE);
    }

    @OnClick({R.id.ll1, R.id.tv_exit, R.id.ll2, R.id.ll3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                if (!isOpenning) {
                    PayActivity.start(this);
                    return;
                }
                ToastUtil.showMessage("您已经开通会员，无需再次开通");
                break;
            case R.id.tv_exit:
                showExitDialog();
                break;
            case R.id.ll2:
                checkVersion();
                break;
            case R.id.ll3:
                startActivity(new Intent(this, AdviceActivity.class));
                break;
            default:
                break;
        }
    }


    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("提示！");
        builder.setMessage("您确定退出登录吗？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            String username = SPUtils.getInstance().getString(Constants.USER_NAME);
            String password = SPUtils.getInstance().getString(Constants.USER_PASSWORD);
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                    params, null, false, InterfaceMethod.EXIT_LOGIN);

        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        if (interfaceMethod.equals(InterfaceMethod.EXIT_LOGIN)) {
            ToastUtil.showMessage("成功退出");
            SPUtils.getInstance().put(Constants.USER_PASSWORD, "");
            SPUtils.getInstance().put(Constants.XIEYI, 0);
            MyApp.getInstance().exit();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (interfaceMethod.equals(InterfaceMethod.QUERY_USER_INFO)) {
            UserBean userBean = gson.fromJson(response, UserBean.class);
            tvName.setText("用户名：" + userBean.getData().getUsername());
            if (userBean.getData().getOpening() == 0) {
                tv_type.setText("未开通");
                isOpenning = false;
                tvDays.setText("0天");
                tvDays.setTextColor(getResources().getColor(R.color.red));
                tv_type.setTextColor(getResources().getColor(R.color.red));
            } else if (userBean.getData().getOpening() == 1) {
                tv_type.setText("已开通");
                isOpenning = true;
                MyApp.setOpenning(1);
                tv_type.setTextColor(getResources().getColor(R.color.theme_color));
                tvDays.setText(userBean.getData().getRemaining_day() + "天");
            } else {
                tv_type.setText("未知");
                tvDays.setText("未知");
            }
        } else if (interfaceMethod.equals(InterfaceMethod.APDATE)) {
            VersionApp versionApp = gson.fromJson(response, VersionApp.class);
            currentVersionCode = Utils.getLocalVersion(this);
            serverVersion = versionApp.getData().getVersionCode();
            contetent = versionApp.getData().getVersionDescribed();
            if (serverVersion > currentVersionCode) {
                showUpdateDialog(versionApp.getData().getDownUrl()
                        , versionApp.getData().getVersionDescribed(), versionApp.getData().getVersionName());
            } else {
                ToastUtil.showMessage("您已经是最新版本！");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                getUserinfo();
            }
        }
    }
}

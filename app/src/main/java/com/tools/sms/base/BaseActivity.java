package com.tools.sms.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.tools.sms.MyApp;
import com.tools.sms.R;
import com.tools.sms.activity.LoginActivity;
import com.tools.sms.http.IHandleMessage;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.MyVolleyHandler;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.DeviceIdUtils;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.statusbartils.Eyes;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * @author wjb
 * describe
 */
@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity implements IHandleMessage {
    public MyVolleyHandler<BaseActivity> mHandler;
    public Gson gson;

    public String device_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        MyApp.getInstance().addActivity(this);
        device_id = DeviceIdUtils.getDeviceId(this);
        Eyes.setStatusBarLightMode(this, getResources().getColor(R.color.background));
        ButterKnife.bind(this);
        mHandler = new MyVolleyHandler<>(this);
        gson = new Gson();
        initView();
        initData();
    }

    @Override
    public void onHandleMessage(Message msg) {
        if (msg.what == Constants.CODE_RESULT) {
            Bundle data = msg.getData();
            String response = data.getString("response");
            String interfaceMethod = data.getString("interfaceMethod");
            onSuceess(response, interfaceMethod);
        }
    }


    public void showExitDialog(Context context) {
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_ID))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.logo);
            builder.setTitle("提示！");
            builder.setCancelable(false);
            builder.setMessage("您还没登录，请先登录");
            builder.setPositiveButton("确定", (dialog, which) -> {
                SPUtils.getInstance().clear();
                startActivity(new Intent(context, LoginActivity.class));
            });
            builder.show();
        }
    }

    public void onSuceess(String response, String interfaceMethod) {

    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int getContentLayout();


}

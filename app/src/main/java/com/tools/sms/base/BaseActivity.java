package com.tools.sms.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.google.gson.Gson;
import com.tools.sms.MyApp;
import com.tools.sms.R;
import com.tools.sms.activity.LoginActivity;
import com.tools.sms.activity.PayActivity;
import com.tools.sms.bean.VersionApp;
import com.tools.sms.http.IHandleMessage;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.MyVolleyHandler;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.DeviceIdUtils;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.statusbartils.Eyes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static com.tools.sms.http.InterfaceMethod.USER_UPDATE_APK;

/**
 * @author wjb
 * describe
 */
@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity implements IHandleMessage, OnButtonClickListener, OnDownloadListener {
    public MyVolleyHandler<BaseActivity> mHandler;
    public Gson gson;
    public String device_id;

    public ProgressDialog progressDialog;

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

    public void showUpdateDialog(VersionApp versionApp) {
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                //.setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置对话框强制更新时进度条和文字的颜色
                //.setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置强制更新
                .setForcedUpgrade(false)
                //设置对话框按钮的点击监听
                .setButtonClickListener(this)

                //设置下载过程的监听
                .setOnDownloadListener(this);

        DownloadManager manager = DownloadManager.getInstance(this);
        String message = versionApp.getData().getVersionDescribed().replaceAll(";", "\n");
        manager.setApkName(versionApp.getData().getApkName() + "_" + versionApp.getData().getVersionName() + ".apk")
                .setApkUrl(InterfaceMethod.base_url + USER_UPDATE_APK)
                .setSmallIcon(R.mipmap.logo)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setApkVersionCode(versionApp.getData().getVersionCode())
                .setApkVersionName(versionApp.getData().getVersionName())
                .setApkSize(versionApp.getData().getApkSize())
                .setApkDescription(message)
                .download();
    }

    public void showExitDialog(Activity mActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("提示！");
        builder.setMessage("对不起，您还没有开通会员，请先进行充值");
        builder.setPositiveButton("确定", (dialog, which) -> {
            Intent intent = new Intent(mActivity, PayActivity.class);
            startActivityForResult(intent, 1000);
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void reLogin(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("提示！");
        builder.setMessage(message);
        builder.setPositiveButton("确定", (dialog, which) -> {
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
    public void onButtonClick(int id) {

    }

    @Override
    public void start() {

    }

    @Override
    public void downloading(int max, int progress) {

    }

    @Override
    public void done(File apk) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void error(Exception e) {

    }
}

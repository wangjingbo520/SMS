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

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.google.gson.Gson;
import com.tools.sms.MyApp;
import com.tools.sms.R;
import com.tools.sms.activity.LoginActivity;
import com.tools.sms.http.IHandleMessage;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.MyVolleyHandler;
import com.tools.sms.tools.DeviceIdUtils;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.statusbartils.Eyes;

import java.io.File;

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

    private DownloadManager manager;

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

    public void showUpdateDialog(String url, String descride, String versionName) {
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
                .setForcedUpgrade(true)
                //设置对话框按钮的点击监听
                .setButtonClickListener(this)
                //设置下载过程的监听
                .setOnDownloadListener(this);

        manager = DownloadManager.getInstance(this);
        manager.setApkName(versionName + ".apk")
                .setApkUrl(InterfaceMethod.base_url + USER_UPDATE_APK)
                .setSmallIcon(R.mipmap.logo)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setApkVersionCode(2)
                .setApkVersionName("2.1.8")
                .setApkSize("20.4")
                .setApkDescription(descride)
                .download();
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

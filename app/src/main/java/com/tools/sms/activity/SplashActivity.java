package com.tools.sms.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tools.sms.R;
import com.tools.sms.tools.ToastUtil;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
/**
 * @author w（C）
 * describe
 */
@RuntimePermissions
public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;
    private CloseRunnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler(getMainLooper());
        runnable = new CloseRunnable();
        SplashActivityPermissionsDispatcher.needWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE})
    void need() {
        mHandler.postDelayed(runnable, 1500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_CONTACTS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE})
    void showRationale(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.READ_CONTACTS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void denied() {
        ToastUtil.showMessage("您已经拒绝权限,程序自动退出");
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.READ_CONTACTS, Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE})
    void askAgain() {
        new AlertDialog.Builder(this)
                .setMessage("您已经拒绝请求权限,请到设置页面打开权限")
                .setPositiveButton("确定", (dialog, which) -> SplashActivity.this.startSetting(SplashActivity.this, SplashActivity.this.getPackageName()))
                .setNegativeButton("取消", (dialog, which) -> SplashActivity.this.finish()).show();
    }

    private void startSetting(Context context, String packageName) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", packageName, null));
        context.startActivity(intent);
    }

    class CloseRunnable implements Runnable {

        @Override
        public void run() {
            //   startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
//            if (!TextUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_ID))) {
//                String beginDate = SPUtils.getInstance().getString(Constants.USER_ID);
//                String current = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//                int i = caculateTotalTime(beginDate, current);
//                if (i <= -7) {
//                    show();
//                } else {
//                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//                    finish();
//                }
//            } else {
//                String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//                SPUtils.getInstance().put(Constants.USER_ID, time);
//                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//                finish();
//            }
        }
    }

    private void show() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("提示");
        normalDialog.setCancelable(false);
        normalDialog.setMessage("试用期限已到，请联系开发者qq:390463560");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
//        normalDialog.setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //...To-do
//                    }
//                });
//        // 显示对话框
        normalDialog.show();
    }


}

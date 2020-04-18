package com.tools.sms.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.tools.sms.base.Constants.alipay;
import static com.tools.sms.http.InterfaceMethod.PAY_URL;

/**
 * @author w（C）
 * describe
 */
public class PayActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.et_kami)
    EditText et_kami;

    private int payType = 0;

    public static final int REQUEST_CODE = 2233;
    private AlertDialog.Builder builder;


    public static void start(Activity context) {
        Intent starter = new Intent(context, PayActivity.class);
        context.startActivityForResult(starter, 1000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleView.setTitle("收费标准");
        titleView.setBackfinishListenser(this);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择支付方式");
        builder.setIcon(R.mipmap.logo);

        findViewById(R.id.tv_pay).setOnClickListener(v -> {
            // PayActivity.this.dialogChoice();
            String camilo = et_kami.getText().toString().trim();
            if (TextUtils.isEmpty(camilo)) {
                ToastUtil.showMessage("请先输入卡密....");
                return;
            }

            doPost(camilo);
        });

        findViewById(R.id.tvInto).setOnClickListener(view -> startLLQ());
    }

    private void doPost(String camilo) {
        String username = SPUtils.getInstance().getString(Constants.USER_NAME);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("camilo", camilo);
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params, null, false, InterfaceMethod.USER_CHARGE);
    }


    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        ToastUtil.showMessage("恭喜您，成功开通VIP，尽情使用群发短信功能");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay;
    }

    private void dialogChoice() {
        payType = 0;
        final String[] items = {"微信支付", "支付宝支付"};
        builder.setSingleChoiceItems(items, 0,
                (dialog, which) -> payType = which);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (payType == 0) {
                checkPermissionAndDonateWeixin();
                dialog.dismiss();
            }
            if (payType == 1) {
                donateAlipay();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void checkPermissionAndDonateWeixin() {
        //检测微信是否安装
        if (!WeiXinDonate.hasInstalledWeiXinClient(this)) {
            Toast.makeText(this, "未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //已经有权限
            showDonateTipDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    private void showDonateTipDialog() {
        new AlertDialog.Builder(this)
                .setTitle("微信支付操作步骤")
                .setMessage("点击确定按钮后会跳转微信扫描二维码界面：\n\n" + "1. 点击右上角的菜单按钮\n\n" + "2. 点击'从相册选取二维码'\n\n" + "3. 选择第一张二维码图片即可\n\n")
                .setPositiveButton("确定", (dialog, which) -> {
                    donateWeixin();
                    dialog.dismiss();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void donateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.wxp);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonateSample" + File.separator +
                "didikee_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(this, qrPath);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            donateWeixin();
        } else {
            Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
        }
    }

    private void donateAlipay() {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, alipay);
        }
    }


    private void startLLQ() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(PAY_URL);
        intent.setData(content_url);
        startActivity(intent);
    }


}

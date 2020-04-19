package com.tools.sms.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

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
                ToastUtil.showMessage("请先输入卡号....");
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

    private void startLLQ() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(PAY_URL);
        intent.setData(content_url);
        startActivity(intent);
    }

}

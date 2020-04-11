package com.tools.sms.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tools.sms.R;
import com.tools.sms.base.Constants;
import com.tools.sms.tools.ToastUtil;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

//    @Override
//    public void onResp(BaseResp resp) {
//        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.app_tip);
//            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//            builder.show();
//        }
//    }

    @Override
    public void onResp(BaseResp resp) {
        int code = resp.errCode;
        switch (code) {
            case 0://支付成功后的界面
                ToastUtil.showMessage("支付成功");
                //   Constants.WEI_XIN_PAY_RESULT = 0; //这个是调起微信支付后，zai
                //返回主页面 然后在跳转至订单页面
                this.finish();
                break;
            case -1:
                //ToastUtil.showToast(getApplicationContext(), "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、您的微信账号异常等。");
                ToastUtil.showMessage("支付失败");
                //    Constants.WEI_XIN_PAY_RESULT = -1;
                this.finish();
                break;
            case -2://用户取消支付后的界面
                ToastUtil.showMessage("您已取消支付");
                //     Constants.WEI_XIN_PAY_RESULT = -2;
                this.finish();
                break;
        }
        //微信支付后续操作，失败，成功，取消
    }

}

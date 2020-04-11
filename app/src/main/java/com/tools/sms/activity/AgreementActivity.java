package com.tools.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;

import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * @author w（C）
 * describe
 */
public class AgreementActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    private int type;

    public static void start(Context context) {
        Intent starter = new Intent(context, AgreementActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void initData() {
        if (type == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    @Override
    protected void initView() {
        titleView.setTitle("服务协议");
        titleView.setBackImageGone(true);
        type = SPUtils.getInstance().getInt(Constants.XIEYI, 0);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_agreement;
    }


    @OnClick(R.id.tv_sure)
    public void onViewClicked() {
        if (checkBox.isChecked()) {
            SPUtils.getInstance().put(Constants.XIEYI, 1);
            finish();
        } else {
            ToastUtil.showMessage("请先确认阅读协议");
        }
    }
}

package com.tools.sms.activity;

import android.widget.TextView;

import com.android.volley.Request;
import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.VersionApp;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.Utils;
import com.tools.sms.views.TitleView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wjb（C）
 * describe
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.tv1)
    TextView tv1;


    @Override
    protected void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("versionCode", Utils.getLocalVersion(this) + "");
        params.put("versionName", Utils.getLocalVersionName(this));
        RequestHandler.addRequest(Request.Method.POST, AboutActivity.this, mHandler, Constants.CODE_RESULT,
                params, null, true, InterfaceMethod.APDATE);

    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        titleView.setTitle("关于软件");
        titleView.getBackView().setOnClickListener(v -> finish());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);

    }
}

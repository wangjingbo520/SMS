package com.tools.sms.activity;

import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.views.TitleView;

import butterknife.BindView;
/**
 * @author w（C）
 * describe
 */
public class UseGuideActivity extends BaseActivity {


    @BindView(R.id.titleView)
    TitleView titleView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleView.setTitle("使用说明");
        titleView.getBackView().setOnClickListener(v -> finish());

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_use_guide;
    }

}

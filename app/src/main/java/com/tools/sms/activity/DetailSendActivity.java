package com.tools.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.views.TitleView;

import butterknife.BindView;

/**
 * @author Bobo
 * @date 2019/9/21
 * describe
 */

public class DetailSendActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.listView)
    ListView listView;

    public static void start(Context context, int mainID) {
        Intent starter = new Intent(context, DetailSendActivity.class);
        starter.putExtra("mainID", mainID);
        context.startActivity(starter);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleView.setTitle("发送详情");
        titleView.getBackView().setOnClickListener(v -> finish());

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_detail_send;
    }
}

package com.tools.sms.activity;

import android.content.Intent;
import android.view.KeyEvent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.tools.sms.R;
import com.tools.sms.adapter.GridViewRecyclerviewAdapter;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.VersionApp;
import com.tools.sms.bean.XLSUserBean;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.tools.Utils;
import com.tools.sms.views.TitleView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author w（C）
 * describe
 */
public class HomeActivity extends BaseActivity implements
        GridViewRecyclerviewAdapter.OnItemClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.titleView)
    TitleView titleView;

    private int REQUESTCODE_FROM_ACTIVITY = 1000;

    private int[] imags = {R.mipmap.add, R.mipmap.mb, R.mipmap.mbs, R.mipmap.jl, R.mipmap.sm, R.mipmap.my};


    @Override
    protected int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleView.setTitle("主页");
        titleView.setBackImageGone(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerview.setLayoutManager(gridLayoutManager);
        GridViewRecyclerviewAdapter gridViewRecyclerviewAdapter
                = new GridViewRecyclerviewAdapter(this, getResources().getStringArray(R.array.titles), imags);
        gridViewRecyclerviewAdapter.setOnItemClickListener(this);
        recyclerview.setAdapter(gridViewRecyclerviewAdapter);
        checkVersion();
    }

    private void checkVersion() {
        Map<String, String> params1 = new HashMap<>();
        params1.put("versionCode", Utils.getLocalVersion(this) + "");
        params1.put("versionName", Utils.getLocalVersionName(this));
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params1, null, false, InterfaceMethod.APDATE);
    }

    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        if (interfaceMethod.equals(InterfaceMethod.APDATE)) {
            VersionApp versionApp = gson.fromJson(response, VersionApp.class);
            int currentVersionCode = Utils.getLocalVersion(this);
            int serverVersion = versionApp.getData().getVersionCode();
            if (serverVersion > currentVersionCode) {
                showUpdateDialog(versionApp);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                new LFilePicker()
                        .withActivity(HomeActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .withFileFilter(new String[]{".xls"})
                        .withMaxNum(1)
                        .withChooseMode(true)
                        .withIsGreater(false)
                        .withFileSize(500 * 1024)
                        .start();
                break;
            case 1:
                SmsEditActivity.start(this);
                break;
            case 2:
                TemplateTextActivity.start(this, 1, false, new XLSUserBean());
                break;
            case 3:
                ToastUtil.showMessage("正在开发中，期待下个版本");
                // startActivity(new Intent(this, SendTheRecordActivity.class));
                break;
            case 4:
                ToastUtil.showMessage("正在开发中，期待下个版本");
                // startActivity(new Intent(this, UseGuideActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, MyActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra("paths");
                if (list != null) {
                    if (list.size() > 0) {
                        String path = list.get(0);
                        ExcellSendActivity.start(this, path);
                    }
                }
            }
        }
    }


    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                ToastUtil.showMessage("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}

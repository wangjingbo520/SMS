package com.tools.sms.activity;

import android.content.Intent;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.sms.R;
import com.tools.sms.adapter.RecyclerviewAdapter;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.bean.MyContacts;
import com.tools.sms.tools.ContactUtils;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.views.TitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tools.sms.base.Constants.NUMBER_SELECTOR_CODE;
/**
 * @author w（C）
 * describe
 */
public class ContactsListActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.titleView)
    TitleView titleView;
    private RecyclerviewAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_contacts_list;
    }


    @Override
    protected void initData() {
        ArrayList<MyContacts> allContacts = ContactUtils.getAllContacts(this);
        adapter = new RecyclerviewAdapter(this, allContacts);
        recyclerview.setAdapter(adapter);
    }

    @Override
    protected void initView() {
        titleView.setTitle("选择联系人");
        titleView.setRightTitle("全选");
        titleView.getBackView().setOnClickListener(v -> finish());
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        titleView.getRightView().setOnClickListener(v -> {
            if (adapter == null) {
                return;
            }
            if (titleView.getRightText().equals("全选")) {
                adapter.setCheck(true);
                titleView.setRightTitle("清除");
            } else {
                adapter.setCheck(false);
                titleView.setRightTitle("全选");
            }
        });
    }


    @OnClick(R.id.btnSure)
    public void onViewClicked() {
        if (adapter != null) {
            ArrayList<String> selectData = adapter.getSelectData();
            if (selectData.size() < 1) {
                ToastUtil.showMessage("您还没有选择联系人");
            } else {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selectData", selectData);
                setResult(NUMBER_SELECTOR_CODE, intent);
                finish();
            }
        }
    }
}

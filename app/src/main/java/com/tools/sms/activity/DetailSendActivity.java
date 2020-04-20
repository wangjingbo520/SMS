package com.tools.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tools.sms.R;
import com.tools.sms.adapter.DetailAdapter;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.bean.SendReultBean;
import com.tools.sms.bean.SendReultBean_Table;
import com.tools.sms.views.TitleView;

import java.util.List;

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

    private List<SendReultBean> totalList;

    private long sum;//总的数据数目
    private int pageSize = 20; //每页有15条数据
    private int pageNum; // 一共有多少页
    private int currentPage = 1; //当期页码
    private boolean isDivPage; //判断是否分页

    private DetailAdapter detailAdapter;

    private int mainId;

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
        // titleView.setRightTitle("一键重发");
        titleView.getBackView().setOnClickListener(v -> finish());

        mainId = getIntent().getIntExtra("mainID", 0);

        sum = SQLite.select().from(SendReultBean.class)
                .where(SendReultBean_Table.mianId.eq(mainId)).queryList().size();

        pageNum = (int) Math.ceil(sum / (double) pageSize); //向上取整

        if (currentPage == 1) {
            totalList = SQLite.select().from(SendReultBean.class)
                    .where(SendReultBean_Table.mianId.eq(mainId))
                    .limit(pageSize)
                    .offset(0)
                    .queryList();
        }

        detailAdapter = new DetailAdapter(this, totalList);
        listView.setAdapter(detailAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //已经分页并且当前滚动状态为停止滚动了
                if (isDivPage && AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                    if (currentPage < pageNum) {
                        currentPage++;
                        // 根据最新的页码加载获取集合存储到数据源中
                        List<SendReultBean> sendReultBeans = SQLite.select().from(SendReultBean.class)
                                .where(SendReultBean_Table.mianId.eq(mainId))
                                .limit(pageSize).offset(currentPage * pageSize).queryList();
                        totalList.addAll(sendReultBeans);
                        detailAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isDivPage = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_detail_send;
    }
}

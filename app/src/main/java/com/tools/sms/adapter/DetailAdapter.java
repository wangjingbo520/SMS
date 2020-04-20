package com.tools.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tools.sms.R;
import com.tools.sms.bean.SendReultBean;

import java.util.List;

/**
 * @author wjb（H）
 * @date describe
 */
public class DetailAdapter extends BaseAdapter {
    private Context context;
    private List<SendReultBean> list;

    public DetailAdapter(Context context, List<SendReultBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<SendReultBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.tvPositon = (TextView) convertView.findViewById(R.id.tvPositon);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.tvTip = (TextView) convertView.findViewById(R.id.tvTip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPositon.setText(position + 1 + ".");
        viewHolder.tvDate.setText(list.get(position).getTime());

        //failure
        int status = list.get(position).getTag();
        if (status == 0) {
            viewHolder.tvStatus.setText("发送失败");
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.tvStatus.setText("发送成功");
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.textColor3));
        }

        String content = list.get(position).getContent();
        int length = content.length();
        if (length >= 70) {
            viewHolder.tvTip.setVisibility(View.VISIBLE);
            viewHolder.tvTip.setText("(分批发送)");
        } else {
            viewHolder.tvTip.setVisibility(View.GONE);
        }

        viewHolder.tvContent.setText(content);

        return convertView;
    }

    static class ViewHolder {
        TextView tvPositon;
        TextView tvContent;
        TextView tvDate;
        TextView tvStatus;
        TextView tvTip;
    }
}

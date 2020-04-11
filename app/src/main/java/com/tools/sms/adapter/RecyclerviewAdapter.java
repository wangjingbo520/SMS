package com.tools.sms.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.sms.R;
import com.tools.sms.bean.MyContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wjb
 * describe
 */
public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    private Context context;
    private List<MyContacts> data;
    private boolean isSelct = false;

    public RecyclerviewAdapter(Context context, List<MyContacts> data) {
        this.context = context;
        this.data = data;
    }

    public void selectAll() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelect(true);
        }
        notifyDataSetChanged();
    }

    public void pauseSelectAll() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelect(false);
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSelect()) {
                list.add(data.get(i).getPhone());
            }
        }
        return list;
    }

    public void setCheck(boolean checked) {
        if (checked) {
            isSelct = true;
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelect(true);
            }
        } else {
            isSelct = false;
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(data.get(position).getName());
        holder.number.setText(data.get(position).getPhone());
        holder.checkBox.setOnClickListener(v -> {
            boolean isCheck = holder.checkBox.isChecked();
            Log.i("------>", "onBindViewHolder: " + isCheck);
//            if (isCheck) {
//                holder.checkBox.setChecked(false);
//                data.get(position).setSelect(false);
//            } else {
//                holder.checkBox.setChecked(true);
//                data.get(position).setSelect(true);
//            }
        });

        if (isSelct) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                data.get(position).setSelect(true);
            } else {
                data.get(position).setSelect(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView number;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            number = itemView.findViewById(R.id.tvNumber);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}
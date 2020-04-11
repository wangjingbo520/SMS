package com.tools.sms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.sms.R;
import com.tools.sms.bean.MyContacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wjb
 * describe
 */
public class GridViewRecyclerviewAdapter extends RecyclerView.Adapter<GridViewRecyclerviewAdapter.ViewHolder> {

    private Context context;
    private String[] data;
    private int[] imgs;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public GridViewRecyclerviewAdapter(Context context, String[] data, int[] imgs) {
        this.context = context;
        this.data = data;
        this.imgs = imgs;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_gridview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(data[position]);
        holder.iv.setImageResource(imgs[position]);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        private ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvTitle);
            iv = itemView.findViewById(R.id.iv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
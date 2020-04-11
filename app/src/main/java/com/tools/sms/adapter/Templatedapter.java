package com.tools.sms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tools.sms.R;
import com.tools.sms.bean.Template;

import java.util.List;

/**
 * @author wjb
 * describe
 */
public class Templatedapter extends RecyclerView.Adapter<Templatedapter.ViewHolder> {

    private Context context;
    private List<Template> data;

    private OnItemClickListener onItemClickListener;

    private OnItemLongClickListenser onItemLongClickListenser;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListenser onItemLongClickListener) {
        this.onItemLongClickListenser = onItemLongClickListener;
    }

    public Templatedapter(Context context, List<Template> data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_tempate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.content.setText(data.get(position).getContent());
        holder.time.setText(data.get(position).getTime());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListenser != null) {
                onItemLongClickListenser.onItemLongClick(data.get(position).getId(),position);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView content;
        private TextView time;


        ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tvContent);
            time = itemView.findViewById(R.id.tvTime);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListenser {
        void onItemLongClick(int id,int postion);
    }
}
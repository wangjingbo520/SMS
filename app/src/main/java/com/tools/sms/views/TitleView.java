package com.tools.sms.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tools.sms.R;


/**
 * @author Bobo
 * @date 2019/9/22 0022
 * describe
 */
public class TitleView extends FrameLayout {
    private TextView tvTitle;
    private FrameLayout fl_back;
    private TextView tv_right;
    private Activity mContext;

    public TitleView(Context context) {
        super(context);
    }

    public void setBackfinishListenser(Activity context) {
        this.mContext = context;
    }


    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.base_title, this);
        fl_back = view.findViewById(R.id.fl_back);
        tvTitle = view.findViewById(R.id.tv_title);
        tv_right = view.findViewById(R.id.tv_right);
        fl_back.setOnClickListener(v -> {
            if (mContext != null) {
                mContext.finish();
            }
        });
    }

    public View getBackView() {
        return fl_back;
    }


    public void setBackImageGone(boolean b) {
        if (b) {
            fl_back.setVisibility(GONE);
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setRightTitle(String title) {
        tv_right.setText(title);
    }

    public String getRightText() {
        return tv_right.getText().toString();
    }

    public View getRightView() {
        return tv_right;
    }


}

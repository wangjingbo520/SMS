package com.tools.sms.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tools.sms.R;

/**
 * @author wjb（C）
 * describe
 */
public class UpdateDialog extends BaseDialog {
    private String url = "";
    private TextView tvContent;

    public UpdateDialog(Context context, LisentenserDilog lisentenserDilog) {
        super(context);
        this.lisentenserDilog = lisentenserDilog;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private LisentenserDilog lisentenserDilog;

    public interface LisentenserDilog {
        void update(String url);
    }


    @Override
    protected View getDefaultView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_update_dialog_plentiful, null);
        tvContent = v.findViewById(R.id.tv_update_content);
        v.findViewById(R.id.btn_update_sure).setOnClickListener(v1 -> {
            if (lisentenserDilog != null) {
                if (!TextUtils.isEmpty(url)) {
                    lisentenserDilog.update(url);
                }
                dismiss();
            }
        });

        v.findViewById(R.id.btn_update_cancel).setOnClickListener(v12 -> dismiss());
        return v;
    }

    public void setContent(String content){
        tvContent.setText(content);
    }
}

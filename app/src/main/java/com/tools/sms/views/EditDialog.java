package com.tools.sms.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tools.sms.R;
import com.tools.sms.tools.ToastUtil;

/**
 * @author wjb（C）
 * describe
 */
public class EditDialog extends BaseDialog {
    private String username;

    public EditDialog(Context context, LisentenserDilog lisentenserDilog, String username) {
        super(context);
        this.lisentenserDilog = lisentenserDilog;
        this.username = username;
    }

    private LisentenserDilog lisentenserDilog;

    public interface LisentenserDilog {
        void updatePassword(String username, String password);
    }


    @Override
    protected View getDefaultView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.edit, null);
        EditText editText = v.findViewById(R.id.etContent);
        v.findViewById(R.id.tv_sure).setOnClickListener(v1 -> {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                ToastUtil.showMessage("请先输入您的新密码");
                return;
            }

            if (lisentenserDilog != null) {
                lisentenserDilog.updatePassword(username, editText.getText().toString());

            }
        });

        v.findViewById(R.id.tv_cancel).setOnClickListener(v12 -> dismiss());
        return v;
    }
}

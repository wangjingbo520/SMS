package com.tools.sms.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tools.sms.R;


/**
 * @author Bobo
 * @date 2019/9/21
 * describe
 */
public abstract class BaseDialog {
    public Dialog mDialog;

    public BaseDialog(Context context) {
        View view = getDefaultView(context);
        mDialog = createDialog(context, view);
    }

    /**
     * 子类重写该方法，即可创建样式相同的对话框。
     *
     * @param context
     * @return
     */
    protected abstract View getDefaultView(Context context);

    private static Dialog createDialog(Context context, View v) {
        Dialog dialog = new Dialog(context, R.style.default_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(v);
        return dialog;
    }

    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }
}

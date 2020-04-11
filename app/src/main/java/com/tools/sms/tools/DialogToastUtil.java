package com.tools.sms.tools;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * @author wjb
 * describe
 */
public class DialogToastUtil {
    private static AlertDialog.Builder builder;

    public static void showDialogToast(Activity mContext, String content) {
        if (mContext == null) {
            return;
        }
        if (builder == null) {
            builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示！！！");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setCancelable(false);
        }
        builder.setMessage(content);
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            mContext.finish();
        });
        builder.show();
    }
}

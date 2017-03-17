package com.dicot.jitprint.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Hunter
 * Describe  进度框的工具方法类
 * on 2017/3/11.
 */
public class ProgressDialogUtil {

    static ProgressDialog progressDialog;

    public static void showProgress(Context con, String title, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(con);
            progressDialog.setCanceledOnTouchOutside(false);
        } else {
            progressDialog.dismiss();
            progressDialog = null;
            progressDialog = new ProgressDialog(con);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void updataProgress(String message) {
        if (progressDialog != null)
            progressDialog.setTitle(message);
    }

    public static void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
package com.dicot.jitprint.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Hunter
 * Describe 对话框工具方法类
 * on 2017/3/11.
 */
public class DialogUtils {
    private Context context;

    public DialogUtils(Context context) {
        this.context = context;
    }

    public void showDialog(String Message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("提示信息").setMessage(Message).setPositiveButton("确定", listener).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showDialogMessage(String Message) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("提示信息")
                .setMessage(Message).setPositiveButton("确定", null).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showDialogListener(String Message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("提示信息")
                .setMessage(Message).setPositiveButton("确定", listener)
                .setNegativeButton("取消", null).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showDialogExit(DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("确认退出 ?").setPositiveButton("确定", listener)
                .setNegativeButton("取消", null).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
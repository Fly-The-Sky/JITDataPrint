package com.dicot.jitprint.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;


public class PopUtils extends PopupWindow {
    PopupWindow popupWindow;
    Context mContext;

    public PopUtils(Context context) {
        mContext = context;
    }

    public void showPopList(View view) {
        // 自适配长、框设置
        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.update();
        popupWindow.setTouchable(true);//控件是否获取焦点
        popupWindow.setFocusable(true);//控件是否可点击
        popupWindow.setOutsideTouchable(false);//控件外部是否可点击
        popupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置此参数，点击外边可消失
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void closePops() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}

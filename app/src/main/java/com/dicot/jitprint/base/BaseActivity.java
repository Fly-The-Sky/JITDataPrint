package com.dicot.jitprint.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dicot.jitprint.R;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Hunter
 * Describe 基础Activity类
 * on 2017/3/11.
 */
public class BaseActivity extends AppCompatActivity {

    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initView();
        initData();
        initListener();
    }

    public void init() {
        hideTitleBar();
    }

    /**
     * 初始化View方法
     */
    public void initView() {
    }

    /**
     * 初始化基础数据方法
     */
    public void initData() {

    }

    /**
     * 初始化事件监听方法
     */
    public void initListener() {
    }

    public void setActionTitle(String title) {
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
    }


    /**
     * 去掉默认的导航栏
     */
    private void hideTitleBar() {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    /**
     * 设置返回按鈕
     */
    public void setBack() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayShowHomeEnabled(true);////使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
        getSupportActionBar().setHomeButtonEnabled(true);//这个小于4.0版本的默认值为true的。但是在4.0及其以上是false，该方法的作用：决定左上角的图标是否可以点击。没有向左的小图标
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home://返回按钮设置
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

package com.dicot.jitprint.activity.main;

import com.dicot.jitprint.R;
import com.dicot.jitprint.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by Hunter
 * on 2017/3/13.
 * Describe 打印机打印功能模块
 */
@ContentView(R.layout.activity_print)
public class PrintModuleActivity extends BaseActivity {
    @Override
    public void init() {
        super.init();
        x.view().inject(this);
        setActionTitle("打印功能模块");
        setBack();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initListener() {
        super.initListener();
    }
}

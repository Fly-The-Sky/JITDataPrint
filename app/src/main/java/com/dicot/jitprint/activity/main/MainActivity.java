package com.dicot.jitprint.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dicot.jitprint.R;
import com.dicot.jitprint.adapter.RecyclerAdapter;
import com.dicot.jitprint.base.BaseActivity;
import com.dicot.jitprint.print.DeviceList;
import com.dicot.jitprint.utils.AppConst;
import com.dicot.jitprint.utils.MyUtil;
import com.dicot.jitprint.utils.PrefTool;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import asp.lib.bt.BluetoothState;

import static com.dicot.jitprint.R.id.fab;


/**
 * Created by Hunter
 * Describe 主菜单功能界面
 * on 2017/3/11.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @ViewInject(value = R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(value = fab)
    private FloatingActionButton mFloatingActionButton;
    @ViewInject(value = R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;
    @ViewInject(value = R.id.nav_view)
    private NavigationView mNavigationView;
    @ViewInject(value = R.id.main_recyclerview_module)
    RecyclerView mRecyclerView;
    RecyclerAdapter mRecAdapter;

    List<String> mLstData;

    @Override
    public void init() {
        super.init();
        x.view().inject(this);
    }

    /**
     * 初始化toolsBar系统标题栏
     */
    public void initData() {

        mLstData = new ArrayList<>();
        mLstData.add("便签打印");
        mLstData.add("短信打印");
        mLstData.add("微信打印");
        mLstData.add("QQ打印");
        mLstData.add("备忘录打印");
        mLstData.add("相册打印");
        try {
            //设置导航栏标题
            mToolbar.setTitle("主菜单");
            setSupportActionBar(mToolbar);

            //设置滑动导航栏
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.setDrawerListener(toggle);
            toggle.syncState();
            initRecyclerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);

        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        mRecAdapter = new RecyclerAdapter(this, mLstData);
        mRecyclerView.setAdapter(mRecAdapter);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //分割线的效果
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initListener() {
        super.initListener();
        //设置界面浮动按钮
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                            .setAction("Action", null).show();
            }
        });
        //滑动导航栏列表点击事件
        mNavigationView.setNavigationItemSelectedListener(this);

        mRecAdapter.setOnItemClickLitener(new RecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                switch (position) {
                    case 0:
                        MyUtil.showToast(MainActivity.this, mLstData.get(position) + "");
                        intent = new Intent(MainActivity.this, PrintTextActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        MyUtil.showToast(MainActivity.this, mLstData.get(position) + "");
                        intent = new Intent(MainActivity.this, PrintTextActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        MyUtil.showToast(MainActivity.this, mLstData.get(position) + "");
                        intent = new Intent(MainActivity.this, PrintTextActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        MyUtil.showToast(MainActivity.this, mLstData.get(position) + "");
                        intent = new Intent(MainActivity.this, PrintTextActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        MyUtil.showToast(MainActivity.this, mLstData.get(position) + "");
                        intent = new Intent(MainActivity.this, PrintTextActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        MyUtil.showToast(MainActivity.this, mLstData.get(position) + "");
                        intent = new Intent(MainActivity.this, PrintPictrueActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 系统导航抽屉的点击方法
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 抽屉导航的点击方法
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case 1:
                break;
            case R.id.nav_camera://
                // Handle the camera action
                break;
            case R.id.nav_gallery://

                break;
            case R.id.nav_slideshow://

                break;
            case R.id.nav_print_set://
                Intent intent = new Intent(this, DeviceList.class);
                startActivity(intent);
                break;
            case R.id.nav_about://关于

                break;
            case R.id.nav_exit://退出
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            switch (resultCode) {
                case RESULT_OK:
                    Bundle b = data.getExtras(); // data为B中回传的Intent
                    PrefTool prefTool = new PrefTool(this, AppConst.PrintPrefInfo.PrintDefault);
                    prefTool.setStringSave(AppConst.PrintPrefInfo.PrintName, b.getString("PrinterName"));
                    prefTool.setStringSave(AppConst.PrintPrefInfo.PrintAddress, b.getString("PrinterAddress"));
                    break;
                default:
                    break;
            }
        }
    }


}

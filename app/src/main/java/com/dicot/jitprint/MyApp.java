package com.dicot.jitprint;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.dicot.jitprint.utils.CrashHandler;

import org.xutils.DbManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hunter
 * Describe
 * on 2017/3/11.
 */

public class MyApp extends Application {
    private static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init();

        //        initNoHttp();
        initDbManager();
    }

    //        private void initNoHttp() {
    //            //初始化NoHttp
    //            NoHttp.initialize(this, new NoHttp.Config()
    //                    .setConnectTimeout(5 * 1000) // 全局连接超时时间，单位毫秒。
    //                    .setReadTimeout(5 * 1000) // 全局服务器响应超时时间，单位毫秒。
    //                    .setNetworkExecutor(new OkHttpNetworkExecutor())  // 使用OkHttp做网络层
    //            );
    //        }

    private void initDbManager() {
        org.xutils.x.Ext.init(this);
        org.xutils.x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.
        // x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        daoConfig = new DbManager.DaoConfig().
                setDbName("jitprint.db")// 创建数据库的名称
                .setDbVersion(1)// 数据库版本号
                .setAllowTransaction(true)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                                       @Override
                                       public void onDbOpened(DbManager db) {
                                           db.getDatabase().enableWriteAheadLogging();
                                           // 开启WAL, 对写入加速提升巨大
                                       }
                                   }

                ).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                                           @Override
                                           public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                                               // TODO: ...
                                           }
                                       }

                );// 数据库更新操作
    }


    /**
     * 重启当前应用
     */
    public static void restart() {
        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static Thread mMainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static Handler mHandler;//主线程Handler

    public static Context getmContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        MyApp.mContext = mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static void setMainThread(Thread mMainThread) {
        MyApp.mMainThread = mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThreadId(long mMainThreadId) {
        MyApp.mMainThreadId = mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static void setMainLooper(Looper mMainLooper) {
        MyApp.mMainLooper = mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void setMainHandler(Handler mHandler) {
        MyApp.mHandler = mHandler;
    }


    public static List<Activity> activities = new LinkedList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 完全退出
     */
    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }
}

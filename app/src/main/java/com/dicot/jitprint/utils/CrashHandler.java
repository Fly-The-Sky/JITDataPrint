
package com.dicot.jitprint.utils;


import com.dicot.jitprint.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * Created by Hunter
 * Describe 捕获并处理那些没有用try...catch捕获的异常
 * on 2017/3/11.
 * @author Sogrey
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    String logSavePath;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
    /**
     * 单例模式 对象
     */
    private static CrashHandler sInstance;

    private CrashHandler() {
    }

    /**
     * 单例模式 一个类最多只能有一个实例 1、有一个私有静态成员 2、有一个公开静态方法getInstance得到这个私有静态成员
     * 3、有一个私有的构造方法（不允许被实例化）
     */
    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandler();
                }
            }
        }
        return sInstance;
    }


    /**
     * 初始化
     */
    public void init() {
        /* 获取默认未捕获异常的Handler（default exception handler），初始化捕获对象 */
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        /* 为线程设置默认默认未捕获异常的Handler监听接口 */
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    /**
     * 捕获异常
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (mDefaultExceptionHandler != null && (BuildConfig.DEBUG || (!outputException(ex)))) {
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 把异常输出到文件 ,字符流
     */
    private boolean outputException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        logSavePath = AppConst.pathLog + DateUtil.getFormatFileTime() + ".log";
        File dir = new File(AppConst.pathLog);
        if (!dir.exists())
            dir.mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(logSavePath);
            fos.write(formatStackTrace(ex).getBytes());
            fos.close();
            // MailUtils.sendEmail(logSavePath);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /***
     * 格式化堆栈信息
     */
    public String formatStackTrace(Throwable throwable) {
        if (throwable == null)
            return "";
        String rtn = throwable.getStackTrace().toString();
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            writer.flush();
            rtn = writer.toString();
            printWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        }
        return rtn;
    }
}

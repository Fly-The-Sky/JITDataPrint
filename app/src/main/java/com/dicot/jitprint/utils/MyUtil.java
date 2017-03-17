package com.dicot.jitprint.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Hunter
 * Describe  基础设备信息方法
 * on 2017/3/11.
 */
public class MyUtil {

    NumberFormat nf = new DecimalFormat("0.00");
    static PackageManager mPManager;

    // 判断字符串是否为�?,不是返回true
    public static boolean isEmpty(String input) {
        if (input == null || input.length() == 0 || "".equals(input) || "null".equals(input))
            return true;
        else
            return false;
    }

    // 判断字符串是否为"null",不是返回本身，使得话返回""
    public static String isNull(String input) {
        if (input == null || "".equals(input) || input.equals("null"))
            return "";
        else
            return input;
    }

    // 判断字符串是否为"null",不是返回本身，使得话返回""
    public static Double toDouble(String input) {
        BigDecimal bd = new BigDecimal(input);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    // 提示信息的显�?
    public static void showToast(Context context, String textStr) {
        try {
            Toast.makeText(context, textStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getVersionName(Context context) {
        try {
            mPManager = context.getPackageManager();
            PackageInfo info = mPManager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }

//    /**
//     * 获取当前应用程序的版本号
//     */
//    public static String getDeviceId() {
//        try {
//            DeviceManager manager = new DeviceManager();
//            return manager.getDeviceId();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return " ";
//        }
//    }

    /**
     * 判断当前网路是否可用
     */
    public static boolean isNetworkConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 半角转换为全�?
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}

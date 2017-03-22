package com.dicot.jitprint.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Hunter
 * Describe  头文件存储工具方法类
 * on 2017/3/11.
 */
public class PrefTool {
    public static SharedPreferences preferences;

    public PrefTool(Context context, String prefname) {
        preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
    }

    /**
     * 获取配置参数 String
     */
    public String getStringPerf(String name, String defValues) {
        synchronized (preferences) {
            return preferences.getString(name, defValues);
        }
    }

    /**
     * String 设置配置参数
     */
    public void setStringSave(String name, String values) {
        Editor editor = preferences.edit();
        editor.putString(name, values);
        editor.commit();
    }

    /**
     * int,获取配置参数
     */
    public int getIntPref(String name, int defValues) {
        synchronized (preferences) {
            return preferences.getInt(name, defValues);
        }
    }

    /**
     * int,设置参数
     */
    public void setIntSave(String name, int value) {
        synchronized (preferences) {
            Editor editor = preferences.edit();
            editor.putInt(name, value);
            editor.commit();
        }
    }

    /**
     * boolean类型,获取配置参数
     */
    public boolean getBooleanPref(String name, boolean defValues) {
        synchronized (preferences) {
            return preferences.getBoolean(name, defValues);
        }
    }

    /**
     * boolean类型,设置参数
     */
    public void setBooleanSave(String name, boolean value) {
        synchronized (preferences) {
            Editor editor = preferences.edit();
            editor.putBoolean(name, value);
            editor.commit();
        }
    }
}

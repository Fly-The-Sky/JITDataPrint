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

	public PrefTool() {
		// TODO Auto-generated constructor stub
	}

	/** 获取配置参数 String */
	public static String getStringPerf(Context context, String prefname, String name, String defValues) {
		preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getString(name, defValues);
		}
	}

	/** String 设置配置参数 */
	public static void setStringSave(Context context, String prefname, String name, String values) {
		preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(name, values);
		editor.commit();
	}

	/**
	 * int,获取配置参数
	 * 
	 */
	public static int getIntPref(Context context, String prefname, String name, int defValues) {
		preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getInt(name, defValues);
		}
	}

	/** int,设置参数 */
	public static void setIntSave(Context context, String prefname, String name, int value) {
		preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putInt(name, value);
			editor.commit();
		}
	}

	/** boolean类型,获取配置参数 */
	public static boolean getBooleanPref(Context context, String prefname, String name, boolean defValues) {
		preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
		synchronized (preferences) {
			return preferences.getBoolean(name, defValues);
		}
	}

	/** boolean类型,设置参数 */
	public static void setBooleanSave(Context context, String prefname, String name, boolean value) {
		preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
		synchronized (preferences) {
			Editor editor = preferences.edit();
			editor.putBoolean(name, value);
			editor.commit();
		}
	}
}

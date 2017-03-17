package com.dicot.jitprint.utils;

/** 点击的方法类 */
public class PressUtil {
	private static long lastClickTime;

	/**按钮连续重复点击的方�? */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();

		// 判断如果时间两次点击的时间小�?2000毫秒则不予以处理
		if (time - lastClickTime < 2000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/** 连续点击�?出的方法 */
	public static boolean isFastExitClick() {
		long time = System.currentTimeMillis();

		// 判断如果时间两次点击的时间小�?1000毫秒则不予以处理
		if (time - lastClickTime < 1000) {
			return true;
		} else {
			lastClickTime = time;
			return false;
		}
	}

}

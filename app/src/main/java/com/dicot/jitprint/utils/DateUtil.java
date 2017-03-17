package com.dicot.jitprint.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hunter
 * Describe 时间功能描述方法
 * on 2017/3/11.
 * @version 1.0
 */
public class DateUtil {

    public static Date date = null;

    public static DateFormat dateFormat = null;

    public static Calendar calendar = null;

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日�?
     * @param format  String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        try {
            dateFormat = new SimpleDateFormat(format);
            if ((!dateStr.equals("")) && (dateStr.length() < format.length())) {
                dateStr += format.substring(dateStr.length()).replaceAll("[YyMmDdHhSs]", "0");
            }
            date = (Date) dateFormat.parse(dateStr);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期：YYYY/MM/DD 格式
     * @return Date
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期：yyyy-MM-dd HH:mm:ss 格式
     * @return Date
     */
    public static Date parseCompleteDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static String StringToFormat(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr0 = "";
        try {
            Date strtodate = df.parse(dateStr);
            dateStr0 = df.format(strtodate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr0;
    }

    public static String StringToTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr0 = "";
        try {
            Date strtodate = df.parse(dateStr);
            dateStr0 = df.format(strtodate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr0;
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return 返回字符型日�?
     */
    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                dateFormat = new SimpleDateFormat(format);
                result = dateFormat.format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 功能描述
     *
     * @param date Date 日期
     * @return
     */
    public static String format(Date date) {
        return format(date, "yyyy/MM/dd");
    }

    /**
     * 功能描述：返回年�?
     *
     * @param date Date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 功能描述：返回月�?
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日�?
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能描述：返回小�?
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分�?
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 功能描述：返回毫�?
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 功能描述：返回字符型日期
     *
     * @param date 日期
     * @return 返回字符型日期yyyy/MM/dd 格式
     */
    public static String getDate(Date date) {
        return format(date, "yyyy/MM/dd");
    }

    /**
     * 功能描述：返回字符型日期
     *
     * @param date 日期
     * @return 返回字符型日期yyyy-MM-dd 格式
     */
    public static String getDate1(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 功能描述：返回字符型时间
     *
     * @param date Date 日期
     * @return 返回字符型时间HH:mm:ss 格式
     */
    public static String getTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date Date 日期
     * @return 返回字符型日期时间yyyy/MM/dd HH:mm:ss 格式
     */
    public static String getDateTime(Date date) {
        return format(date, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date Date 日期
     * @return 返回字符型日期时间yyyy-MM-dd HH:mm:ss 格式
     */
    public static String getDateTime1(Date date) {
        return format(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date Date 日期
     * @return 返回字符型日期时间yyyyMMddHHmmss 格式
     */
    public static String getDateTime2(Date date) {
        return format(date, "yyyyMMddHHmmss");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date Date 日期
     * @return 返回字符型日期时间yyyyMMdd 格式
     */
    public static String getDateTime3(Date date) {
        return format(date, "yyyyMMdd");
    }

    /**
     * 获取指定日后 后 dayAddNum 天的 日期
     *
     * @param day         日期，格式为String："2014-10-22";
     * @param dayMinusNum 增加天数 格式为int;
     * @return
     */
    public static String getDiffDateStr(String day, int dayMinusNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() - dayMinusNum * 24 * 60 * 60 * 1000);
        String dateOk = df.format(newDate2);
        return dateOk;
    }

    /**
     * 功能描述：日期相�???
     *
     * @param date Date 日期
     * @param day  int 天数
     * @return 返回相加后的日期
     */
    public static Date addDate(Date date, int day) {
        calendar = Calendar.getInstance();
        long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    /**
     * 功能描述：日期相�???
     *
     * @param date Date 日期
     * @param day  Date 日期
     * @return 返回相减后的日期
     */
    public static Date diffDate(Date date, int day) {
        calendar = Calendar.getInstance();
        long millis = getMillis(date) - ((long) day) * 24 * 3600 * 1000;
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    /**
     * 功能描述：取得指定月份的第一�?
     *
     * @param strdate String 字符型日�?
     * @return String yyyy-MM-dd 格式
     */
    public static String getMonthBegin(String strdate) {
        date = parseDate(strdate);
        return format(date, "yyyy-MM") + "-01";
    }

    /**
     * 功能描述：取得指定月份的�?后一�?
     *
     * @param strdate String 字符型日�?
     * @return String 日期字符yyyy-MM-dd格式
     */
    public static String getMonthEnd(String strdate) {
        date = parseDate(getMonthBegin(strdate));
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return formatDate(calendar.getTime());
    }

    /**
     * 功能描述：常用的格式化日�?
     *
     * @param date Date 日期
     * @return String 日期字符串yyyy-MM-dd格式
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, "yyyy-MM-dd");
    }

    /**
     * 功能描述：以指定的格式来格式化日�?
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return String 日期字符�?
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 通过和今日的天数差异获得date
     *
     * @param count
     * @return date
     */
    public static Date getDate(int count) {
        Calendar d = Calendar.getInstance();
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        d.set(year, month, day + count);
        Date date = d.getTime();
        return date;
    }

    /**
     * 将yyyy/MM/dd HH:mm:ss 格式转成yyyy-MM-dd HH:mm:ss 格式
     *
     * @param dateString
     * @return
     */
    public static String transformDateFormat(String dateString) {
        if (!(dateString != null && !dateString.equals(""))) {
            return "";
        }
        String[] partArr = dateString.split(" |-|/");
        for (int i = 1; i < 3; i++) {
            if (partArr[i].length() == 1) {
                partArr[i] = "0" + partArr[i];
            }
        }
        return partArr[0] + "-" + partArr[1] + "-" + partArr[2] + " " + partArr[3];
    }

    /**
     * yyyyMMddHHmmss 格式转成yyyy-MM-dd HH:mm:ss 格式
     *
     * @param dateString
     * @return
     */
    public static String transformDateFormat1(String dateString) {
        if (!(dateString != null && !dateString.equals(""))) {
            return "";
        }
        Date date = parseDate(dateString, "yyyyMMddHHmmss");
        return getDateTime1(date);
    }

    /**
     * @param type 0:当天日期：昨天，2：本周，3：上�?
     * @return
     */
    public static String getSpecialDateStrStart(int type) {
        Calendar d = Calendar.getInstance();
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        int whatDay = d.get(Calendar.DAY_OF_WEEK);
        if (whatDay == 1) {
            whatDay = 7;
        } else {
            whatDay--;
        }

        switch (type) {
            case 0:

                break;
            case 1:
                d.set(year, month, day - 1);
                break;
            case 2:
                d.set(year, month, day - (whatDay - 1));
                break;
            case 3:
                d.set(year, month, day - (whatDay - 1) - 7);
                break;
        }

        Date date = d.getTime();
        return getDate1(date) + " 00:00:00";
    }

    /**
     * @param type 0:当天日期：昨天，2：本周，3：上月
     * @return
     */
    public static String getSpecialDateStrEnd(int type) {
        Calendar d = Calendar.getInstance();
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        int whatDay = d.get(Calendar.DAY_OF_WEEK);
        if (whatDay == 1) {
            whatDay = 7;
        } else {
            whatDay--;
        }

        switch (type) {
            case 0:
                break;
            case 1:
                d.set(year, month, day - 1);
                break;
            case 2:
                d.set(year, month, day + (7 - whatDay));
                break;
            case 3:
                d.set(year, month, day - (whatDay));
                break;
        }

        Date date = d.getTime();
        return getDate1(date) + " 23:59:59";
    }

    /**
     * 获取两个日期之间的时间间隔
     *
     * @param data1 yyyy-MM-dd HH:mm:ss
     * @param data2 yyyy-MM-dd HH:mm:ss
     */
    public static String getBetweenData(String data1, String data2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = "0 小时 0 分钟";
        try {
            Date d1 = sdf.parse(data2);
            Date d2 = sdf.parse(data1);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long hours = diff / (1000 * 60 * 60);
            long minutes = (diff - hours * (1000 * 60 * 60)) / (1000 * 60);
            if (minutes >= 1) {
                hours += 1;
            }
            time = hours + "时" + minutes + "分";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 获取当前的年月日 HH:mm
     *
     * @return
     */
    public static String getHM() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(now);
    }

    /**
     * 获取当前的年月日yyMMdd
     *
     * @return
     */
    public static String getYmd() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        return dateFormat.format(now);
    }

    /**
     * 获取当前的年月日 yyyy-MM-dd
     *
     * @return
     */
    public static String getYMD() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(now);
    }

    /**
     * 获取日期时间格式为yyyy/MM/dd HH:mm:ss的时间
     */
    public static String getFormatTime() {
        Date date = new Date();
        return getDateTime(date);
    }

    /**
     * 获取日期时间格式为yyyy-MM-dd HH:mm:ss的时间
     */
    public static String getFormatNowTime() {
        Date date = new Date();
        return getDateTime1(date);
    }

    /**
     * 获取日期时间格式为yyyyMMddHHmmss的时间
     */
    public static String getFormatFileTime() {
        Date date = new Date();
        return getDateTime2(date);
    }

    /**
     * 获取日期时间格式为yyyyMMdd的时间
     */
    public static String getYMDDate() {
        Date date = new Date();
        return getDateTime3(date);
    }

    /**
     * 获取日期时间格式为yyyy/MM/dd的时间
     */
    public static String getYMDDate1() {
        Date date = new Date();
        return getDate(date);
    }

    /**
     * 获取日期时间格式为yyyy-MM-dd的时间
     */
    public static String getYMDDate2() {
        Date date = new Date();
        return getDate1(date);
    }

}

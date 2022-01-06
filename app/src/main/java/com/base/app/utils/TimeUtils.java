package com.base.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    /**
     * 获取当前时间
     * @return
     */
    public  static  String getCurrentTime(){
        return  System.currentTimeMillis()+"";
    }

    /**
     * 格式化当前时间
     * @return
     */
    public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public  static  String getTime()
    {
        return  sf.format(new Date());
    }

    /**
     * 获取当前时间的时间戳
     * @return
     */
    public static long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

    private static Calendar cd = Calendar.getInstance();

    /**
     * 获取年
     * @return
     */
    public static int getYear(){
        return  cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     * @return
     */
    public static int getMonth(){
        return  cd.get(Calendar.MONTH)+1;
    }

    /**
     * 获取日
     * @return
     */
    public static int getDay(){
        return  cd.get(Calendar.DATE);
    }

    /**
     * 获取时
     * @return
     */
    public static int getHour(){
        return  cd.get(Calendar.HOUR);
    }

    /**
     * 获取分
     * @return
     */
    public static int getMinute(){
        return  cd.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     * @return
     */
    public static int getSecond(){
        return cd.get(Calendar.SECOND);
    }

    /**
     * 时间戳转时间
     * @param milSecond
     * @return
     */
    public static String getDateToStr(long milSecond) {
        Date date = new Date(milSecond* 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 字符串格式化
     */
    public static String getDateForStr(String time,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            return new SimpleDateFormat(format).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    public static String timeFormatStr(Calendar calendar, String strDay) {
        String tempStr = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 11) {
            tempStr = "下午" + " " + strDay;
        } else {
            tempStr = "上午" + " " + strDay;
        }
        return tempStr;
    }

    /**
     * 时间转化为星期
     *
     * @param indexOfWeek 星期的第几天
     */
    public static String getWeekDayStr(int indexOfWeek) {
        String weekDayStr = "";
        switch (indexOfWeek) {
            case 1:
                weekDayStr = "星期日";
                break;
            case 2:
                weekDayStr = "星期一";
                break;
            case 3:
                weekDayStr = "星期二";
                break;
            case 4:
                weekDayStr = "星期三";
                break;
            case 5:
                weekDayStr = "星期四";
                break;
            case 6:
                weekDayStr = "星期五";
                break;
            case 7:
                weekDayStr = "星期六";
                break;
        }
        return weekDayStr;
    }

    /**
     * 时间转化为显示字符串，24小时内显示昨天 + 时间，一周内显示周几
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0)
            return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            return "昨天";
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            if (calendar.before(inputTime)) {
                return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK));
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                int year = inputTime.get(Calendar.YEAR);
                int month = inputTime.get(Calendar.MONTH) + 1;
                int day = inputTime.get(Calendar.DAY_OF_MONTH);
                return year + "-" + month + "-" + day;
            }
        }
    }
}

package com.tencent.qcloud.tim.tuikit.live.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 290243232 on 2017/5/4.
 */

public class DateUtils {
    public static String toDate(String timeStr) {
        String temp = "";
        try {
            long now = System.currentTimeMillis() / 1000;
            long publish = Long.parseLong(timeStr);
            long diff = now - publish;
            long months = diff / (60 * 60 * 24*30);
            long days = diff / (60 * 60 * 24);
            long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
            long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60;
            if (months > 0) {
                temp = getDateToString(timeStr);
            } else if (days > 0&&days<=3) {
                temp = days + "天前";
            }else if(days>3){
                temp = getDateToString(timeStr);
            }
            else if (hours > 0) {
                temp = hours + "小时前";
            } else {
                if(minutes==0){
                    temp="刚刚";
                }else {
                    temp = minutes + "分钟前";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    /*时间戳转换成字符窜*/
    public static String getDateToString(String time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    public static String getParseTime(long value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd") ;
        String time = format.format(new Date(value * 1000L));
        Log.d("xxx-------->", "转换后时间: " + time );
        return time;
    }

    public static boolean isSameData(String currentTime,String lastTime) {
        try {
            Calendar nowCal = Calendar.getInstance();
            Calendar dataCal = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            Long nowLong = new Long(currentTime);
            Long dataLong = new Long(lastTime);
            String data1 = df1.format(nowLong);
            String data2 = df2.format(dataLong);
            Date now = df1.parse(data1);
            Date date = df2.parse(data2);
            nowCal.setTime(now);
            dataCal.setTime(date);
            return isSameDay(nowCal, dataCal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(cal1 != null && cal2 != null) {
            return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }
    }

    /**
     * 计算时间差
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            System.out.println("时间相差：" + day + "天" + hour + "小时" + min
                    + "分钟" + sec + "秒。");
            if (day>=1) {
                return day;
            }else {
                if (day==0) {
                    return 1;
                }else {
                    return 0;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 计算时间大小
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}

package com.car.bolang.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Migu Kindle Perfect
 * Author:	zhusiliang    Version:1.0.0   Date:2019/4/27
 * Description:
 * Others:
 */
public class TimeUtils {
    public static String getTime(long time, String format) {
        SimpleDateFormat sdr = new SimpleDateFormat(format);
        @SuppressWarnings("unused")
        //int i = Integer.parseInt(time);
                String times = sdr.format(new Date(time));
        return times;
    }


    public static String getTime(String time, String format) {
        long showTime = 0;
        try {
            showTime = Long.parseLong(time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat sdr = new SimpleDateFormat(format);
        @SuppressWarnings("unused")
        //int i = Integer.parseInt(time);
                String times = sdr.format(new Date(showTime));
        return times;
    }

    /**
     * 根据字符串获取long类型
     *
     * @param DateTime
     * @param format
     * @return
     * @throws Exception
     */
    public static long getToLong(String DateTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date time = null;
        try {
            time = sdf.parse(DateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time.getTime();
    }

    public static long getDayLong() {
        String timeStr = getTime(System.currentTimeMillis(), "yyyy-MM-dd");
        timeStr = timeStr + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        try {
            time = sdf.parse(timeStr);
            return time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeStr(String second) {
        StringBuffer stringBuffer = new StringBuffer();
        if (TextUtils.isEmpty(second)) {
            return "0min";
        }
        try {
            int secondNum = Integer.parseInt(second);
            if (secondNum <= 0) {
                return stringBuffer.append("0min").toString();
            }
            if (secondNum < 60) {
                return stringBuffer.append(second).append("s").toString();
            }
            if (secondNum < 60 * 60) {
                int minNum = secondNum / 60;
                return stringBuffer.append(minNum).append("min").toString();
            }
            int hourNum = secondNum / (60 * 60);
            int minNum = secondNum / 60 - hourNum * 60;
            return stringBuffer.append(hourNum).append("h").append(minNum).append("min").toString();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "00:00";

    }

    public static int getCurrentWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置星期一为一周开始的第一天
        calendar.setMinimalDaysInFirstWeek(4);//可以不用设置
        calendar.setTimeInMillis(System.currentTimeMillis());//获得当前的时间戳
        //获得当前日期属于今年的第几周
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

}


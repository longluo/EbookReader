package com.longluo.ebookreader.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {

    public static String getStringData(String strData) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        return date.toLocaleString().split(" ")[0];//切割掉不要的时分秒数据
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd");
    }

    public static boolean compareDate(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(startDate);
            Date dt2 = df.parse(endDate);
            //System.out.println("TimeUtils.compareDate dt1.getTime()=" + dt1.getTime() + "  dt2.getTime()=" + dt2.getTime());
            return dt1.getTime() < dt2.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 格式化时间
     */
    public static String getTimePickerView(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd HH-mm
        return format.format(date);
    }
}

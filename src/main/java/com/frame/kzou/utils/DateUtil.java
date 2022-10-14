package com.frame.kzou.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.frame.kzou.enums.DateFormatEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/7
 * Time: 22:02
 * Description:
 */
public class DateUtil {
    /**
     * 2022-10-07 22:07
     */
    private static final DateFormat normalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String getNow(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String format(DateFormatEnum dateFormat, Date date) {
        DateFormat format = dateFormat.getValue();
        return format.format(date);
    }

    public static Date unFormat(DateFormatEnum dateFormat, String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        DateFormat format = dateFormat.getValue();
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Date getNow() {
        return new Date();
    }

    public static long getDistance(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    public static long getDistanceToNow(Date date) {
        return getDistance(date, getNow());
    }

}

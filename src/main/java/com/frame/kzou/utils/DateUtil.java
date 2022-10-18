package com.frame.kzou.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.frame.kzou.enums.DateFormatEnum;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/7
 * Time: 22:02
 * Description: 时间工具类
 */
public class DateUtil {

    private DateUtil() {}

    /**
     * 2022-10-07 22:07
     */
    public static String getNow(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * date -> string
     * @param dateFormat    指定格式类型
     * @param date          date
     * @return              符合指定格式的string类型的date
     */
    public static String format(DateFormatEnum dateFormat, Date date) {
        DateFormat format = dateFormat.getValue();
        return format.format(date);
    }


    /**
     * string -> date
     * @param dateFormat    指定格式类型
     * @param date          符合指定格式的string类型的date
     * @return              date
     */
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

    public static String getNowFormat(DateFormatEnum dateFormat) {
        return format(dateFormat, getNow());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 时间差
     * @param date1     date1
     * @param date2     date2
     * @return
     */
    public static long getDistance(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    public static long getDistanceToNow(Date date) {
        return getDistance(date, getNow());
    }

    public static long getDistanceToNow(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(LocalDateTime.now(), date).toMillis();
    }
}

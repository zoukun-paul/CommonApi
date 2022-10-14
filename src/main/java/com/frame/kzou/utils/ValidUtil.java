package com.frame.kzou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/12
 * Time: 13:17
 * Description: 校验工具类
 */
public class ValidUtil {

    private static final Pattern  ipv4Pattern = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)(\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)){3}");

    public static boolean isEmptyString(String str) {
        return str == null && str.length() == 0;
    }

    public static boolean validIp4(String str) {
        if (isEmptyString(str)) {
            return false;
        }
        return ipv4Pattern.matcher(str).matches();
    }
}

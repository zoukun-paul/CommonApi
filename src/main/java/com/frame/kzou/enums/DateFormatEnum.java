package com.frame.kzou.enums;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/8
 * Time: 22:56
 * Description:
 */
public enum DateFormatEnum {
    STANDARD(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),
    STANDARD_IGNORE_S(new SimpleDateFormat("yyyy-MM-dd HH:mm")),
    ;

    public DateFormat getValue() {
        return value;
    }

    private DateFormat value;

    DateFormatEnum(SimpleDateFormat simpleDateFormat) {
        this.value = simpleDateFormat;
    }
}

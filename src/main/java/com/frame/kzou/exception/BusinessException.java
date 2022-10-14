package com.frame.kzou.exception;

import com.frame.kzou.common.CommonResult;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/7
 * Time: 10:03
 * Description: 统一异常基类
 */
public class BusinessException extends RuntimeException {
    /**
     * 提示编码
     */
    private final int code;

    /**
     * 后端提示语
     */
    private final String msg;

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(CommonResult commonResult) {
        this(commonResult.getCode(), commonResult.getMsg());
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

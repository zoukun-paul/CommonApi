package com.frame.kzou.common;

import com.frame.kzou.exception.BusinessException;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.Data;

import javax.swing.text.TabExpander;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/9/19
 * Time: 10:22
 * Description:
 */
@Data
public class ResultResponse<T> implements ResultTemplate {
    private String msg;
    private int code;
    private T data;

    public static  <T> ResultResponse<T> success(T data) {
        ResultResponse<T> result = new ResultResponse<>(CommonResult.SUCCESS);
        result.setData(data);
        return result;
    }

    public static ResultResponse<Void> success() {
        return success(null);
    }


    public ResultResponse(CommonResult commonResult) {
        this.code = commonResult.getCode();
        this.msg = commonResult.getMsg();
    }

    public ResultResponse() { }

    public static ResultResponse<Void> error(CommonResult commonResult) {
        ResultResponse<Void> response = new ResultResponse<>();
        int code = commonResult.getCode();
        String msg = commonResult.getMsg();
        response.setMsg(msg);
        response.setCode(code);
        return response;
    }

    public static ResultResponse<Void> error(BusinessException exception) {
        String msg = exception.getMsg();
        int code = exception.getCode();
        ResultResponse<Void> response = new ResultResponse<>();
        response.setMsg(msg);
        response.setCode(code);
        return response;
    }

    public ResultResponse<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResultResponse<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public ResultResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}

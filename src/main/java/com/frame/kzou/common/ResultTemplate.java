package com.frame.kzou.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/9/19
 * Time: 10:24
 * Description:
 */
public interface ResultTemplate {

    /**
     * 获取响应状态码
     */
    int getCode();

    /**
     * 获取响应描述信息
     */
    String getMsg();
}

package com.frame.kzou.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/9/19
 * Time: 10:32
 * Description:
 */
public enum CommonResult implements ResultTemplate {

    SUCCESS(200, "请求成功"),
    SystemError(500,"系统异常"),
    EmailError(600,"邮箱发送失败"),
    FileNotFoundError(700,"404-文件不存在"),
    ParameterError(800,"参数异常"),
    PermissionError(900,"权限异常"),
    UserNotBindError(901,"请先绑定用户"),
    NoAccessPermissionError(902,"无访问权限"),
    EmailTimeError(1001,"邮件发送时间异常"),
    IpQueryError(1002,"IP查询异常"),
    ;
            ;

    private String msg;
    private int code;


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    CommonResult(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }
}

package com.frame.kzou.business.vo.email;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/6
 * Time: 23:36
 * Description: 邮箱权限信息
 */
@Data
public class EmailSmtpAuthVo {

    @ApiModelProperty(value = "主机名称", required = true)
    private String hostName;

    @ApiModelProperty(value = "SMTP 的端口号", required = false)
    private Integer smtpPort = null;

    @ApiModelProperty(value = "邮箱登录账号名", required = true)
    private String username;

    @ApiModelProperty(value = "密码或密钥", required = true)
    private String password;

    @ApiModelProperty(value = "发送者邮箱账号", required = true)
    private String formEmail;

}

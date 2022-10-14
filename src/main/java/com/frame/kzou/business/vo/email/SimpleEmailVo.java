package com.frame.kzou.business.vo.email;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.frame.kzou.enums.DateFormatEnum;
import com.frame.kzou.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/11
 * Time: 19:16
 * Description:
 */
@Data
public class SimpleEmailVo {

    @ApiModelProperty(value = "权限信息")
    protected EmailSmtpAuthVo emailSmtpAuthVo;

    @ApiModelProperty("邮件主题")
    protected String subject;

    @ApiModelProperty(value = "文本类容")
    protected String content;

    @ApiModelProperty(value = "对方的邮箱账号", required = true)
    protected String[] targetEmail;

    @ApiModelProperty("定时发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    protected Date sendTime;


    public <T extends Email> T of(T t) throws EmailException {
        // 验证信息
        t.setAuthenticator(new DefaultAuthenticator(
                emailSmtpAuthVo.getUsername(),
                emailSmtpAuthVo.getPassword())
        );
        // 邮件主题
        t.setSubject(subject);

        // 邮件主体类容
        t.setMsg(content);

        // 发送者邮箱号
        t.setFrom(emailSmtpAuthVo.getFormEmail());

        t.setHostName(emailSmtpAuthVo.getHostName());

        if (emailSmtpAuthVo.getSmtpPort() != null) {
            t.setSmtpPort(emailSmtpAuthVo.getSmtpPort());
        }

        if (getSendTime() != null) {
            t.setSentDate(getSendTime());
        }

        t.addTo(getTargetEmail());

        return t;
    }

    public SimpleEmail ofSimpleEmail() throws EmailException {
        return of(new SimpleEmail());
    }
}

package com.frame.kzou.business.controller.email;

import com.frame.kzou.business.base.BaseController;
import com.frame.kzou.business.vo.email.EmailInfoVo;
import com.frame.kzou.business.vo.email.EmailSmtpAuthVo;
import com.frame.kzou.business.vo.email.SimpleEmailVo;
import com.frame.kzou.common.CommonResult;
import com.frame.kzou.common.ResultResponse;
import com.frame.kzou.enums.DateFormatEnum;
import com.frame.kzou.exception.BusinessException;
import com.frame.kzou.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.swing.plaf.basic.BasicButtonUI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/9/19
 * Time: 19:26
 * Description:
 * 1. 定时邮件发送
 * 2. 纯文本、附件邮件发送
 * 3. 批量发送
 * 4. 默认邮箱发送
 * 5. 用户登录
 */
@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController extends BaseController {

    @Autowired
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @ApiModelProperty("绑定操作用户")
    @PostMapping("/auth/bind")
    public ResultResponse bindEmailUser(@RequestBody EmailSmtpAuthVo emailSmtpAuthVo) {
        getSession().setAttribute("emailAuth", emailSmtpAuthVo);
        return ResultResponse.success();
    }

    @ApiModelProperty("解绑绑定操作用户")
    @DeleteMapping("/auth/unbind")
    public ResultResponse unbindEmailUser() {
        getSession().removeAttribute("emailAuth");
        return ResultResponse.success();
    }

    @ApiModelProperty("发送简单纯文本邮箱")
    @PostMapping("/simple")
    public ResultResponse sendSimpleTextEmail(@RequestBody SimpleEmailVo emailVo) throws EmailException {
        if (emailVo.getEmailSmtpAuthVo() == null) {
            emailVo.setEmailSmtpAuthVo(getEmailSmtpAuthVo());
        }
        if (emailVo.getEmailSmtpAuthVo() == null) {
            throw new BusinessException(CommonResult.PermissionError);
        }
        sendEmail(emailVo.ofSimpleEmail());
        return ResultResponse.success();
    }

    @ApiModelProperty("发送携带附件的邮箱")
    @PostMapping("/attachment")
    public ResultResponse sendEmailWithAttachmentFile(@RequestBody EmailInfoVo emailInfoVo) throws EmailException {
        return sendSimpleTextEmail(emailInfoVo);
    }

    private EmailSmtpAuthVo getEmailSmtpAuthVo() {
        return (EmailSmtpAuthVo) getSession().getAttribute("emailAuth");
    }

    /**
     * 发送邮件
     * @param email 邮件对象
     * @throws EmailException
     */
    private void sendEmail(Email email) throws EmailException {
        Date sentDate = email.getSentDate(), now = DateUtil.getNow();
        if (sentDate == null) {
            // 直接发送
            email.send();
            log.info("send a email[host={}] at now", email.getHostName());
        }else if (sentDate.before(now)) {
            // 异常
            throw new BusinessException(CommonResult.EmailTimeError);
        }else if (sentDate.after(now)) {
            // 定时发送
            scheduledThreadPoolExecutor.schedule(() -> {
                try {
                    email.send();
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            }, DateUtil.getDistanceToNow(sentDate), TimeUnit.MILLISECONDS);
            log.info("add a email schedule send at {}", DateUtil.format(DateFormatEnum.STANDARD_IGNORE_S, sentDate));
        }else {
            email.send();
            log.info("send a email[host={}] at now", email.getHostName());
        }
    }
}

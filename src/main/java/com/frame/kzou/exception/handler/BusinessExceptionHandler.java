package com.frame.kzou.exception.handler;

import com.frame.kzou.common.CommonResult;
import com.frame.kzou.common.ResultResponse;
import com.frame.kzou.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/7
 * Time: 10:06
 * Description: 异常处理模块
 */

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(EmailException.class)
    public ResultResponse<Void> handleEmailException(MessagingException e) {
        log.error("邮件异常：" + e.getMessage());
        return ResultResponse.error(CommonResult.EmailError);
    }

    @ExceptionHandler(Exception.class)
    public ResultResponse<Void> handleException(Exception e){
        log.error("系统异常："+ e.getMessage());
        return ResultResponse.error(CommonResult.SystemError);
    }

    @ExceptionHandler(BusinessException.class)
    public ResultResponse<Void> handleBusinessException(BusinessException e){
        log.error("BusinessException："+ e.getMessage());
        return ResultResponse.error(e);
    }
}

package com.frame.kzou.business.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/9/16
 * Time: 15:20
 * Description:
 */
@Slf4j
public class BaseController {

    private ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取request对象
     * @return
     */
    public HttpServletRequest getRequest() {
        return Optional.ofNullable(getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取响应对象
     *
     * @return
     */
    public HttpServletResponse getResponse(){
        return Optional.ofNullable(getRequestAttributes())
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }

    /**
     * 获取Session
     *
     * @return
     */
    public HttpSession getSession(){
        return Optional.ofNullable(getRequest())
                .map(HttpServletRequest::getSession)
                .orElse(null);
    }

    public void exportFile(File file, String filename) {
        if (filename == null) {
            filename = file.getName();
        }
        HttpServletResponse response = getResponse();
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setContentType("multipart/form-data");

        try (ServletOutputStream outputStream = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)
        ) {
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exportFile(File file) {
        exportFile(file, null);
    }

    public void setUser(String userId) {
        log.info("session[{}]绑定用户：{}", getSession().getId(), userId);
        getSession().setAttribute("userId", userId);
    }

    public void removeUser() {
        log.info("session[{}]绑定用户", getSession().getId());
        getSession().removeAttribute("userId");
    }

    public String getBindUser() {
        Object userId = getSession().getAttribute("userId");
        log.info("session[{}]获取绑定用户：{}", getSession().getId(), userId);
        return userId == null ? null : userId.toString();
    }
}

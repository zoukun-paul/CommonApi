package com.frame.kzou.business.vo.email;

import com.frame.kzou.utils.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.mail.EmailAttachment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/11
 * Time: 19:17
 * Description: 邮箱附件vo
 */
@Data
@NoArgsConstructor
public class Attachment {

    /** 在线链接*/
    private String path;
    private String name;
    private Date createTime;
    private Integer size;
    private String desc;

    public Attachment(String path, String name, Integer size) {
        this.path = path;
        this.name = name;
        this.createTime = DateUtil.getNow();
        this.size = size;
    }

    public EmailAttachment of(String disposition) {
        EmailAttachment emailAttachment = new EmailAttachment();
        emailAttachment.setDescription(desc);
        emailAttachment.setPath(path);
        emailAttachment.setName(name);
        try {
            emailAttachment.setURL(new URL(path));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        emailAttachment.setDisposition(disposition);
        return emailAttachment;
    }

    public EmailAttachment of() {
        return of(EmailAttachment.ATTACHMENT);
    }


}

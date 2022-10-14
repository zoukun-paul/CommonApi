package com.frame.kzou.business.vo.email;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.mail.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/6
 * Time: 22:37
 * Description: 复杂邮件Vo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailInfoVo extends SimpleEmailVo{

    @ApiModelProperty(value = "附件" ,required = true)
    private List<Attachment> attachments;

    public MultiPartEmail ofMultiPartEmail() throws EmailException{
        MultiPartEmail multiPartEmail = of(new MultiPartEmail());

        multiPartEmail.setBoolHasAttachments(true);

        for (Attachment attachment : attachments) {
            multiPartEmail.attach(attachment.of());
        }

        return multiPartEmail;
    }

}

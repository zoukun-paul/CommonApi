package com.frame.kzou.business.vo.file;

import com.frame.kzou.business.pojo.UserFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 19:29
 * Description:
 */
@Data
@ApiModel("根据Id更新")
public class UserFileUpdateVo {

    @ApiModelProperty(value = "fileId", required = true)
    private Integer id;

    @ApiModelProperty(value = "描述信息")
    private String desc;

    @ApiModelProperty("是否对外开放")
    private Boolean openShare;

    @ApiModelProperty("对外开放的deadline, 默认永久")
    private Date availableTime;

    public UserFile ofUserFile() {
        UserFile userFile = new UserFile();
        BeanUtils.copyProperties(this, userFile);
        return userFile;
    }

}

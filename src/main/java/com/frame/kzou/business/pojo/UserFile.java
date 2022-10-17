package com.frame.kzou.business.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.frame.kzou.config.mybatis.CustomerDataHandler;
import com.frame.kzou.config.mybatis.OpenShareFieldHandler;
import com.frame.kzou.utils.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 11:39
 * Description: 用户文件表对象
 */
@Data
@NoArgsConstructor
@TableName(value = "user_file",autoResultMap = true)
public class UserFile {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 本地存放路径
     */
    private String source;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 描述信息
     */
    private String desc;

    /**
     * 在线链接
     */
    private String online;

    /**
     * 源文件名称
     */
    private String originalName;

    /**
     * 文件大下 ，单位：byte
     */
    private Long size;

    /**
     * 是否是开放的
     */
    @TableField(typeHandler = OpenShareFieldHandler.class)
    private Boolean openShare;

    @TableField(value = "available_time", typeHandler = CustomerDataHandler.class)
    private Date availableTime;

    @TableField(value = "create_time", typeHandler = CustomerDataHandler.class)
    private Date createTime;

    @TableField(value = "update_time", typeHandler = CustomerDataHandler.class)
    private Date updateTime;

    public UserFile(String source, String userId, String online, String originalName, Long size, boolean openShare, Date availableTime) {
        this.source = source;
        this.userId = userId;
        this.online = online;
        this.originalName = originalName;
        this.size = size;
        this.openShare = openShare;
        this.availableTime = availableTime;
        this.createTime = DateUtil.getNow();
        this.updateTime = this.createTime;
    }

    public UserFile(String source, String userId, String online, String originalName, Long size) {
        this(source, userId, online, originalName, size, false, null);
    }
}

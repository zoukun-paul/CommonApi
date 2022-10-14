package com.frame.kzou.business.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.frame.kzou.config.mybatis.CustomerDataHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 11:40
 * Description: 共享文件表对象
 */
@Data
@NoArgsConstructor
@TableName(value = "share_file", autoResultMap = true)
public class ShareFile {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * md5 code
     */
    private String code;

    /**
     *  本地存放地址
     */
    private String source;

    /**
     * 在线访问地址
     */
    private String online;

    /**
     * 文件大小 ，单位 byte
     */
    private Long size;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", typeHandler = CustomerDataHandler.class)
    private Date createTime;

    /**
     * base64 文件内容
     */
    private String data;

    public ShareFile(String code, String source, String online, Long size) {
        this.code = code;
        this.source = source;
        this.online = online;
        this.size = size;
    }
}

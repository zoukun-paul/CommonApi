package com.frame.kzou.business.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.frame.kzou.config.mybatis.CustomerDataHandler;
import com.frame.kzou.utils.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/17
 * Time: 16:09
 * Description:
 *
 *        id            INTEGER primary key autoincrement,
 *        lurl          char(160) not null ,
 *        surl          char (30) not  null ,
 *        click_times   INTEGER   default 0,
 *        create_time   DATETIME
 */
@Data
@NoArgsConstructor
@TableName(value = "short_link", autoResultMap = true)
public class ShortLink {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 长连接
     */
    private String lurl;

    /**
     * 短链接
     */
    private String surl;


    /**
     * 点击率
     */
    private Integer clickTimes;

    /**
     * 短链接 后缀
     */
    private String suffix;

    @TableField(value = "create_time", typeHandler = CustomerDataHandler.class)
    private Date createTime;


    public ShortLink(String lurl, String surl, String suffix ) {
        this.surl = surl;
        this.lurl = lurl;
        this.suffix = suffix;
        this.clickTimes = 0;
        this.createTime = DateUtil.getNow();
    }
}

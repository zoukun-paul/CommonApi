package com.frame.kzou.business.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.frame.kzou.config.mybatis.CustomerDataHandler;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/19
 * Time: 19:36
 * Description: 代理IP pojo
 */
@Data
@TableName(value = "proxy_ip", autoResultMap = true)
public class ProxyIp {


    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * IP
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 状态 100 为健康
     */
    private Integer life;

    public void setType(Integer type) {
        this.type = type;
    }

    public void setType(String type) {
        switch (type) {
            case "普匿": this.type = 1; break;
            case "透明": this.type = 2; break;
            case "高匿": this.type = 3; break;
            default:
                this.type = 0;
        }
        if (this.type == 0) {
            if (type.contains("高匿")) {
                this.type = 3;
            } else if (type.contains("普匿")) {
                this.type = 1;
            }else if (type.contains("透明")) {
                this.type = 2;
            }
        }
    }

    /**
     * 类型：0.未知（默认） 1.普匿 2.透明 3.高匿
     */
    private Integer type = 0;

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public void setProtocol(String protocol) {
        if (protocol.equalsIgnoreCase("http")) {
            this.protocol = 1;
        }else {
            this.protocol = 2;
        }
    }

    /**
     * 协议：1.http（默认） 2.https
     */
    private Integer protocol = 1;

    /**
     * 位置
     */
    private String position;

    /**
     * 来源地址
     */
    private String source;

    /**
     * 校验次数
     */
    private Integer checkTimes = 0;

    /**
     * 创建时间
     */
    @TableField(typeHandler = CustomerDataHandler.class)
    private Date creatTime;

    /**
     * 最后检验时间
     */
    @TableField(typeHandler = CustomerDataHandler.class)
    private Date checkTime;


    public ProxyIp(String ip, Integer port, String source) {
        this.ip = ip;
        this.port = port;
        this.source = source;
        this.life = 50;
        creatTime = new Date();
    }

    public ProxyIp(String ip, Integer port,Integer type, Integer protocol, String position, String source) {
        this.ip = ip;
        this.port = port;
        this.source = source;
        this.life = 50;
        this.position = position;
        this.protocol = protocol;
        this.type = type;
        creatTime = new Date();
    }

    public ProxyIp() {}

}

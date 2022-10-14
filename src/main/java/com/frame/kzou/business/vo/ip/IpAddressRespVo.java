package com.frame.kzou.business.vo.ip;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/12
 * Time: 14:46
 * Description:
 */
@Data
public class IpAddressRespVo {
    /**
     * ip
     */
    @ApiModelProperty("ip")
    private String ip;

    /**
     * ip对应的国家
     */
    @ApiModelProperty("ip对应的国家")
    private String country;

    /**
     * ip对应的地区
     */
    @ApiModelProperty("ip所在的地区")
    private String region;

    /**
     * ip所在的省份
     */
    @ApiModelProperty("ip所在的省份")
    private String province;

    /**
     * ip所在的城市
     */
    @ApiModelProperty("ip所在的城市")
    private String city;

    /**
     * 运营商
     */
    @ApiModelProperty("ip所属运营商")
    private String isp;

    /**
     * 0 代表 IP异常 address解析error， 1正常
     */
    @ApiModelProperty("状态： 0-ip parse error, 1-success")
    private int state = 0;

    private IpAddressRespVo(){}

    /**
     * @param str 国家|区域|省份|城市|ISP 格式的字符串
     * @param ip  ip
     * @return
     */
    public static IpAddressRespVo parseSuccess(String str, String ip) {
        IpAddressRespVo ipAddressRespVo = new IpAddressRespVo();
        ipAddressRespVo.state = 1;
        ipAddressRespVo.ip = ip;
        String[] split = str.split("\\|");
        ipAddressRespVo.country = split[0];
        ipAddressRespVo.region = split[1];
        ipAddressRespVo.province = split[2];
        ipAddressRespVo.city = split[3];
        ipAddressRespVo.isp = split[4];
        return ipAddressRespVo;
    }

    public static IpAddressRespVo parseError(String ip) {
        IpAddressRespVo ipAddressRespVo = new IpAddressRespVo();
        ipAddressRespVo.state = 0;
        ipAddressRespVo.ip = ip;
        return ipAddressRespVo;
    }

}

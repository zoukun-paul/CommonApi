package com.frame.kzou.business.vo.qr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/16
 * Time: 15:37
 * Description: QR 请求数据封装类
 */
@Data
public class QrReqVo {

    @ApiModelProperty("QR颜色-16进制表示")
    private String qrColor;

    @ApiModelProperty("QR背景颜色-16进制表示")
    private String bgColor;

    @ApiModelProperty("logo")
    private String logo;

    @ApiModelProperty(value = "QR编码内容", required = true)
    private String content;

    @ApiModelProperty(value = "QR大小")
    private Integer size;
}

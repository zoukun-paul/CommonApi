package com.frame.kzou.business.vo.ip;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/12
 * Time: 15:35
 * Description: 经纬度坐标对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoAddress {
    @ApiModelProperty("纬度")
    private double lat;

    @ApiModelProperty("经度")
    private double lng;


}

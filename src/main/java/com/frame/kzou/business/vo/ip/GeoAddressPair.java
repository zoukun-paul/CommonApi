package com.frame.kzou.business.vo.ip;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/12
 * Time: 16:23
 * Description:
 */
@Data
public class GeoAddressPair {
    public GeoAddress source;
    public GeoAddress target;
}

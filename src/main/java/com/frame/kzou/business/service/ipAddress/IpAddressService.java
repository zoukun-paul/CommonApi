package com.frame.kzou.business.service.ipAddress;

import com.frame.kzou.business.vo.ip.GeoAddress;
import com.frame.kzou.business.vo.ip.IpAddressRespVo;
import com.frame.kzou.common.CommonResult;
import com.frame.kzou.exception.BusinessException;
import com.frame.kzou.utils.ValidUtil;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/12
 * Time: 10:57
 * Description:
 */
@Service
public class IpAddressService {

    @Autowired
    private Searcher ipSearcher;

    private final static double EarthRadius = 6378.137;


    /**
     * 获取Ip的 address
     * @param ip
     * @return
     */
    public String queryIpAddress(String ip)  {
        try {
            return ipSearcher.search(ip);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonResult.IpQueryError);
        }
    }

    /**
     * 批量获取Ip的 address 信息
     * @param ips   ips
     * @param ignoreError 是否忽略异常Ip
     * @return
     */
    public List<IpAddressRespVo> queryIpAddressBatch(String[] ips, boolean ignoreError) {
        ArrayList<IpAddressRespVo> res = new ArrayList<>();
        for (String ip : ips) {
            boolean validRes = ValidUtil.validIp4(ip);
            if (!ignoreError) {
                // 不忽略异常Ip
                if (!validRes) {
                    throw new BusinessException(CommonResult.ParameterError);
                } else {
                    res.add(IpAddressRespVo.parseSuccess(queryIpAddress(ip), ip));
                }
            } else {
                if (!validRes) {
                    res.add(IpAddressRespVo.parseError(ip));
                } else {
                    res.add(IpAddressRespVo.parseSuccess(queryIpAddress(ip), ip));
                }
            }
        }
        return res;
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }
    /**
     * 计算 两个经纬度 的距离
     * @param geoAddress1  1
     * @param geoAddress2  2
     * @return
     */
    public double getDistance(GeoAddress geoAddress1, GeoAddress geoAddress2) {
        double radLat1 = rad(geoAddress1.getLat());
        double radLat2 = rad(geoAddress2.getLat());
        double a = radLat1 - radLat2;
        double b = rad(geoAddress1.getLng()) - rad(geoAddress2.getLng());
        double tmp = Math.pow(Math.sin(a / 2), 2) + Math.cos(geoAddress1.getLat())
                * Math.cos(geoAddress2.getLat()) * Math.pow(Math.sin(b / 2), 2);
        return  2 * Math.asin(Math.sqrt(tmp)) * EarthRadius * 1_000;
    }
}

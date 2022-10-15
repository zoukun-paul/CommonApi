package com.frame.kzou.business.controller.ip_address;
import com.frame.kzou.business.service.ipAddress.IpAddressService;
import com.frame.kzou.business.vo.ip.GeoAddressPair;
import com.frame.kzou.business.vo.ip.IpAddressRespVo;
import com.frame.kzou.common.ResultResponse;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/12
 * Time: 10:09
 * Description:
 */
@RestController
@RequestMapping("ipad")
public class IpAddressController {

    @Autowired
    private IpAddressService ipAddressService;

    @ApiResponse(response = String.class, code = 200, message = "国家|区域|省份|城市|ISP")
    @GetMapping("{ip}")
    public ResultResponse<String> getAddressByIp(@PathVariable String ip) {
        return ResultResponse.success(ipAddressService.queryIpAddress(ip));
    }

    @PostMapping("ip/batch")
    public ResultResponse<List<IpAddressRespVo>> getAddressBatch(@RequestParam("ips") String[] ips, Boolean ignoreError) {
        List<IpAddressRespVo> ipAddressRespVos = ipAddressService.queryIpAddressBatch(ips,
                ignoreError == null || ignoreError);
        return ResultResponse.success(ipAddressRespVos);
    }

    @PostMapping("/distance")
    @ApiResponse(response = String.class, code = 200, message = "单位：米")
    public ResultResponse<String> getDistance(@RequestBody GeoAddressPair geoAddressPair) {
        double distance = ipAddressService.getDistance(geoAddressPair.getSource(), geoAddressPair.getTarget());
        return ResultResponse.success(String.format("%.1f", distance));
    }
}

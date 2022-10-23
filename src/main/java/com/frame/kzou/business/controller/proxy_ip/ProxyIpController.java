package com.frame.kzou.business.controller.proxy_ip;

import com.frame.kzou.business.pojo.ProxyIp;
import com.frame.kzou.business.service.proxy_pool.ProxyPoolService;
import com.frame.kzou.common.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/20
 * Time: 19:00
 * Description: Ip 代理 对外 API层
 */
@RestController
@RequestMapping("proxy")
public class ProxyIpController {

    @Autowired(required = false)
    private  ProxyPoolService proxyPoolService;


    @GetMapping("{limit}")
    public ResultResponse queryProxyIp(@PathVariable Integer limit) {
        if (proxyPoolService == null) {
            return ResultResponse.success();
        }
        List<ProxyIp> data = proxyPoolService.queryLimit(limit);
        return ResultResponse.success(data);
    }

}


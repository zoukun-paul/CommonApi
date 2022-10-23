package com.frame.kzou.business.service.proxy_pool.check;

import com.frame.kzou.business.pojo.ProxyIp;
import com.frame.kzou.business.service.proxy_pool.store.StoreProxyIp;
import com.frame.kzou.enums.DateFormatEnum;
import com.frame.kzou.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/20
 * Time: 19:03
 * Description: 定时检测
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "server.crawl.enable", havingValue = "true")
public class ScheduledProxyIpCheck {

    private final ProxyIpCheck proxyIpCheck;

    private final StoreProxyIp storeProxyIp;

    public ScheduledProxyIpCheck(StoreProxyIp storeProxyIp, ProxyIpCheck proxyIpCheck) {
        this.storeProxyIp = storeProxyIp;
        this.proxyIpCheck = proxyIpCheck;
    }

    /**
     * 1 h 检测一次
     */
//    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void scheduleValid() {
        log.info("开始全局IP清理");
        long startTime = System.currentTimeMillis();
        storeProxyIp.clear(proxyIp -> {
            proxyIpCheck.validProxyIp(proxyIp);
            return proxyIp.getLife() > 0;
        });
        log.info(
                "代理池缓存全局检测清理耗时：{}，当前时间：{}",
                System.currentTimeMillis() - startTime,
                DateUtil.getNowFormat(DateFormatEnum.STANDARD_IGNORE_S)
        );
    }
    /**
     * 5min 检测一次
     */
//    @Scheduled(fixedRate = 180_000)
    public void scheduleValidAsync() {
        log.info("开始全局[异步]IP清理：{}", DateUtil.getNowFormat(DateFormatEnum.STANDARD_IGNORE_S));
        List<ProxyIp> proxyIps = storeProxyIp.pollAll();
        for (ProxyIp proxyIp : proxyIps) {
            proxyIpCheck.validScoreAsync(proxyIp, ip -> {
                if (ip.getLife() > 0) {
                    storeProxyIp.push(ip);
                }
            });
        }
    }

}

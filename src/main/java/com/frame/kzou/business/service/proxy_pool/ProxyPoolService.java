package com.frame.kzou.business.service.proxy_pool;

import com.frame.kzou.business.pojo.ProxyIp;
import com.frame.kzou.business.service.proxy_pool.check.ProxyIpCheck;
import com.frame.kzou.business.service.proxy_pool.crawl.Crawl;
import com.frame.kzou.business.service.proxy_pool.store.StoreProxyIp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/21
 * Time: 8:45
 * Description: 自管理服务
 */
@Slf4j
@Service
@ConditionalOnProperty(value = "server.crawl.enable", havingValue = "true")
public class ProxyPoolService {

    private final List<Crawl> crawlers;
    private final ProxyIpCheck proxyIpCheck;
    private final StoreProxyIp storeProxyIp;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final CompletionService<List<ProxyIp>> completionService;

    public ProxyPoolService(List<Crawl> crawlers, StoreProxyIp storeProxyIp, ThreadPoolExecutor threadPoolExecutor, ProxyIpCheck proxyIpCheck) {
        this.crawlers = crawlers;
        this.storeProxyIp = storeProxyIp;
        this.threadPoolExecutor = threadPoolExecutor;
        this.proxyIpCheck = proxyIpCheck;
        completionService = new ExecutorCompletionService<>(threadPoolExecutor);
    }

    /**
     * 程序启动时，爬取一次数据
     */
    @PostConstruct
    public void initPoxy() {
        // 初始化服务
        log.info("共检测到【{}】个IP站点", crawlers.size());
        for (Crawl crawler : crawlers) {
            // 爬取一次目标站点， 将结果存储到 completionService 中
            completionService.submit(crawler);
            log.info("成功开启站点：{},等待爬取", crawler.getUrl());
        }
        // 开启检测线程
        dealAsync();
    }

    /**
     * 校验
     */
    public void deal() {
        threadPoolExecutor.execute(() -> {
            while (true) {
                try {
                    // take 会阻塞，找到有一个任务完成
                    Future<List<ProxyIp>> future = completionService.take();
                    List<ProxyIp> proxyIps = future.get();
                    if (proxyIps != null || !proxyIps.isEmpty()) {
                        log.info(Thread.currentThread() + "=> 接受到{}个IP，等待[同步]校验加入缓存", proxyIps.size());
                        // 测试
                        for (ProxyIp proxyIp : proxyIps) {
                            proxyIpCheck.validProxyIp(proxyIp);
                            if (proxyIp.getLife() > 0) {
                                log.info(
                                        "[同步]校验完成，ip:{}, port:{} 得分：{}",
                                        proxyIp.getIp(),
                                        proxyIp.getPort(),
                                        proxyIp.getLife()
                                );
                                storeProxyIp.push(proxyIp);
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.warn(e.getMessage());
                }
            }
        });
    }

    public void dealAsync() {
        threadPoolExecutor.execute(() -> {
            while (true) {
                try {
                    // take 会阻塞，找到有一个任务完成
                    Future<List<ProxyIp>> future = completionService.take();
                    List<ProxyIp> proxyIps = future.get();
                    if (proxyIps != null || !proxyIps.isEmpty()) {
                        log.info(Thread.currentThread() + "=> 接受到{}个IP，等待[异步]校验加入缓存", proxyIps.size());
                        // 测试
                        for (ProxyIp proxyIp : proxyIps) {
                            proxyIpCheck.validScoreAsync(proxyIp, ip -> {
                                if (ip.getLife() > 0) {
                                    storeProxyIp.push(proxyIp);
                                }
                            });
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.warn(e.getMessage());
                }
            }
        });
    }

    public List<ProxyIp> queryLimit(Integer limit) {
        return storeProxyIp.queryProxyIp(limit);
    }
}

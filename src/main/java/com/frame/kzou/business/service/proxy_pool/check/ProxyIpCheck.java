package com.frame.kzou.business.service.proxy_pool.check;

import com.frame.kzou.business.pojo.ProxyIp;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/19
 * Time: 20:14
 * Description: 校验 proxyIP
 */
@Slf4j
@Component
public class ProxyIpCheck {
    /**
     * 测试站点
     */
    private final List<HttpGet> targetStandardWeb = new ArrayList<>(8);


    private CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();

    @PostConstruct
    public void initTargetStandardWeb() {
        targetStandardWeb.add(new HttpGet("https://www.jd.com/"));
        targetStandardWeb.add(new HttpGet("https://www.taobao.com"));
        targetStandardWeb.add(new HttpGet("https://www.sina.com.cn"));
        httpAsyncClient.start();
    }
    /**
     * 测试指定的 ProxyIp，活跃的代理的Lift值>=90
     * @param proxyIp    ProxyIp
     */
    public void validProxyIp(ProxyIp proxyIp) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort()));
        int score = validScore(proxy);
        proxyIp.setCheckTime(new Date());
        proxyIp.setCheckTimes(proxyIp.getCheckTimes() + 1);
        if (score > 0) {
            proxyIp.setLife(90 + score);
        }else {
            proxyIp.setLife(proxyIp.getLife() + score);
        }
        log.info(
                Thread.currentThread().getName() + "=>[{}]测试IP:{}, port:{},测试结果life：{}",
                score > 0 ? ("+" + score) : score,
                proxyIp.getIp(),
                proxyIp.getPort(),
                proxyIp.getLife()
        );
    }

    /**
     * 同步校验
     * @param proxy 待校验的代理
     * @return  得分
     */
    private int validScore(Proxy proxy) {
        log.info("开始[同步]校验 {}", proxy);
        int res = 0;
        HttpURLConnection conn = null;
        for (HttpGet httpGet : targetStandardWeb) {
            URI uri = httpGet.getURI();
            try {
                conn = (HttpURLConnection) uri.toURL().openConnection(proxy);
                // 不使用缓存
                conn.setUseCaches(false);
                //设置超时时间 3s
                conn.setConnectTimeout(3_000);
                // 请求方式
                conn.setRequestMethod("GET");
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    res++;
                } else {
                    res--;
                }
            } catch (Exception e) {
                res--;
            }finally {
                if (conn != null) {
                    // 断开连接，释放资源
                    conn.disconnect();
                }
            }
        }
        return res;
    }

    private int validScore(ProxyIp proxyIp, URI uri) {
        int res = 0;
        Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort()));
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) uri.toURL().openConnection(proxy1);
            // 不使用缓存
            conn.setUseCaches(false);
            //设置超时时间 3s
            conn.setConnectTimeout(3_000);
            // 请求方式
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                res++;
            } else {
                res--;
            }
        } catch (Exception e) {
            res--;
        }finally {
            if (conn != null) {
                // 断开连接，释放资源
                conn.disconnect();
            }
        }
        return res;
    }

    /**
     * 异步校验
     * @param proxyIp 待校验代理
     */
    public void validScoreAsync(ProxyIp proxyIp, Consumer<ProxyIp> finish) {
        CompletableFuture<Integer>[] futures = new CompletableFuture[targetStandardWeb.size()];
        for (int i = 0; i < targetStandardWeb.size(); i++) {
            HttpGet request = targetStandardWeb.get(i);
            // 设置代理
            HttpHost proxy = new HttpHost(proxyIp.getIp(), proxyIp.getPort());
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            request.setConfig(config);
            // 异步校验
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                // 校验
                return validScore(proxyIp, request.getURI());
            });
            // 添加结果
            futures[i] = completableFuture;
        }
        // 统一处理，结果
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futures);
        allOfFuture.whenComplete((unused, throwable) -> {
            // 统计得分
            Integer sumScore = Arrays.stream(futures).map(completableFuture -> {
                try {
                    return completableFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return -10;
                }
            }).reduce(0, Integer::sum);
            proxyIp.setLife(proxyIp.getLife() + sumScore);
            // 执行回调
            finish.accept(proxyIp);
            log.info(
                    "异步校验完成，校验参考站点数{}，校验IP：{}, port: {}, life:{}",
                    targetStandardWeb.size(),
                    proxyIp.getIp(),
                    proxyIp.getPort(),
                    proxyIp.getLife()
            );
        });
    }

}

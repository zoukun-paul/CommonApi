package com.frame.kzou.business.service.proxy_pool;

import com.frame.kzou.business.pojo.ProxyIp;
import com.frame.kzou.business.service.proxy_pool.crawl.Crawl;
import com.frame.kzou.business.service.proxy_pool.crawl.CrawlHelper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/23
 * Time: 17:09
 * Description:
 */
@Component
public class Seofangfa implements Crawl, CrawlHelper {

    final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    public String getUrl() {
        return "https://proxy.seofangfa.com/";
    }

    @Override
    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public List<ProxyIp> parse(String content) {
        Element table = Jsoup
                .parse(content)
                .selectXpath("//div[@class='container theme-showcase']//table")
                .first();
        return parseFromTableElement(table, tds -> {
            ProxyIp proxyIp = new ProxyIp(tds.get(0).text(), Integer.parseInt(tds.get(1).text()), getUrl());
            proxyIp.setProtocol("http");
            proxyIp.setPosition(tds.get(3).text());
            proxyIp.setType("高匿");
            return proxyIp;
        });
    }
}

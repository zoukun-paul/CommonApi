package com.frame.kzou.business.service.proxy_pool.crawl;

import com.frame.kzou.business.pojo.ProxyIp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/21
 * Time: 13:08
 * Description: https://www.zdaye.com/dayProxy/1.html
 */
@Component
public class ZdayeCrawl extends PageBathCrawl{

    /**
     * 解析详情页 获取具体的IP
     * @param content html string
     * @return IP list
     */
    @Override
    public List<ProxyIp> parse(String content) {
        List<ProxyIp> res = new ArrayList<>();
        Elements trs = Jsoup.parse(content)
                .select("#ipc")
                .first()
                .select("tr");
        for (int i = 1; i < trs.size(); i++) {
            Element tr = trs.get(i);
            Elements tds = tr.select("td");
            ProxyIp proxyIp = new ProxyIp(
                    tds.get(0).text(),
                    Integer.parseInt(tds.get(1).text()),
                    getCurrPageUrl()
            );
            proxyIp.setProtocol(tds.get(2).text());
            proxyIp.setType(tds.get(3).text());
            proxyIp.setPosition(tds.get(4).text());
            res.add(proxyIp);
        }
        return res;
    }

    /**
     * 获取具体IP详情页uri
     * @param content   string html content
     * @return  uri list
     */
    private List<String> parseLink(String content) {
        Document doc = Jsoup.parse(content);
        return doc.select(".thread_item")
                .select("a")
                .eachAttr("href");
    }

    @Override
    Integer getMaxPage() {
        return 5;
    }

    @Override
    String getBaseUrl() {
        return "https://www.zdaye.com/dayProxy";
    }

    @Override
    String getCurrPageUrl() {
        return getBaseUrl() + "/" + currPage + ".html";
    }

    @Override
    public List<ProxyIp> call() throws Exception {
        List<ProxyIp> res = new ArrayList<>();
        for (currPage = getStatPage(); currPage < getMaxPage(); currPage++) {
            // 获取IP详情页的站点链接
            String url = getCurrPageUrl();
            List<String> links = parseLink(request(url));
            for (String link : links) {
                // 解析详情页面获取IP数据对象
                List<ProxyIp> proxyIps = parse(request(link));
                res.addAll(proxyIps);
            }
        }
        return res;
    }
}

package com.frame.kzou.config;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/9
 * Time: 13:56
 * Description:
 */
@Slf4j
@Configuration
public class ServiceConfig {

    @Autowired
    private ServletWebServerApplicationContext webServerAppContext;

    @Value("${ip.region-db-path:ip2region.xdb}")
    private String ipRegionDbPath;

    @Bean
    public byte[] getIpRegionVIndex() throws IOException {
        byte[] res = null;
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(ipRegionDbPath)) {
            // 本地存储
            res = new byte[inputStream.available()];
            inputStream.read(res);
        }
        return res;
    }

    @Bean
    public Searcher ipSearcher(byte[] regionVIndex) {
        try {
            return Searcher.newWithBuffer(regionVIndex);
        } catch (IOException e) {
            log.error("create Searcher bean error: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  基本访问入口
     * @return
     */
//    @Bean(name = "baseUrl")
    public String baseUrl() {
        String host = "";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int port = webServerAppContext.getWebServer().getPort();
        ServletContext servletContext = webServerAppContext.getServletContext();
        String contextPath = servletContext == null ? "" : servletContext.getContextPath();
        return "http://" + host + ":" + port + contextPath;
    }
}

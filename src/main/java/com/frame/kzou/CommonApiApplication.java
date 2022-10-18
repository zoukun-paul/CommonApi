package com.frame.kzou;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author HP
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
@MapperScan("com.frame.kzou.busine" +
        "ss.mapper")
public class CommonApiApplication {

    public static void main(String[] args) {
        log.info("...启动服务...");
        SpringApplication.run(CommonApiApplication.class, args);
    }
}

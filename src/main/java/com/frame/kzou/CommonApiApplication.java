package com.frame.kzou;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HP
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.frame.kzou.busine" +
        "ss.mapper")
public class CommonApiApplication {

    public static void main(String[] args) {
        log.info("...启动服务...");
        SpringApplication.run(CommonApiApplication.class, args);
    }
}

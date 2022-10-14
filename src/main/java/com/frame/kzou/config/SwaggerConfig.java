package com.frame.kzou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/9/16
 * Time: 14:50
 * Description: swagger 配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable:true}")
    private boolean enable;

    @Bean
    public Docket createDocket(){
        /*配置信息*/
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.frame.kzou.business.controller"))
                .paths(PathSelectors.any())
                .build()
                .enable(enable);
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("")
                .description("CommonAPi 接口文档")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}

package com.heyu.travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description 声明spring-mvc的配置类
 * EnableWebMvc:开启mvc的高级注解配置
 */
@Configuration
@EnableWebMvc
public class SpringMvcConfig implements WebMvcConfigurer {
    /**
     * @Description 文件上传
     */
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(104857600);
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxInMemorySize(4096);
        return multipartResolver;
    }
}

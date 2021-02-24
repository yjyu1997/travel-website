package com.heyu.travel.config;

import com.heyu.travel.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @author heyu
 * @Description 前台页面spring-mvc配置类
 */

@ComponentScan("com.heyu.travel.web")
@Configuration
public class PlatformSpringMvcConfig extends SpringMvcConfig {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/**/userService/*", "/**/sellerService/*", "/**/categoryService/findAllCategory");

    }
}

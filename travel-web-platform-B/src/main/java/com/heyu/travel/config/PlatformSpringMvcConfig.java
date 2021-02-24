package com.heyu.travel.config;

import com.heyu.travel.interceptors.CrossInterceptor;
import com.heyu.travel.interceptors.LoginInterceptor;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
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
@EnableRedissonHttpSession
public class PlatformSpringMvcConfig extends SpringMvcConfig {

    @Bean("loginInterceptor")
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean("crossInterceptor")
    public CrossInterceptor crossInterceptor() {return new CrossInterceptor();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crossInterceptor()).addPathPatterns("/**");
        // 登录拦截
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/**/userService/*",
                "/**/sellerService/*",
                "/**/categoryService/findAllCategory",
                "/**/checkCaptchaCode.do");

    }
}

package com.heyu.travel.config;

import com.heyu.travel.interceptors.CheckCodeInterceptor;
import com.heyu.travel.interceptors.CrossInterceptor;
import com.heyu.travel.interceptors.LoginInterceptor;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Bean("checkCodeInterceptor")
    public CheckCodeInterceptor checkCodeInterceptor() {return new CheckCodeInterceptor();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crossInterceptor()).addPathPatterns("/**");
        // 登录拦截
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/**/userService/*",
                "/**/sellerService/*",
                "/**/categoryService/findAllCategory",
                "/**/checkCaptchaCode.do");

        /**
         * @Description 可能因为crossInteceptor未生效（顺序问题）
         */
        //registry.addInterceptor(checkCodeInterceptor()).addPathPatterns("/**/userService/loginUser","/**/userService/registerUser");
    }


}

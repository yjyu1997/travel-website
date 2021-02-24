package com.heyu.travel.interceptors;

import com.heyu.travel.constant.Constant;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author heyu
 * @Description 跨域拦截器
 */
public class CrossInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 告知浏览器本服务器返回的资源支持跨域访问
        //解决跨域问题
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        // 设置跨域请求时,支持的请求方式
        response.setHeader("access-control-allow-methods", "*");
        // 是否支持cookie跨域
        response.setHeader("access-control-allow-credentials", "true");
        // 指定头部跨域传递accessToken
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,"+ Constant.ACCESS_TOKEN + ",checkCode");


        return true;
    }
}

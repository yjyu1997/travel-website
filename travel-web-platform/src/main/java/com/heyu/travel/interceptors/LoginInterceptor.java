package com.heyu.travel.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.heyu.travel.enums.StatusEnum;
import com.heyu.travel.res.GatewayResp;
import com.heyu.travel.service.UserService;
import com.heyu.travel.session.SubjectUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author heyu
 * @Description 登陆拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * @Description spring注入前台对象上下文
     * 拦截器加载的时间点在springContext之前，
     * 如果不做额外操作，在拦截器中注入为null
     */
    @Autowired
    SubjectUserContext subjectUserContext;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("______________________________________________________");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isLogin = subjectUserContext.existSubject();
        if(!isLogin){
            GatewayResp resp = GatewayResp.builder()
                    .code(StatusEnum.NO_LOGIN.getCode())
                    .msg(StatusEnum.NO_LOGIN.getMsg())
                    .data("")
                    .operationTime(new Date())
                    .build();
            response.setContentType("application/json; charset=utf-8");
            //转换json，并且处理时间格式
            response.getWriter().write(JSONObject.toJSONString(resp,
                    SerializerFeature.WriteDateUseDateFormat));
            //不放行
            return false;
        }
        return true;
    }
}

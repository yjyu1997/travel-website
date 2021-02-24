package com.heyu.travel.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.heyu.travel.res.GatewayResp;
import com.heyu.travel.session.SubjectUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;


public class CheckCodeInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String checkCode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        System.out.println("cookies:    " + request.getCookies());
        String id = session.getId();
        System.out.println(id);
        String checkCode = request.getHeader("checkCode");
        if(!checkCode_server.equals(checkCode)){
            GatewayResp resp = GatewayResp.builder()
                    .code("1009")
                    .msg("验证码错误")
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

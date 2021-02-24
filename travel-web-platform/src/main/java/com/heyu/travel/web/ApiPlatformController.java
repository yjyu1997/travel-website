package com.heyu.travel.web;

import com.heyu.travel.req.BaseRequest;
import com.heyu.travel.res.GatewayResp;
import com.heyu.travel.service.GatewayService;
import com.heyu.travel.utils.EmptyUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 后台网关
 */
@Controller
//窄化路径
@RequestMapping("/api-platform")
@Log4j2
public class ApiPlatformController {

    @Autowired
    GatewayService gatewayService;

    @RequestMapping("/{service}/{method}")
    @ResponseBody
    //解决跨域问题
    @CrossOrigin
    public GatewayResp apiLoan(@PathVariable(required = false) String service,
                               @PathVariable(required = false) String method,
                               MultipartFile file,
                               @RequestBody BaseRequest<?> baseRequest){
        GatewayResp resp = null;
        if(EmptyUtil.isNullOrEmpty(service) || EmptyUtil.isNullOrEmpty(method)) {
            log.warn("api-loan请求不合法！---> 调用者[{}]--->调用方法[{}]",service,method);
            return GatewayResp.builder().data(null).code("1008").msg("请求参数不合法").build();
        }else {
            log.info("api-loan请求：调用类[{}]--->方法[{}]--->请求参数[{}]",
                    service, method, baseRequest.toString());
            resp = gatewayService.method(method,baseRequest,file);
        }
        return resp;
    }
}

package com.heyu.travel.service;

import com.heyu.travel.req.BaseRequest;
import com.heyu.travel.res.GatewayResp;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author heyu
 * @Description 网关服务
 */
public interface GatewayService {

    /**
     * @Description 统一方法处理
     * @param method 请求方法
     * @param baseRequest 请求数据
     * @param file 文件上传对象
     * @return 网关响应对象
     */
     GatewayResp method( String method, BaseRequest<?> baseRequest, MultipartFile file);

    /**
     * @Description 统一异常处理
     * @param e 异常信息
     * @return 网关响应对象
     */
    GatewayResp error(GatewayResp result,Exception e);

}


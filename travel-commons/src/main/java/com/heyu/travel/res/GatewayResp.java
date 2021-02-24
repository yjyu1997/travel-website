package com.heyu.travel.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heyu.travel.utils.ToString;
import lombok.*;

import java.util.Date;

/**
 * @author heyu
 * @Description 返回结果
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatewayResp extends ToString {

    /**
     * @Description 返回编码
     */
    private String code;

    /**
     * @Description 返回信息
     */
    private String msg;

    /**
     * @Description 操作者
     */
    private Long userId;

    /**
     * @Description 操作者名称
     */
    private String userName;

    /**
     * @Description 创建时间,处理json的时间参数解析
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date operationTime;

    /**
     * @Description 返回结果
     */
    private Object data;

    /**
     * @Description 图片站点
     */
    private String webSite;

}

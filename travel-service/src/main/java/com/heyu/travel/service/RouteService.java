package com.heyu.travel.service;

import com.github.pagehelper.PageInfo;
import com.heyu.travel.req.RouteVo;

public interface RouteService {

    /**
     * @Description 添加路线
     * @param routeVo 路径请求参数
     * @return 影响行数
     */
    Integer addRoute(RouteVo routeVo);

    /**
     * @Description 编辑线路
     * @param routeVo 线路请求参数
     * @return 影响行数
     */
    Integer updateRoute(RouteVo routeVo);

    /**
     * @Description 查询线路
     * @param routeVo 路径请求参数
     * @return 线路详情
     */
    RouteVo findRouteById(RouteVo routeVo);

    /**
     * @Description 分页查询
     * @param routeVo 路径请求参数
     * @return 线路分页
     */
    PageInfo<RouteVo> findRouteByPage(RouteVo routeVo, int pageNum, int pageSize);

    /**
     * @Description 查询路线对应图片，并绑定到routeVo对象
     * @param routeVo routeVo对象
     */
    public void bindAffixesToRoute(RouteVo routeVo);
}

package com.heyu.travel.service;

import com.github.pagehelper.PageInfo;
import com.heyu.travel.req.FavoriteVo;
import com.heyu.travel.req.RouteVo;

public interface FavoriteService {
    /**
     * @Description 查询当前用户 收藏夹
     * @param favoriteVo 关注请求参数
     * @param pageNum 分页显示 页数
     * @param pageSize 分页显示 每页条数
     * @return RouteVo 路线信息
     */
    PageInfo<RouteVo> findMyFavorite(FavoriteVo favoriteVo, int pageNum, int pageSize);

    /**
     * @Description 是否收藏
     * @param favoriteVo 关注请求参数
     * @return 是否收藏
     */
    Boolean isFavorite(FavoriteVo favoriteVo);

    /**
     * @Description 添加收藏
     * @return 当前路线新收藏个数
     */
    Integer addFavorite(FavoriteVo favoriteVo);
}

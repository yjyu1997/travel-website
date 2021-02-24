package com.heyu.travel.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heyu.travel.constant.RedisConstant;
import com.heyu.travel.mapper.FavoriteMapper;
import com.heyu.travel.mapper.RouteMapper;
import com.heyu.travel.pojo.Affix;
import com.heyu.travel.pojo.Route;
import com.heyu.travel.pojo.RouteExample;
import com.heyu.travel.req.AffixVo;
import com.heyu.travel.req.RouteVo;
import com.heyu.travel.service.AffixService;
import com.heyu.travel.service.RedisCacheService;
import com.heyu.travel.service.RouteService;
import com.heyu.travel.session.SubjectSellerContext;
import com.heyu.travel.utils.BeanConv;
import com.heyu.travel.utils.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description 线路实现类
 */
@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    SubjectSellerContext subjectSellerContext;

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    AffixService affixService;


    /**
     * @param routeVo 路径请求参数
     * @return 影响行数
     * @Description 添加路线
     */
    @Override
    public Integer addRoute(RouteVo routeVo) {
        return routeMapper.insert(BeanConv.toBean(routeVo, Route.class));
    }

    /**
     * @param routeVo 线路请求参数
     * @return 影响行数
     * @Description 编辑线路
     */
    @Override
    public Integer updateRoute(RouteVo routeVo) {
        if(EmptyUtil.isNullOrEmpty(routeVo.getId())){
            return 0;
        }
        String routeKey = RedisConstant.ROUTESERVICE_FINDROUTEBYID;
        String affixKey = RedisConstant.AFFIXSERVICE_FINDAFFIXBYBUSINESSID;
        //进行延时双删
        redisCacheService.deleSingleCache(routeKey + routeVo.getId());
        redisCacheService.deleListCache(affixKey + routeVo.getId());
        int flag = routeMapper.updateByPrimaryKey(BeanConv.toBean(routeVo,Route.class));
        if(flag == 1){
            try {
                Thread.sleep(500);
                redisCacheService.deleSingleCache(routeKey + routeVo.getId());
                redisCacheService.deleListCache(affixKey + routeVo.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * @param routeVo 路径请求参数
     * @return 线路详情
     * @Description 查询线路
     */
    @Override
    public RouteVo findRouteById(RouteVo routeVo) {
        String key = RedisConstant.ROUTESERVICE_FINDROUTEBYID;
        return redisCacheService.singleCache(()->{
                    RouteVo routeVoResult = BeanConv.toBean(routeMapper.selectByPrimaryKey(routeVo.getId()), RouteVo.class);
                    //走缓存拿图片
                    //绑定路线Id到affixVo对象方便查询图片信息
                    bindAffixesToRoute(Objects.requireNonNull(routeVoResult));
                    return routeVoResult;
                }
                ,key + routeVo.getId());
    }

    /**
     * @param routeVo  路径请求参数
     * @param pageNum 当前页数
     * @param pageSize 每页条数
     * @return 线路分页
     * @Description 分页查询
     */
    @Override
    public PageInfo<RouteVo> findRouteByPage(RouteVo routeVo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        RouteExample example = new RouteExample();
        RouteExample.Criteria criteria = example.createCriteria();
        //动态sql拼接
        if(!EmptyUtil.isNullOrEmpty(routeVo.getCategoryId())){
            criteria.andCategoryIdEqualTo(routeVo.getCategoryId());
        }
        if(!EmptyUtil.isNullOrEmpty(routeVo.getRouteName())){
            criteria.andRouteNameLike("%"+routeVo.getRouteName()+"%");
        }
        if(!EmptyUtil.isNullOrEmpty(routeVo.getMinPrice())){
            criteria.andPriceGreaterThan(routeVo.getMinPrice());
        }
        if(!EmptyUtil.isNullOrEmpty(routeVo.getMaxPrice())){
            criteria.andPriceLessThan(routeVo.getMaxPrice());
        }
        //注意这里的字段为数据库字段：类似example.setOrderByClause("index ASC,created_time ASC");
        if (!EmptyUtil.isNullOrEmpty(routeVo.getOrderBy())){
            example.setOrderByClause(routeVo.getOrderBy());
        }

        List<Route> routes = routeMapper.selectByExample(example);
        PageInfo<RouteVo> voPageInfo = BeanConv.toPageInfo(routes, RouteVo.class);
        //添加图片信息
        List<RouteVo> routeVoList = voPageInfo.getList();
        for (RouteVo routeVoHandler : routeVoList) {
            bindAffixesToRoute(routeVoHandler);
        }
        voPageInfo.setList(routeVoList);
        return voPageInfo;
    }

    /**
     * @Description 查询路线对应图片，并绑定到routeVo对象
     * @param routeVo routeVo对象
     */
    @Override
    public void bindAffixesToRoute(RouteVo routeVo){
        AffixVo affixVo = AffixVo.builder().businessId(routeVo.getId()).build();
        routeVo.setAffixVoList(affixService.findAffixByBusinessId(affixVo));
    }
}

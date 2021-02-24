package com.heyu.travel.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heyu.travel.mapper.FavoriteMapper;
import com.heyu.travel.mapper.RouteMapper;
import com.heyu.travel.mapperExt.FavoriteMapperExt;
import com.heyu.travel.mapperExt.RouteMapperExt;
import com.heyu.travel.pojo.Favorite;
import com.heyu.travel.pojo.FavoriteExample;
import com.heyu.travel.pojo.Route;
import com.heyu.travel.req.FavoriteVo;
import com.heyu.travel.req.RouteVo;
import com.heyu.travel.service.FavoriteService;
import com.heyu.travel.service.RouteService;
import com.heyu.travel.session.SubjectUser;
import com.heyu.travel.session.SubjectUserContext;
import com.heyu.travel.utils.BeanConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.Subject;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    //收藏表单表交互
    @Autowired
    FavoriteMapper favoriteMapper;

    //收藏表多表交互
    @Autowired
    FavoriteMapperExt favoriteMapperExt;

    //路线表单表交互
    @Autowired
    RouteMapper routeMapper;

    //路线表多表交互
    @Autowired
    RouteMapperExt routeMapperExt;

    @Autowired
    SubjectUserContext subjectUserContext;

    @Autowired
    RouteService routeService;


    /**
     * @param favoriteVo 关注请求参数
     * @param pageNum    分页显示 页数
     * @param pageSize   分页显示 每页条数
     * @return RouteVo 路线信息
     * @Description 查询当前用户 收藏夹
     */
    @Override
    public PageInfo<RouteVo> findMyFavorite(FavoriteVo favoriteVo, int pageNum, int pageSize) {
        //使用分页
        PageHelper.startPage(pageNum,pageSize);
        //获取当前登陆用户
        SubjectUser subjectUser = subjectUserContext.getSubject();
        //需要注意的是不要直接使用BeanConv对list进行copy,这样会导致统计页面出错
        List<Route> list = favoriteMapperExt.findFavoriteByUserId(subjectUser.getId());
        PageInfo<RouteVo> voPageInfo = BeanConv.toPageInfo(list, RouteVo.class);
        List<RouteVo> routeVoList = voPageInfo.getList();
        for (RouteVo routeVoHandler : routeVoList) {
            routeService.bindAffixesToRoute(routeVoHandler);
        }
        voPageInfo.setList(routeVoList);
        return voPageInfo;
    }

    /**
     * @param favoriteVo 关注请求参数
     * @return 是否收藏
     * @Description 收藏夹数量是否大于0
     */
    @Override
    public Boolean isFavorite(FavoriteVo favoriteVo) {
        //1.获得当前用户
        SubjectUser subjectUser = subjectUserContext.getSubject();
        FavoriteExample favoriteExample = new FavoriteExample();
        favoriteExample.createCriteria().andRouteIdEqualTo(favoriteVo.getRouteId()).
                andUserIdEqualTo(subjectUser.getId());
        List<Favorite> favoriteList = favoriteMapper.selectByExample(favoriteExample);
        return favoriteList.size()>0;
    }

    /**
     * @param favoriteVo 关注请求参数
     * @return 当前路线新收藏个数
     * @Description 添加收藏
     */
    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class,
            isolation = Isolation.REPEATABLE_READ,
            transactionManager = "transactionManager")
    @Override
    public Integer addFavorite(FavoriteVo favoriteVo) {
        //1, 获得当前用户
        SubjectUser subjectUser = subjectUserContext.getSubject();
        favoriteVo.setUserId(subjectUser.getId());
        //2.向tab_favorite表插入一条数据
        favoriteMapper.insert(BeanConv.toBean(favoriteVo,Favorite.class));
        //3.更新tab_route表的count字段+1
        Long flag = routeMapperExt.updateRouteCountById(favoriteVo.getRouteId());
        if (flag==0){
            throw new RuntimeException("修改统计表出错");
        }
        //4.重新查询tab_route表的count字段
        Route route = routeMapper.selectByPrimaryKey(favoriteVo.getRouteId());
        return route.getAttentionCount();
    }
}

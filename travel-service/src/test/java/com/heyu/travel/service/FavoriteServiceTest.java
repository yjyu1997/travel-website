package com.heyu.travel.service;

import com.github.pagehelper.PageInfo;
import com.heyu.travel.basic.TestConfig;
import com.heyu.travel.exception.ProjectException;
import com.heyu.travel.req.FavoriteVo;
import com.heyu.travel.req.RouteVo;
import com.heyu.travel.req.UserVo;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;


/**
 * @Description FavoriteService测试类
 */
@Log4j2
public class FavoriteServiceTest extends TestConfig {

    /**
     * @Description 模拟用户登录
     */
    @Before
    public void before() throws ProjectException {
        UserVo userVo = UserVo.builder()
                .username("admin")
                .password("pass")
                .build();
        UserVo userVoResult = userService.loginUser(userVo);
    }

    @Test
    public void testAddFavorite(){
        log.info("testAddFavorite----开始");
        FavoriteVo favoriteVo = FavoriteVo.builder()
                .routeId(1L)
                .build();
        int flag = favoriteService.addFavorite(favoriteVo);
        log.info("testAddFavorite----结束:{}",flag);
    }

    @Test
    public void testFindMyFavorite(){
        log.info("testFindMyFavorite----开始");
        FavoriteVo favoriteVo = FavoriteVo.builder()
                .build();
        PageInfo<RouteVo> page = favoriteService.findMyFavorite(favoriteVo, 1, 2);
        log.info("testFindMyFavorite----结束：{}",page.toString());
    }

    @Test
    public void isFavorited(){
        log.info("isFavorited----开始");
        FavoriteVo favoriteVo = FavoriteVo.builder()
                .routeId(1L)
                .build();
        boolean flag = favoriteService.isFavorite(favoriteVo);
        log.info("isFavorited----结束:{}",flag);
    }

}
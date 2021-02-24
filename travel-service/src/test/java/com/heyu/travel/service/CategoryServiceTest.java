package com.heyu.travel.service;

import com.heyu.travel.basic.TestConfig;
import com.heyu.travel.exception.ProjectException;
import com.heyu.travel.req.CategoryVo;
import com.heyu.travel.req.UserVo;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Description 分类测试类
 */
@Log4j2
public class CategoryServiceTest extends TestConfig {

    @Before
    public void before() throws ProjectException {
        UserVo userVo = UserVo.builder()
                .username("admin")
                .password("pass")
                .build();
        UserVo userVoResult = userService.loginUser(userVo);
    }

    @Test
    public void testFindAllCategory(){
        log.info("testFindAllCategory----开始");
        List<CategoryVo> allCategory = categoryService.findAllCategory();
        log.info("testFindAllCategory----结束:{}",allCategory);
    }
}

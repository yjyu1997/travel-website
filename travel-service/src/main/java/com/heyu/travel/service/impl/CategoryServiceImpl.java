package com.heyu.travel.service.impl;

import com.heyu.travel.constant.RedisConstant;
import com.heyu.travel.mapper.CategoryMapper;
import com.heyu.travel.pojo.Category;
import com.heyu.travel.pojo.CategoryExample;
import com.heyu.travel.req.CategoryVo;
import com.heyu.travel.service.CategoryService;
import com.heyu.travel.service.RedisCacheService;
import com.heyu.travel.utils.BeanConv;
import com.heyu.travel.utils.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @Description 分类服务实现
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    RedisCacheService redisCacheService;

    /**
     * 分类表单表交互
     */
    @Autowired
    CategoryMapper categoryMapper;


    /**
     * @Description 查询所有分类
     */
    @Override
    public List<CategoryVo> findAllCategory() {
        String key = RedisConstant.CATEGORYSERVICE_FINDALLCATEGORY;
        return redisCacheService.listCache(()->{
            CategoryExample categoryExample = new CategoryExample();
            List<Category> categories= categoryMapper.selectByExample(categoryExample);
            if(!EmptyUtil.isNullOrEmpty(categories)){
                return BeanConv.toBeanList(categories,CategoryVo.class);
            }
            return null;
        },key);
    }
}

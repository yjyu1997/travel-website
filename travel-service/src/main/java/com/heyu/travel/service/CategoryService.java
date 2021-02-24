package com.heyu.travel.service;

import com.heyu.travel.req.CategoryVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 分类服务接口
 */
public interface CategoryService {

    /**
     * @Description 查询所分类
     */
    List<CategoryVo> findAllCategory();
}

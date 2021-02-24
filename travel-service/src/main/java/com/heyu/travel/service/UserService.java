package com.heyu.travel.service;

import com.heyu.travel.exception.ProjectException;
import com.heyu.travel.req.UserVo;

/**
 * @Description 用户服务
 */
public interface UserService {
    /**
     * @Description 用户注册
     * @param userVo
     * @return 注册是否成功
     */
    Boolean registerUser(UserVo userVo);

    /**
     * @Description 用户登陆
     * @param userVo
     * @return 登陆成功后返回
     */
    UserVo loginUser(UserVo userVo) throws ProjectException;

    /**
     * @Description 用户退出
     *
     */
    void logoutUser();

    /**
     * @Description 用户是否登陆
     * @return
     */
    Boolean isLogin();
}

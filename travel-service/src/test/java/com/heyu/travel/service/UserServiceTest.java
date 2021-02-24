package com.heyu.travel.service;

import com.heyu.travel.basic.TestConfig;
import com.heyu.travel.exception.ProjectException;
import com.heyu.travel.req.UserVo;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Date;

/**
 * @Description UserService测试类
 */
@Log4j2
public class UserServiceTest extends TestConfig {

    @Test
    public void testRegisterUser(){
        log.info("testRegisterUser----开始");
        UserVo userVo = UserVo.builder()
                .username("shuwenqi")
                .password("pass")
                .realName("束XX")
                .birthday(new Date())
                .sex("1")
                .telephone("15156408888")
                .email("15156408888@qq.com").build();
        boolean flag = userService.registerUser(userVo);
        log.info("testRegisterUser----通过--->{}",flag);
    }

    @Test
    public void testLoginUser() throws ProjectException {
        log.info("testLoginUser----开始");
        UserVo userVo = UserVo.builder()
                .username("admin")
                .password("pass")
                .build();
        UserVo userVoResult = userService.loginUser(userVo);
        log.info("testLoginUser----通过--->{}",userVoResult);
    }


    @Test
    public void testLoginOutUser() throws ProjectException {
        log.info("testLoginUser----开始");
        UserVo userVo = UserVo.builder()
                .username("admin")
                .password("pass")
                .build();
        UserVo userVoResult = userService.loginUser(userVo);
        log.info("testLoginUser----通过--->{}",userVoResult.toString());

        log.info("testLoginOutUser----开始");
        userService.logoutUser();
        log.info("testLogOutUser----通过--->{}");
    }

    @Test
    public void testIsLogin() throws ProjectException {
        log.info("testLoginUser----开始");
        UserVo userVo = UserVo.builder()
                .username("admin")
                .password("pass")
                .build();
        UserVo userVoResult = userService.loginUser(userVo);
        log.info("testLoginUser----通过--->{}",userVoResult.toString());

        log.info("testIsLogin----开始");
        Boolean flag = userService.isLogin();
        log.info("testIsLogin----通过--->{}",flag);
    }

}

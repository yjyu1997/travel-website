package com.heyu.travel.service.impl;

import com.heyu.travel.exception.ProjectException;
import com.heyu.travel.mapper.UserMapper;
import com.heyu.travel.pojo.User;
import com.heyu.travel.pojo.UserExample;
import com.heyu.travel.req.UserVo;
import com.heyu.travel.service.UserService;
import com.heyu.travel.session.SubjectUser;
import com.heyu.travel.session.SubjectUserContext;
import com.heyu.travel.utils.BeanConv;
import com.heyu.travel.utils.MD5Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    //user表单表操作
    @Autowired
    private UserMapper userMapper;

    //当前前台session user对象处理
    @Autowired
    SubjectUserContext subjectUserContext;
    @Autowired
    HttpSession session;

    @Autowired
    HttpServletRequest request;

    /**
     * @Description 用户注册服务
     * @param userVo view层vo对象
     * @return flag>0 是否注册成功
     */
    @Override
    public Boolean registerUser(UserVo userVo) {
        //把uservo对象转换成普通user pojo对象
        User user = BeanConv.toBean(userVo, User.class);
        //对user对象密码进行md5加密
        Objects.requireNonNull(user).setPassword(MD5Coder.md5Encode(user.getPassword()));
        //把
        int flag = userMapper.insert(user);
        return flag>0;
    }

    /**
     * @Description 用户登陆服务
     * @param userVo view层user对象
     * @return 当前登陆用户对象
     */
    @Override
    public UserVo loginUser(UserVo userVo) throws ProjectException {
        if(!session.getAttribute("CHECKCODE_SERVER").equals(request.getHeader("checkCode"))){
            throw new ProjectException("1009","验证码错误");
        }
        //组装条件
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(userVo.getUsername()).
                andPasswordEqualTo(MD5Coder.md5Encode(userVo.getPassword()));
        //查询list结果集
        List<User> users = userMapper.selectByExample(example);
        //转换vo
        List<UserVo> list = BeanConv.toBeanList(users, UserVo.class);
        //判断结果
        if(list.size()==1){
            UserVo userVoResult = list.get(0);
            //存入会话
            String token = UUID.randomUUID().toString();
            userVoResult.setToken(token);
            subjectUserContext.createdSubject(token,BeanConv.toBean(userVoResult, SubjectUser.class));
            return userVoResult;
        }
        return null;
    }

    /**
     * @Description 用户登出服务
     *
     */
    @Override
    public void logoutUser() {
        //从会话中删除当前对象
        subjectUserContext.deleteSubject();
    }

    /**
     * 判断用户登陆状态
     * @return
     */
    @Override
    public Boolean isLogin() {
        //根据当前用户是否存在判断登陆状态
        return subjectUserContext.existSubject();
    }
}

package com.heyu.travel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heyu.travel.enums.StatusEnum;
import com.heyu.travel.exception.ProjectException;
import com.heyu.travel.req.*;
import com.heyu.travel.res.GatewayResp;
import com.heyu.travel.service.*;
import com.heyu.travel.session.SubjectSeller;
import com.heyu.travel.session.SubjectSellerContext;
import com.heyu.travel.session.SubjectUser;
import com.heyu.travel.session.SubjectUserContext;
import com.heyu.travel.utils.EmptyUtil;
import com.heyu.travel.utils.ExceptionsUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * @Description 网关服务实现类
 */
@Service
@Log4j2
public class GatewayServiceImpl implements GatewayService {

    /**
     * @Description 附件服务
     */
    @Autowired
    AffixService affixService;

    /**
     * @Description 分类服务
     */
    @Autowired
    CategoryService categoryService;

    /**
     * @Description 收藏服务
     */
    @Autowired
    FavoriteService favoriteService;

    /**
     * @Description 路线服务
     */
    @Autowired
    RouteService routeService;

    /**
     * @Description 用户服务
     */
    @Autowired
    UserService userService;

    /**
     * @Description 前台对象上下文
     */
    @Autowired
    SubjectUserContext subjectUserContext;

    /**
     * @Description 后台对象上下文
     */
    @Autowired
    SubjectSellerContext subjectSellerContext;

    /**
     * @Description 图片网址
     */
    @Value("${upLoad.webSite}")
    String webSite;

    /**
     * @param method      请求方法
     * @param baseRequest 请求数据
     * @param file        文件上传对象
     * @return 网关响应对象
     * @Description 统一方法处理
     */
    @Override
    public GatewayResp method(String method, BaseRequest<?> baseRequest, MultipartFile file){
        //获得请求对象
        Object data = baseRequest.getData();
        String jsonString = null;
        if(!EmptyUtil.isNullOrEmpty(data)){
            //将data转换成json字符串
            jsonString = JSONObject.toJSONString(data);
        }
        //转换成JsonObject对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //初始化操作人
        GatewayResp resp = new GatewayResp();
        //case1：获取后台对象
        SubjectSeller subjectSeller = subjectSellerContext.getSubject();
        if(!EmptyUtil.isNullOrEmpty(subjectSeller)){
            resp.setUserId(subjectSeller.getId());
            resp.setUserName(subjectSeller.getUsername());
        }
        //case2：获取前台对象
        SubjectUser subjectUser = subjectUserContext.getSubject();
        if(!EmptyUtil.isNullOrEmpty(subjectUser)){
            resp.setUserId(subjectUser.getId());
            resp.setUserName(subjectUser.getUsername());
        }
        Object dataResult = null;
        //ToDo: 优化为策略模式
        try{
            //例如method = findAllCategory
            switch (method){
                case "currentSubjectUser":
                    dataResult = subjectUser;
                    break;

                case "currentSubjectSeller":
                    dataResult = subjectSeller;
                    break;

                    //文件上传
                case "upLoad":
                    dataResult = affixService
                            .upLoad(file,JSONObject.toJavaObject(jsonObject, AffixVo.class));
                    break;

                    //为上传绑定相应的业务ID
                case "bindBusinessId" :
                    dataResult = affixService
                            .bindBusinessId(JSONObject.toJavaObject(jsonObject,AffixVo.class));
                    break;

                    //按业务id查询附件
                case "findAffixByBusinessId" :
                    dataResult = affixService
                            .findAffixByBusinessId(JSONObject.toJavaObject(jsonObject,AffixVo.class));
                    break;

                    //用户注册
                case "registerUser" :
                    dataResult = userService
                            .registerUser(JSONObject.toJavaObject(jsonObject, UserVo.class));
                    break;

                    //登陆
                case "loginUser" :
                    dataResult = userService
                            .loginUser(JSONObject.toJavaObject(jsonObject, UserVo.class));
                    break;
                    //登出
                case "logoutUser" :
                    dataResult = true;
                    userService.logoutUser();
                    break;

                    //是否登陆
                case "isLogin" :
                    dataResult = userService
                            .isLogin();
                    break;

                    //查询所有分类
                case "findAllCategory":
                    dataResult = categoryService
                            .findAllCategory();
                    break;

                    //是否收藏
                case "isFavorited":
                    dataResult = favoriteService
                            .isFavorite(JSONObject.toJavaObject(
                                    jsonObject, FavoriteVo.class));
                    break;

                    //添加收藏
                case "addFavorite":
                    dataResult =  favoriteService
                            .addFavorite(JSONObject.toJavaObject(
                                    jsonObject, FavoriteVo.class));
                    break;

                    //查询我的收藏
                case "findMyFavorite":
                    dataResult =  favoriteService
                            .findMyFavorite(JSONObject.toJavaObject(
                                    jsonObject, FavoriteVo.class),
                                    baseRequest.getPageNum(),
                                    baseRequest.getPageSize());
                    break;

                    //添加路线
                case "addRoute":
                    dataResult = routeService
                            .addRoute(JSONObject.toJavaObject(
                                    jsonObject, RouteVo.class));
                    break;

                    //根据主键查询路线
                case "findRouteById":
                    dataResult = routeService
                            .findRouteById(JSONObject.toJavaObject(
                                    jsonObject, RouteVo.class));
                    break;

                    //根据条件分页查询路线
                case "findRouteByPage":
                    dataResult = routeService
                            .findRouteByPage(JSONObject.toJavaObject(
                                    jsonObject, RouteVo.class),
                                    baseRequest.getPageNum(),
                                    baseRequest.getPageSize());
                    break;

                default:
                    log.error("调用：{}接口：不合法",method);
                    resp = error(resp, new ProjectException("1010","请求不合法"));
                    break;
            }

            //构建返回值
            if(!EmptyUtil.isNullOrEmpty(dataResult)){
                buildGatewayResp(resp,dataResult, StatusEnum.SUCCEED.getCode(),
                        StatusEnum.SUCCEED.getMsg());
            }

        } catch (Exception e) {
            log.error("调用：{}接口出错：{}",method, ExceptionsUtil.getStackTraceAsString(e));
            resp = error(resp,e);
        }
        return resp;
    }

    /**
     * @param resp 需要封装异常信息的网关响应对象
     * @param e      异常信息
     * @return 网关响应对象
     * @Description 统一异常处理
     */
    @Override
    public GatewayResp error(GatewayResp resp, Exception e) {
        ProjectException projectException = null;
        if(e instanceof ProjectException){
            projectException = (ProjectException) e;
        }else {
            projectException = new ProjectException(StatusEnum.FAIL.getCode(),
                    StatusEnum.FAIL.getMsg());
        }
        return buildGatewayResp(resp,null,
                projectException.getCode(),
                projectException.getMessage());
    }

    /**
     * @Description 构建返回参数
     * @param resp 需要封装参数的网关响应对象
     * @param dataResult 返回数据体
     * @param code 编码
     * @param msg 消息
     * @return
     */
    private GatewayResp buildGatewayResp(GatewayResp resp, Object dataResult, String code, String msg){
        resp.setData(dataResult);
        resp.setCode(code);
        resp.setMsg(msg);
        resp.setOperationTime(new Date());
        resp.setWebSite(webSite);
        return resp;
    }
}

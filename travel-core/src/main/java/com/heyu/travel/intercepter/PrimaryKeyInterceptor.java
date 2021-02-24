package com.heyu.travel.intercepter;

import com.heyu.travel.config.SnowflakeIdWorker;
import com.heyu.travel.utils.EmptyUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @Description 主键雪花算法
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Log4j2
public class PrimaryKeyInterceptor implements Interceptor {

    //雪花算法主键生成策略
    private SnowflakeIdWorker snowflakeIdWorker;

    //主键标示
    private String primaryKey;

    public PrimaryKeyInterceptor() {
    }

    public PrimaryKeyInterceptor(SnowflakeIdWorker snowflakeIdWorker) {
        this.snowflakeIdWorker = snowflakeIdWorker;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取参数
        Object[] args = invocation.getArgs();
        //参数判断 不合规则不做拦截
        if(args == null || args.length != 2){
            return invocation.proceed();
        }else{
            MappedStatement ms = (MappedStatement)args[0];
            //操作类型
            SqlCommandType sqlCommandType = ms.getSqlCommandType();
            //只处理插入操作
            if(!EmptyUtil.isNullOrEmpty(sqlCommandType) && sqlCommandType == SqlCommandType.INSERT) {
                //Todo: 批量操作实现
                if(args[1] instanceof Map){
                    List list = (List) (( Map) args[1]).get("list");
                    for (Object obj : list) {
                        setProperty(obj,primaryKey,snowflakeIdWorker.nextId());
                    }
                }else {
                    setProperty(args[1],primaryKey,snowflakeIdWorker.nextId());
                }
            }
            return invocation.proceed();
        }
    }

    /**
     * @Description  使用JDK的动态代理，给target对象创建一个delegate代理对象，
     *               以此来实现方法拦截和增强功能，它会回调intercept()方法。
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        System.out.println("主键生成plugin方法执行，包装的对象（目标对象）："+target);
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
        //指定主键
        primaryKey = properties.getProperty("primaryKey");
        //默认为id
        if(EmptyUtil.isNullOrEmpty(primaryKey)){
            primaryKey = "id";
        }
    }

    /**
     * 设置对象属性值
     * @param obj 对象
     * @param property 属性名称
     * @param value 属性值
     *
     */
    public void setProperty(Object obj, String property, Object value) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        //反射获取指定属性对象
        Field field = obj.getClass().getDeclaredField(property);
        if (!EmptyUtil.isNullOrEmpty(field)) {
            field.setAccessible(true);
            Object val = field.get(obj);
            if (EmptyUtil.isNullOrEmpty(val)) {
                //BeanUtils可以自动转换类型
                BeanUtils.setProperty(obj, property, value);
            }
        }
    }

    public void setSnowflakeIdWorker(SnowflakeIdWorker snowflakeIdWorker) {
        this.snowflakeIdWorker = snowflakeIdWorker;
    }
}

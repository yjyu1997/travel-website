package com.heyu.travel.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.heyu.travel.intercepter.PrimaryKeyInterceptor;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
@MapperScan(basePackages = {"com.heyu.travel.mapper","com.heyu.travel.mapperExt"},
        sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class MybatisConfig {
    @Value("${dataSource.driverClassName}")
    private String driverClassName;

    @Value("${dataSource.url}")
    private String url;

    @Value("${dataSource.username}")
    private String username;

    @Value("${dataSource.password}")
    private String password;

    @Value("${seq.workerId}")
    private String workerId;

    @Value("${seq.datacenterId}")
    private String datacenterId;

    /**
     * @Description 数据源配置
     * 细节：默认不指定名称使用当前方法名为bean的名称
     */

    @Bean("druidDataSource")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * @Description 配置事务管理器
     * 细节：名称必须是：transactionManager，如果更换需要在使用时指定
     */
    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("druidDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * @Description 配置会话管理器
     */
    @Bean("sqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("druidDataSource") DataSource dataSource,
                                                       PageInterceptor pageInterceptor,
                                                       PrimaryKeyInterceptor primaryKeyInterceptor){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //指定数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        //指定对应的别名
        sqlSessionFactoryBean.setTypeAliasesPackage("com.heyu.travel.pojo");
        //指定核心xml的配置
//        try {
//            sqlSessionFactoryBean.setMapperLocations(
//                    new PathMatchingResourcePatternResolver()
//                    .getResources("sqlMapper/*Mapper.xml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //指定高级配置：驼峰、是否返回主键、缓存、日志
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(Log4j2Impl.class);
        //分页拦截
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor,primaryKeyInterceptor});
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean;
    }

    /**
     * @Description 分页插件
     */
    @Bean("pageInterceptor")
    public PageInterceptor initPageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 数据库方言(对那个数据库进行分页)
        properties.setProperty("helperDialect", "mysql");
        // 分页偏移量设置(默认值为false,需要开启)
        // 开启后可以使用当前页页码与每页显示条数进行分页
        properties.setProperty("offsetAsPageNum", "true");
        // 开启分页时查询总数量
        properties.setProperty("rowBoundsWithCount", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }


    /**
     * @Description 雪花算法工厂类
     *
     */
    @Bean("snowflakeIdWorker")
    public SnowflakeIdWorker initSnowflakeIdWorker(){
        return new SnowflakeIdWorker(Integer.parseInt(workerId),Integer.parseInt(datacenterId));
    }

    @Bean("primaryKeyInterceptor")
    public PrimaryKeyInterceptor initPrimaryKeyIntercepter(SnowflakeIdWorker snowflakeIdWorker){
        PrimaryKeyInterceptor primaryKeyInterceptor = new PrimaryKeyInterceptor();
        primaryKeyInterceptor.setSnowflakeIdWorker(snowflakeIdWorker);
        Properties properties = new Properties();
        properties.setProperty("primaryKey","id");
        primaryKeyInterceptor.setProperties(properties);
        return primaryKeyInterceptor;
    }

}

package com.heyu.travel.config;

import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author heyu
 * @Description Redisson 配置类 工厂方法
 */
@Configuration
@Log4j2
@PropertySource("classpath:db.properties")
public class RedissonConfig {
    /**
     * @Description redis连接地址
     */
    @Value("${redis.nodes}")
    private String nodes;

    /**
     * @Description 连接超时时间
     */
    @Value("${redis.connectTimeout}")
    private int connectTimeout;

    /**
     * @Description 最小连接数
     */
    @Value("${redis.connectionPoolSize}")
    private int connectionPoolSize;

    /**
     * @Description 最小空闲连接数
     */
    @Value("${redis.connectionMinimumIdleSize}")
    private int connectionMinimumIdleSize;

    /**
     * @Description 等待数据返回超时时间
     */
    @Value("${redis.timeout}")
    private int timeout;

    /**
     * @Description 刷新时间
     */
    @Value("${redis.retryInterval}")
    private int retryInterval;

    /**
     * @Description 添加配置
     *
     */
    @Bean(value = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient config(){
        log.info("=====初始化RedissonClient开始======");
        String[] nodeArr = nodes.split(",");
        Config config = new Config();
        //单节点
        if(nodeArr.length == 1) {
            config.useSingleServer().setAddress(nodeArr[0])
                    .setConnectTimeout(connectTimeout)
                    .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                    .setConnectionPoolSize(connectionPoolSize)
                    .setTimeout(timeout);
        }
        //集群节点
        else {
            config.useClusterServers().addNodeAddress(nodeArr)
                    .setConnectTimeout(connectTimeout)
                    .setRetryInterval(retryInterval)
                    .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize)
                    .setMasterConnectionPoolSize(connectionPoolSize)
                    .setSlaveConnectionMinimumIdleSize(connectionMinimumIdleSize)
                    .setSlaveConnectionPoolSize(connectionPoolSize)
                    .setTimeout(3000);
        }
        log.info("=====初始化RedissonClient完成======");
        return Redisson.create(config);
    }



}

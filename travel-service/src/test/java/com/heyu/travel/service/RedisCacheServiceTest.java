package com.heyu.travel.service;

import com.heyu.travel.basic.TestConfig;
import com.heyu.travel.pojo.User;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 */
@Log4j2
public class RedisCacheServiceTest extends TestConfig {

    /**
     * @Description 支持多线程测试，防止当前多线程下session的绑定问题
     */
    @Before
    public void before(){
        redisCacheService.setCurrentTest(true);
    }

    @Test
    public void TestTryRateLimiter() throws InterruptedException {
        //初始化50个线程，让每个线程执行一个请求
        ExecutorService executorServicePool = Executors.newFixedThreadPool(50);
        //初始化等待数：50
        //外部阻塞(阻塞主线程，等待50个线程全部完成任务后再结束主线程任务 类似于join()）
        CountDownLatch countDownLatch = new CountDownLatch(50);
        //内部阻塞(最大化并发）
        CyclicBarrier cyclicBarrier = new CyclicBarrier(50);
        for (int i = 0; i < 50; i++) {
            executorServicePool.execute(()-> {
                //阻塞
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("请求线程"+Thread.currentThread().getId()+"时间：{}",new Date());
                redisCacheService.tryRateLimiter("tryRateLimiter_101010101");
                log.info("处理线程"+Thread.currentThread().getId()+"时间：{}",new Date());
                //等待数-1，挂起当前线程
                countDownLatch.countDown();
            });
        }
        //等待,计数为0时一次性执行，模拟50个并发
        countDownLatch.await();
    }

    @Test
    public void testAddSingleCache() throws InterruptedException {

        String key ="testAddSingleCache_101010101";
        //初始化50个线程，让每个线程执行一个请求
        ExecutorService executorServicePool = Executors.newFixedThreadPool(50);
        //初始化等待数：50
        //外部阻塞
        CountDownLatch countDownLatch = new CountDownLatch(50);
        //内部阻塞
        CyclicBarrier cyclicBarrier = new CyclicBarrier(50);
        for (int i = 0; i < 50; i++) {
            executorServicePool.execute(()-> {
                //阻塞
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("请求线程"+Thread.currentThread().getId()+"时间：{}",new Date());
                User user = User.builder().id(1111L).build();
                redisCacheService.addSingleCache(user, key);
                log.info("处理线程"+Thread.currentThread().getId()+"时间：{}",new Date());
                //等待数-1，挂起当前线程
                countDownLatch.countDown();
            });
        }
        //等待,计数为0时一次性执行，模拟50个并发
        countDownLatch.await();
    }


    @Test
    public void testDeleSingleCache() throws InterruptedException {
        String key ="testAddSingleCache_101010101";
        redisCacheService.deleSingleCache(key);
    }

    @Test
    public void testSingleCache() throws InterruptedException {

        String key ="testSingleCache_101010101";
        //初始化50个线程，让每个线程执行一个请求
        ExecutorService executorServicePool = Executors.newFixedThreadPool(50);
        //初始化等待数：50
        //外部阻塞
        CountDownLatch countDownLatch = new CountDownLatch(50);
        //内部阻塞
        CyclicBarrier cyclicBarrier = new CyclicBarrier(50);
        for (int i = 0; i < 50; i++) {
            executorServicePool.execute(()-> {
                //阻塞
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("请求线程"+Thread.currentThread().getId()+"时间：{}",new Date());
                redisCacheService.singleCache(() -> {
                    log.info("执行数据查询");
                    return User.builder().id(1111L).build();
                }, key);
                log.info("处理线程" + Thread.currentThread().getId() + "时间：{}", new Date());
                //等待数-1，挂起当前线程
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }

    @Test
    public void testListCache() throws InterruptedException {

        String key ="testListCache_101010101";
        //初始化50个线程，让每个线程执行一个请求
        ExecutorService executorServicePool = Executors.newFixedThreadPool(50);
        //外部阻塞
        CountDownLatch countDownLatch = new CountDownLatch(50);
        //内部阻塞
        CyclicBarrier cyclicBarrier = new CyclicBarrier(50);
        for (int i = 0; i < 50; i++) {
            executorServicePool.execute(()-> {
                //阻塞线程等待
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("请求线程"+Thread.currentThread().getId()+"时间：{}",new Date());
                //函数式编程 伪数据
                redisCacheService.listCache(() -> {
                    log.info("执行数据查询");
                    List<User> list = new ArrayList<>();
                    list.add(User.builder().id(1111L).build());
                    return list;
                }, key);
                log.info("处理线程" + Thread.currentThread().getId() + "时间：{}", new Date());
                //等待数-1，
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }


    @Test
    public void testDeleListCache() throws InterruptedException {
        String key ="testListCache_101010101";
        redisCacheService.deleSingleCache(key);

    }

    @Test
    public void testMapCache() throws InterruptedException {

        String key ="testMapCache_101010101";
        ///初始化50个线程，让每个线程执行一个请求
        ExecutorService executorServicePool = Executors.newFixedThreadPool(50);
        //外部阻塞
        CountDownLatch countDownLatch = new CountDownLatch(50);
        //内部阻塞
        CyclicBarrier cyclicBarrier = new CyclicBarrier(50);
        for (int i = 0; i < 50; i++) {
            executorServicePool.execute(()-> {
                log.info("请求线程"+Thread.currentThread().getId()+"时间：{}",new Date());
                //阻塞线程等待
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                redisCacheService.mapCache(() -> {
                    log.info("执行数据查询");
                    Map<Integer,User> map = new HashMap<>();
                    map.put(1111,User.builder().id(1111L).build());
                    return map;
                }, key);
                log.info("处理线程" + Thread.currentThread().getId() + "时间：{}", new Date());
                //等待数-1，挂起当前线程
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }

    @Test
    public void testDeleMapCache() throws InterruptedException {

        String key ="testMapCache_101010101";
        redisCacheService.deleMapCache(key);
    }

}



package com.haiyisec.oa.inventorymanager.domain.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.zen.frame.func.threadpool.HyThreadPoolBuilder;
import org.zen.frame.func.threadpool.springthreadpool.HyThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Service
@Slf4j
public class ThreadPoolInitService {
    //单线程线程池，用作监控，核心数1，最大1，队列5
    @Bean("SingleThreadExecutor")
    public Executor createSingleThreadExecutor(){
        log.info("初始化SingleThreadExecutor....");
        HyThreadPoolTaskExecutor executor = new HyThreadPoolBuilder()
                .setPoolName("ImportExcelDatasExecutor")
                .springFiexedBuild(1);

        return executor;
    }

    //初始化核心数为1，最大线程数为5，队列容量为5的线程池
    @Bean("ImportExcelDatasExecutor")
    public Executor createImportExcelDatasExecutor(){
        log.info("初始化ImportExcelDatasExecutor....");
        HyThreadPoolTaskExecutor executor=new HyThreadPoolBuilder()
                .setPoolName("ImportExcelDatasExecutor-")
                .setCoreSize(1)
                .springBuild();

        return executor;
    }
}

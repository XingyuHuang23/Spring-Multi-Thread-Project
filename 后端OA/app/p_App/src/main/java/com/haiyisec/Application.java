package com.haiyisec;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepositoryFactoryBean;


@SpringBootApplication
@ComponentScan(basePackages = {"org.zen", "app", "com.haiyisec"})         //多个包中找Bean
@EntityScan({"org.zen", "app","com.haiyisec"})                                                     //多个包中找Entity
@EnableJpaRepositories(
        basePackages = {"org.zen", "app","com.haiyisec"},                  //多个包中找repository
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)

@EnableJpaAuditing(auditorAwareRef = "baseAuditorAware")
@ServletComponentScan(basePackages = {"org.zen", "app", "com.haiyisec"})
@EnableAsync
@EnableScheduling                   //spring boot自带定时器
@Slf4j
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        if(!ApplicationArgsDeal.dealArgs(args)){
            init(args);
            run(args);
        }
    }

    private static void run(String[] args) {
        //程序运行
        //下面这种设置ctx的方式，只会在webJar运行方式的时候运行，在web容器中不能执行，所以设置放在SpringContextUtil中做
        //Global.ctx = SpringApplication.run(Application.class);
        SpringApplication.run(Application.class);
    }

    public static void init(String[] args){

    }



}

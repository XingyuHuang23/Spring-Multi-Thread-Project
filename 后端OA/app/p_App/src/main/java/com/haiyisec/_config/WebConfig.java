package com.haiyisec._config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by HB on 2018-02-06.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        configurer.registerCallableInterceptors(...);
//        configurer.registerDeferredResultInterceptors(...);
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(timeInterceptor);
    }
}

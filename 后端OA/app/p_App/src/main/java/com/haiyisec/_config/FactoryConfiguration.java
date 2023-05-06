package com.haiyisec._config;

import org.springframework.context.annotation.Configuration;

/**
 * Created by HB on 2016-04-07.
 */
@Configuration
public class FactoryConfiguration {

//    @Bean(name = "jasyptStringEncryptor")
//    static public StringEncryptor stringEncryptor() {
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//        config.setPassword("password");     //@todo: 正式使用前需要设置项目自己的密码
//        config.setAlgorithm("PBEWithMD5AndDES");
//        config.setKeyObtentionIterations("1000");
//        config.setPoolSize("1");
//        config.setProviderName("SunJCE");
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//        config.setStringOutputType("base64");
//        encryptor.setConfig(config);
////        System.out.println(encryptor.encrypt("aaa"));
//        return encryptor;
//    }

}
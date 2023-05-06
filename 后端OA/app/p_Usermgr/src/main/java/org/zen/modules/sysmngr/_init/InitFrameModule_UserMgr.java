package org.zen.modules.sysmngr._init;


import app.frame.appmodule.common.AppModule;
import app.frame.appmodule.common.AppModuleManager;
import app.frame.config.FrameConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 80)
public class InitFrameModule_UserMgr implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("模块初始化_权限管理......");

//        AppModuleManager.setModule(new AppModule().setName(FrameConfig.Module_权限管理).setExists(true).setMenuShow(true));
//        AppModuleManager.setModule(new AppModule().setName(FrameConfig.Module_权限管理).setExists(true).setMenuShow(true));
//        AppModuleManager.setModule(new AppModule().setName(FrameConfig.Module_权限管理).setExists(true).setMenuShow(true));
    }

}
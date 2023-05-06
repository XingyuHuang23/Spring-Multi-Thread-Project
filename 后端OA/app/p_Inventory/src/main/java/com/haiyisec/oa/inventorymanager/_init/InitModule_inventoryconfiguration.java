package com.haiyisec.oa.inventorymanager._init;

import app.config.AppConfig;
import app.frame.appmodule.common.AppModule;
import app.frame.appmodule.common.AppModuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zen.modules.commonservice.dic.domain.service.IDicService;

@Slf4j
@Component
@Order(value = 150)
public class InitModule_inventoryconfiguration implements CommandLineRunner {
    @Autowired
    private IDicService dicService;


    @Override
    public void run(String... strings) throws Exception {
        log.info("初始化库存领域模块....");

        AppModuleManager.setModule(new AppModule().setName(AppConfig.Module_inventoryManager).setExists(true).setMenuShow(true));
        AppModuleManager.setModule(new AppModule().setName(AppConfig.Module_systemSetting).setExists(true).setMenuShow(true));
        AppModuleManager.setModule(new AppModule().setName(AppConfig.Module_authorityManager).setExists(true).setMenuShow(true));

        initDic();

    }

    private void initDic() {
    }
}

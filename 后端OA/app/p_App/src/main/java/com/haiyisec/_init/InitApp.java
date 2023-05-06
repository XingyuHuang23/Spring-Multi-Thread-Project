package com.haiyisec._init;


//import app.dna.comm.domain.po.search.CommSearchTaskPo;
//import app.dna.comm.domain.repository.search.CommSearchTaskHistoryRepository;
//import app.dna.comm.global.PasswordDefine;
//import app.dnatask.domain.repository.NewSearchTaskRepository;
//import app.dnatask.domain.repository.RunningTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zen.frame.base.dic.YesNo;
import org.zen.modules.commonservice.dic.domain.service.IDicService;

@Component
@Order(value = 150)
public class InitApp implements CommandLineRunner {

    @Autowired
    private IDicService dicService;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("应用初始化......");
        initDic();
        initWork();
    }

    private void initDic() {
//        dicService.addDic_Enum("定制字典", TestDicEnum.values());
        dicService.addDic_Enum("是否", YesNo.values());
    }

    private void initWork(){
    }

}
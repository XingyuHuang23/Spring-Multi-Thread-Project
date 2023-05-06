package com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zen.frame.base.domain.obj.IOutResult;

import static com.haiyisec.oa.inventorymanager.domain.service.test.AppCommonErrorConfig.导入_限定策略_阈值_code;
import static com.haiyisec.oa.inventorymanager.domain.service.test.AppCommonErrorConfig.导入_限定策略_阈值_msg;

@Slf4j
@Component
public class AppImportStrategy implements ImportStrategy<AppImportStrategy.ImportParams> {

    @Autowired
    private ImportLimitCheck importLimitCheck;


    @Override
    //@change: 【已处理】  加入 or参数
    public IOutResult operation(IOutResult or, AppImportStrategy.ImportParams importParams) {
        String model = importParams.getModel();
        if(importLimitCheck.countByModel(model) == importParams.maxTaskNum){
            //@change: 【已处理】  失败时 标识应该为false
            //@change:  or 未设置 AppCommonErrorConfig
            or.setSuccess(false);
            or.setErrorCode(导入_限定策略_阈值_code);
            or.setMsg(导入_限定策略_阈值_msg);
        }
        return  or;
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    public static class ImportParams{
        //内部类,校验参数的定义，max_num,max_task_num,max_user_task_num
        //@change:  去掉该属性
        private int maxTaskNum; //同一模块最多允许几个task
        private String model;
    }
}

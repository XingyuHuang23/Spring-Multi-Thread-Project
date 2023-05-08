package com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy;

import org.zen.frame.base.domain.obj.IOutResult;


//@change:  类名不明确
public interface ImportStrategy<ImportParams>  {
    //@change: 【已处理】  加入 or参数
    IOutResult operation(IOutResult or,ImportParams operation);

}

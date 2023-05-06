package com.haiyisec.oa.inventorymanager.domain.service.test;

import org.zen.frame.base.domain.obj.CheckResult;

import java.util.List;

//@change: 接口类 以 I 开头
public interface IHyPreDateHandler extends IHyDataAdaption {

    //@change: 接口中的变量默认的修饰词：public static final，这里的checkResult是static的

    void preDateHandler(CheckResult cr, List successDatas, List failDatas, String taskId);

}
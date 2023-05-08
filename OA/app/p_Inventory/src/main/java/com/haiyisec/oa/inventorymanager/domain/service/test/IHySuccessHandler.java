package com.haiyisec.oa.inventorymanager.domain.service.test;

import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.HyExcelModel;

import java.util.List;

//@change: 接口类 以 I 开头
public interface IHySuccessHandler extends IHyDataAdaption {

   void successHandler(List<HyExcelModel> successDatas, String taskId);
}

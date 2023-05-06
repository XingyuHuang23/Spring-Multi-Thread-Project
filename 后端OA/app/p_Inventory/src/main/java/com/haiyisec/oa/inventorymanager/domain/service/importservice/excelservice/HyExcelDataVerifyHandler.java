package com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice;


import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import java.util.ArrayList;
import java.util.List;

public interface HyExcelDataVerifyHandler<T> extends IExcelVerifyHandler<T> {
     List  failData = new ArrayList<>();
}

package com.haiyisec.oa.inventorymanager.domain.model.vo.goods;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

public class CommodityExcelVerifyVO extends CommodityVO implements HyExcelModel{
    private String errorMsg;
    private int rowNum;
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String s) {
        this.errorMsg=s;
    }

    @Override
    public Integer getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(Integer i) {
        this.rowNum =i;
    }
}

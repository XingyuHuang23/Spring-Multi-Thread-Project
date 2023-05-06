package com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityVO;
import com.haiyisec.oa.inventorymanager.domain.service.dicservice.InventoryDicService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.zen.frame.api.Api;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class HyExcelDataHandler<T> extends ExcelDataHandlerDefaultImpl<T> {

    public String toCode(List<DicItemVO> listOfDicItemVO, String value) {
        if (listOfDicItemVO != null && listOfDicItemVO.size() > 0) {
            for (DicItemVO dicItemVO : listOfDicItemVO) {
                if (value.equals(dicItemVO.getText()))
                    return dicItemVO.getCode();
            }
        }
        return value;
    }
}
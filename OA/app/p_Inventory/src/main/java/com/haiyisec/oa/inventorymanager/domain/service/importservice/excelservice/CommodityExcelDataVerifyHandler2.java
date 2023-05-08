package com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityVO;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.dicservice.InventoryDicService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zen.frame.api.Api;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class CommodityExcelDataVerifyHandler2 implements HyExcelDataVerifyHandler<CommodityVO> {

    private List<String> type;
    private List<String> position;
    InventoryDicService inventoryDicService;

    @Autowired
    ImportMonitorService importMonitorService;

    public CommodityExcelDataVerifyHandler2() {
        inventoryDicService = Api.Spring.getBean(InventoryDicService.class);
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(CommodityVO commodityVO) {
        type = toCode(inventoryDicService.getDic_DD_type(new HashMap<>()));
        position = toCode(inventoryDicService.getDic_DD_position(new HashMap<>()));

        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();
        result.setSuccess(true);

        if (!type.contains(commodityVO.getType())) {
            failData.add(commodityVO);
            //importMonitorService.addFailData(commodityVO);
            result.setSuccess(false);
            result.setMsg("不存在该物品类别，保存错误。");
            return result;
        }
        if (!position.contains(commodityVO.getPosition())) {
            failData.add(commodityVO);
            result.setSuccess(false);
            result.setMsg("不存在该所属位置，保存错误。");
            return result;
        }

        return result;
    }


    private List<String> toCode(List<DicItemVO> list) {
        List<String> stringList = new ArrayList<>();
        for (DicItemVO dicItemVO : list) {
            stringList.add(dicItemVO.getCode());
        }
        return stringList;
    }
}

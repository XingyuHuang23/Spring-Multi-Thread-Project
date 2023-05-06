package com.haiyisec.oa.inventorymanager.domain.service.excelservice;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityFailVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityVO;
import com.haiyisec.oa.inventorymanager.domain.service.dicservice.InventoryDicService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.zen.frame.api.Api;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;

/*
 * 参考 http://easypoi.mydoc.io/
 *      DateTimeExcelDataHandler
 *
 *      导入/导出数据处理类
 * */
//@Service
public class CommodityExcelDataHandler extends ExcelDataHandlerDefaultImpl<CommodityVO> {
    private List<DicItemVO> listOfType;
    private List<DicItemVO> listOfPosition;

    public CommodityExcelDataHandler() {
        InventoryDicService inventoryDicService = Api.Spring.getBean(InventoryDicService.class);
        listOfType = inventoryDicService.getDic_DD_type(new HashMap<>());
        listOfPosition = inventoryDicService.getDic_DD_position(new HashMap<>());
    }

    @Override
    public Object importHandler(CommodityVO obj, String name, Object value) {
        switch (name) {
            case "物品类别":
                value = toCode(listOfType, value.toString());
                break;
            case "所属位置":
                value = toCode(listOfPosition, value.toString());
                break;
        }

        return super.importHandler(obj, name, value);
    }

    private String toCode(List<DicItemVO> listOfDicItemVO, String value) {
        if (listOfDicItemVO != null && listOfDicItemVO.size() > 0) {
            for (DicItemVO dicItemVO : listOfDicItemVO) {
                if (value.equals(dicItemVO.getText()))
                    return dicItemVO.getCode();
            }
        }
        return value;
    }

    public List<Commodity> toEntity(List<CommodityExcelVerifyVO> list) {
//        List<CommodityVO> _datasMap = excelImportResult.getList();
        List<Commodity> datasMap = new ArrayList<>();
        for (CommodityExcelVerifyVO commodityVO : list) {
            Commodity commodity = new Commodity();
            commodity.setCommodityNo(commodityVO.getCommodityNo());
            commodity.setName(commodityVO.getName());
            commodity.setPosition(commodityVO.getPosition());
            commodity.setStoreId("000");
            commodity.setType(commodityVO.getType());
            commodity.setUnit(commodityVO.getUnit());

            datasMap.add(commodity);
        }
        return datasMap;
    }

    public List<CommodityExcelVerifyVO> toFailEntity(List<CommodityExcelVerifyVO> list) {
        for (CommodityExcelVerifyVO commodityExcelVerifyVO : list) {
            for (DicItemVO dicItemVO : listOfType) {
                if (commodityExcelVerifyVO.getType().equals(dicItemVO.getCode()))
                    commodityExcelVerifyVO.setType(dicItemVO.getText());
            }

            for (DicItemVO dicItemVO : listOfPosition) {
                if (commodityExcelVerifyVO.getPosition().equals(dicItemVO.getCode()))
                    commodityExcelVerifyVO.setPosition(dicItemVO.getText());
            }
        }
        return list;
    }
}

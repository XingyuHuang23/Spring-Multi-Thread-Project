package com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityVO;
import com.haiyisec.oa.inventorymanager.domain.service.dicservice.InventoryDicService;
import org.apache.poi.ss.formula.functions.T;
import org.zen.frame.api.Api;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommodityExcelDataHandler2 extends HyExcelDataHandler<CommodityVO> {
    private List<DicItemVO> listOfType;
    private List<DicItemVO> listOfPosition;


    InventoryDicService inventoryDicService;


    public CommodityExcelDataHandler2() {
        inventoryDicService = Api.Spring.getBean(InventoryDicService.class);
    }

    @Override
    public Object importHandler(CommodityVO obj, String name, Object value) {


        listOfType = inventoryDicService.getDic_DD_type(new HashMap<>());
        listOfPosition = inventoryDicService.getDic_DD_position(new HashMap<>());
        if (value == null || "".equals(value)) {

            return null;
        }

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


    //vo转实体
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

    //预留方法
    //@todo 应该移动到相关的业务service
    public List<CommodityExcelVerifyVO> toFailEntity(List<CommodityExcelVerifyVO> list) {
        listOfType = inventoryDicService.getDic_DD_type(new HashMap<>());
        listOfPosition = inventoryDicService.getDic_DD_position(new HashMap<>());
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

    public CommodityExcelVerifyVO toFailEntity(CommodityExcelVerifyVO commodityExcelVerifyVO) {
        listOfType = inventoryDicService.getDic_DD_type(new HashMap<>());
        listOfPosition = inventoryDicService.getDic_DD_position(new HashMap<>());

        if (commodityExcelVerifyVO.getType() != null) {
            for (DicItemVO dicItemVO : listOfType) {
                if (commodityExcelVerifyVO.getType().equals(dicItemVO.getCode())) {
                    commodityExcelVerifyVO.setType(dicItemVO.getText());
//                    break;
                }
            }
        }

        if (commodityExcelVerifyVO.getPosition() != null) {
            for (DicItemVO dicItemVO : listOfPosition) {
                if (commodityExcelVerifyVO.getPosition().equals(dicItemVO.getCode())) {
                    commodityExcelVerifyVO.setPosition(dicItemVO.getText());
//                    break;
                }
            }
        }

        return commodityExcelVerifyVO;
    }
}

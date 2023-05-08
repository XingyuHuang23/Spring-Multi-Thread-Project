package com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.HyExcelModel;
import com.haiyisec.oa.inventorymanager.domain.service.dicservice.InventoryDicService;
import lombok.extern.slf4j.Slf4j;
import org.zen.frame.api.Api;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;

import java.util.*;

/*
 * 参考 http://easypoi.mydoc.io/
 *      DateTimeExcelDataHandler
 *
 *      导入/导出数据处理类
 * */
//@Service
@Slf4j
public  class CommodityExcelDataHandler extends ExcelDataHandlerDefaultImpl<CommodityVO> {
    private List<DicItemVO> listOfType;
    private List<DicItemVO> listOfPosition;

    InventoryDicService inventoryDicService;

    public CommodityExcelDataHandler() {
        inventoryDicService = Api.Spring.getBean(InventoryDicService.class);
//        listOfType = inventoryDicService.getDic_DD_type(new HashMap<>());
//        listOfPosition = inventoryDicService.getDic_DD_position(new HashMap<>());
    }

    @Override
    public Object importHandler(CommodityVO obj, String name, Object value) {
        //return  config.getDataHandler().transfer(obj,name,value);

        listOfType = inventoryDicService.getDic_DD_type(new HashMap<>());
        listOfPosition = inventoryDicService.getDic_DD_position(new HashMap<>());
        if (value == null || "".equals(value)) {
            log.info(name + " 的value为空....");
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

    private String toCode(List<DicItemVO> listOfDicItemVO, String value) {
        if (listOfDicItemVO != null && listOfDicItemVO.size() > 0) {
            for (DicItemVO dicItemVO : listOfDicItemVO) {
                if (value.equals(dicItemVO.getText()))
                    return dicItemVO.getCode();
            }
        }
        return value;
    }

    //vo转实体
    public List<Commodity> toEntity(List<HyExcelModel> list) {

        List<Commodity> datasMap = new ArrayList<>();

        List<CommodityExcelVerifyVO> commodityVO_list = new ArrayList<>();
        for(HyExcelModel b : list){
            commodityVO_list.add((CommodityExcelVerifyVO)b);    //强转后依次加到子类List里
        }

        for (CommodityExcelVerifyVO commodityVO : commodityVO_list) {
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

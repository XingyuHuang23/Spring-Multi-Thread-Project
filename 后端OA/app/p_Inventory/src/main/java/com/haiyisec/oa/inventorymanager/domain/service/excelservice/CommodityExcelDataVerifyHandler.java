package com.haiyisec.oa.inventorymanager.domain.service.excelservice;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityVO;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;
import com.haiyisec.oa.inventorymanager.domain.service.dicservice.InventoryDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zen.frame.api.Api;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@Service
public class CommodityExcelDataVerifyHandler implements IExcelVerifyHandler<CommodityVO> {
//    @Autowired
//    private CommodityRepository commodityRepository;

    private List<String> type;
    private List<String> position;

    public CommodityExcelDataVerifyHandler() {
        InventoryDicService inventoryDicService = Api.Spring.getBean(InventoryDicService.class);
        type = toText(inventoryDicService.getDic_DD_type(new HashMap<>()));
        position = toText(inventoryDicService.getDic_DD_position(new HashMap<>()));
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(CommodityVO commodityVO) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();
        result.setSuccess(true);

        if (!type.contains(commodityVO.getType())) {
            result.setSuccess(false);
            result.setMsg("不存在该物品类型选项，保存错误。");
            return result;
        }
        if (!position.contains(commodityVO.getPosition())) {
            result.setSuccess(false);
            result.setMsg("不存在该所属位置，保存错误。");
            return result;
        }
        //根据名字以及位置查询保存
//        Commodity commodity = commodityRepository.findCommodityByNameAndPosition(commodityVO.getName(), commodityVO.getPosition());
//        if (commodity != null) {
//            result.setSuccess(false);
//            result.setMsg("该物品档案已存在，保存错误。");
//            return result;
//        }

        return result;
    }

    private List<String> toText(List<DicItemVO> list) {
        List<String> stringList = new ArrayList<>();
        for (DicItemVO dicItemVO : list) {
            stringList.add(dicItemVO.getCode());
        }
        return stringList;
    }
}

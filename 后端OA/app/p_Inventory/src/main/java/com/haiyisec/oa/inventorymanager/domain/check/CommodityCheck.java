package com.haiyisec.oa.inventorymanager.domain.check;


import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.repository.*;
import org.zen.frame.api.Domain_DB;
import org.zen.frame.base.domain.annotation.check.Zen_DC_Method;
import org.zen.frame.base.domain.check.dic.DomainOperType;
import org.zen.frame.base.domain.check.obj.CheckMethodCallObject;
import org.zen.frame.base.domain.check.obj.CheckObject;
import org.zen.frame.base.domain.check.service.CheckService;
import org.zen.frame.base.domain.obj.CheckResult;

public class CommodityCheck extends CheckService<Commodity> {
    @Zen_DC_Method(id = "物品名称是否已存在检查", msgTemplate = "该物品名称已存在,不可保存")
    public static CheckResult commodity_name_unique(CheckMethodCallObject<Commodity> cmco) {
        Commodity commodity = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (commodity != null) {
            CommodityRepository commodityRepository = Domain_DB.getDao(Commodity.class, CommodityRepository.class);
            Long count = 0L;
            String commodity_id = commodity.getId();
            String commodity_name = commodity.getName();

            if (commodity_id == null || "".equals(commodity_id))
                count = commodityRepository.countByName(commodity_name);  //新增判断
            else
                count = commodityRepository.countByNameAndIdNot(commodity_name, commodity_id);  //修改判断

            if (count > 0)
                setFalse_CheckResult(cr, co);
        }

        return cr;
    }

//    @Zen_DC_Method(id = "物品序列号是否已存在检查", msgTemplate = "该物品序列号已存在,不可保存")
////    public static CheckResult commodity_no_unique(CheckMethodCallObject<Commodity> cmco) {
////        Commodity commodity = cmco.getEntity();
////        CheckResult cr = cmco.getCr();
////        CheckObject co = cmco.getCo();
////
////        if (commodity != null) {
////            CommodityRepository commodityRepository = Domain_DB.getDao(Commodity.class, CommodityRepository.class);
////            Long count = 0L;
////            String commodity_id = commodity.getId();
////            String commodity_no = commodity.getCommodityNo();
////
////            if (commodity_id == null || "".equals(commodity_id))
////                count = commodityRepository.countByCommodityNo(commodity_no);  //新增判断
////            else
////                count = commodityRepository.countByCommodityNoAndIdNot(commodity_no, commodity_id);  //修改判断
////
////            if (count > 0)
////                setFalse_CheckResult(cr, co);
////        }
////
////        return cr;
////    }

    @Zen_DC_Method(id = "物品选项是否已被使用", msgTemplate = "该物品已被使用，不允许删除", doType = DomainOperType.delete)
    public static CheckResult commodity_using(CheckMethodCallObject<Commodity> cmco) {

        Commodity commodity = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (commodity != null) {

            CommodityRepository commodityRepository = Domain_DB.getDao(Commodity.class, CommodityRepository.class);
            InStoreRepository instoreRepository = Domain_DB.getDao(InStore.class, InStoreRepository.class);
            OutStoreRepository outstoreRepository = Domain_DB.getDao(OutStore.class,OutStoreRepository.class);
            InventoryStatisticsRepository isRepository =  Domain_DB.getDao(InventoryStatistics.class,InventoryStatisticsRepository.class);
            String commodity_id = commodity.getId();

            if (!(commodity_id == null || "".equals(commodity_id))){

                if(instoreRepository.countByCommodityId(commodity_id)>0){
                    setFalse_CheckResult(cr, co);
                }else if(outstoreRepository.countByCommodityId(commodity_id)>0){
                    setFalse_CheckResult(cr, co);
                }else if(isRepository.countByCommodityId(commodity_id)>0){
                    setFalse_CheckResult(cr, co);
                }
            }
        }
        return cr;
    }
}

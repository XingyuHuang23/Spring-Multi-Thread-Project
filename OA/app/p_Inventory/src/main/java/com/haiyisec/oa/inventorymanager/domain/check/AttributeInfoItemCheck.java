package com.haiyisec.oa.inventorymanager.domain.check;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoItemRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InStoreRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.OutStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.zen.frame.api.Domain_DB;
import org.zen.frame.base.domain.annotation.check.Zen_DC_Method;
import org.zen.frame.base.domain.check.dic.DomainOperType;
import org.zen.frame.base.domain.check.obj.CheckMethodCallObject;
import org.zen.frame.base.domain.check.obj.CheckObject;
import org.zen.frame.base.domain.check.service.CheckService;
import org.zen.frame.base.domain.obj.CheckResult;

public class AttributeInfoItemCheck extends CheckService<AttributeInfoItem> {


    @Zen_DC_Method(id = "属性选项名称是否已存在检查", msgTemplate = "该属性选项已存在,不可保存")
    public static CheckResult attributeItemInfo_unique(CheckMethodCallObject<AttributeInfoItem> cmco) {
        AttributeInfoItem attributeInfoItem = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (attributeInfoItem != null) {
            AttributeInfoItemRepository attributeInfoItemRepository = Domain_DB.getDao(AttributeInfoItem.class, AttributeInfoItemRepository.class);
            Long count = 0L;
            String attributeItemInfo_id = attributeInfoItem.getId();
            String attributeItemInfo_name = attributeInfoItem.getName();

            if (attributeItemInfo_id == null || "".equals(attributeItemInfo_id))  //新增
                count = attributeInfoItemRepository.countByNameAndAttributeInfoId(attributeInfoItem.getName(),attributeInfoItem.getAttributeInfoId() );
            else                                //修改
                count = attributeInfoItemRepository.countByNameAndIdNotAndAttributeInfoId(attributeInfoItem.getName(), attributeItemInfo_id,attributeInfoItem.getAttributeInfoId());

            if (count > 0)
                setFalse_CheckResult(cr, co);
        }

        return cr;
    }


    @Zen_DC_Method(id = "属性选项是否已被使用", msgTemplate = "该属性选项已被使用，不允许删除", doType = DomainOperType.delete)
    public static CheckResult attributeItemInfo_using(CheckMethodCallObject<AttributeInfoItem> cmco) {

        AttributeInfoItem attributeInfoItem = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (attributeInfoItem != null) {
            AttributeInfoItemRepository attributeInfoItemRepository = Domain_DB.getDao(AttributeInfoItem.class, AttributeInfoItemRepository.class);
            CommodityRepository commodityRepository = Domain_DB.getDao(Commodity.class, CommodityRepository.class);
            InStoreRepository instoreRepository = Domain_DB.getDao(InStore.class, InStoreRepository.class);
            OutStoreRepository outstoreRepository = Domain_DB.getDao(OutStore.class,OutStoreRepository.class);

            String attributeItemInfo_id = attributeInfoItem.getId();

            if (!(attributeItemInfo_id == null || "".equals(attributeItemInfo_id))){

              if(commodityRepository.countByPositionOrType(attributeItemInfo_id,attributeItemInfo_id)>0){
                  setFalse_CheckResult(cr, co);
              }else if(instoreRepository.countByPurchaser(attributeItemInfo_id)>0){
                  setFalse_CheckResult(cr, co);
              }else if(outstoreRepository.countByApplicant(attributeItemInfo_id)>0){
                  setFalse_CheckResult(cr, co);
              }
            }
        }
        return cr;
    }
}

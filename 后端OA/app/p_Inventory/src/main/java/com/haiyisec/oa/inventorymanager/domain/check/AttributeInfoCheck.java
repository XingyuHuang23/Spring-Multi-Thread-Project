package com.haiyisec.oa.inventorymanager.domain.check;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoRepository;
import com.haiyisec.oa.inventorymanager.domain.service.AttributeItemInfoService;
import lombok.extern.slf4j.Slf4j;
import org.zen.frame.api.Api;
import org.zen.frame.api.Domain_DB;
import org.zen.frame.base.domain.annotation.check.Zen_DC_Method;
import org.zen.frame.base.domain.check.dic.DomainOperType;
import org.zen.frame.base.domain.check.obj.CheckMethodCallObject;
import org.zen.frame.base.domain.check.obj.CheckObject;
import org.zen.frame.base.domain.check.service.CheckService;
import org.zen.frame.base.domain.obj.CheckResult;

//CheckService_Owner涉及用户权限检查
@Slf4j
public class AttributeInfoCheck extends CheckService<AttributeInfo> {
    @Zen_DC_Method(id = "属性名称是否已存在检查", msgTemplate = "该属性名称已存在,不可保存")
    public static CheckResult attributeInfo_unique(CheckMethodCallObject<AttributeInfo> cmco) {

        AttributeInfo attributeInfo = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (attributeInfo != null) {
            AttributeInfoRepository attributeInfoRepository = Domain_DB.getDao(AttributeInfo.class, AttributeInfoRepository.class);
            String attributeInfo_id = attributeInfo.getId();
            Long count = 0L;
            String attributeInfo_name = attributeInfo.getName();

            if (attributeInfo_id==null||"".equals(attributeInfo_id))
                count = attributeInfoRepository.countByName(attributeInfo_name);
            else
                count = attributeInfoRepository.countByNameAndIdNot(attributeInfo_name, attributeInfo_id);

            if (count > 0)
                setFalse_CheckResult(cr, co);

        }

        return cr;
    }

    @Zen_DC_Method(id = "属性名称是否存在子选项检查", msgTemplate = "该属性名称存在子选项,不可删除", doType = DomainOperType.delete)
    public static CheckResult attributeInfo_hasItems(CheckMethodCallObject<AttributeInfo> cmco) {
        //todo 按照规则名字命名方法
        //校验属性名称删除
        //调用对应PO的service的方法进行检查
        AttributeInfo attributeInfo = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        AttributeItemInfoService attributeItemInfoService = Api.Spring.getBean(AttributeItemInfoService.class);
        boolean flag = attributeItemInfoService.checkExistAttributeItemInfo(attributeInfo.getId());

        if (flag) {
            setFalse_CheckResult(cr, co);
        }
        return cr;
    }


}

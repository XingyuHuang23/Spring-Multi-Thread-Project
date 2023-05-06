package com.haiyisec.oa.inventorymanager.domain.service;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;

@Service
public class AttributeItemInfoService  extends BaseService<AttributeInfoItem> {

    @Autowired
    private AttributeInfoItemRepository attributeInfoItemRepository;

    @Override
    public BaseRepository<AttributeInfoItem, String> getDao() {
        return attributeInfoItemRepository;
    }



    public Page<AttributeInfoItem> queryByAttributeInfoId(String attributePOId, Pageable page) {
        return attributeInfoItemRepository.findByAttributeInfoId(attributePOId,page);
    }

    public void delete(CheckResult cr, AttributeInfoItem attributeInfoItem) {
        attributeInfoItemRepository.delete(cr, attributeInfoItem);
    }

    public boolean checkExistAttributeItemInfo(String attributePOId) {
        Long count = 0L;
        count = attributeInfoItemRepository.countByAttributeInfoId(attributePOId);
        if (count > 0)
            return true;
        else
            return false;
    }
}

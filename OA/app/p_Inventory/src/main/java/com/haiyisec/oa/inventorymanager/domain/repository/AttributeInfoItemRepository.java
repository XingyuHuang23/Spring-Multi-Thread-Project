package com.haiyisec.oa.inventorymanager.domain.repository;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.util.List;

public interface AttributeInfoItemRepository extends BaseRepository<AttributeInfoItem, String> {

    Page<AttributeInfoItem> findByAttributeInfoId(String id, Pageable page);

    List<AttributeInfoItem> findByAttributeInfoId(String id);  //字典项使用

    Long countByAttributeInfoId(String attributeInfoId);

    Long countByName(String name);

    List<AttributeInfoItem> findByName(String attributeItemInfo_name);


    Long countByNameAndIdNot(String attributeItemInfo_name, String attributeItemInfo_id);

    Long countByNameAndAttributeInfoId(String name, String attributeInfoId);

    Long countByNameAndIdNotAndAttributeInfoId(String name, String attributeItemInfo_id, String attributeInfoId);
}

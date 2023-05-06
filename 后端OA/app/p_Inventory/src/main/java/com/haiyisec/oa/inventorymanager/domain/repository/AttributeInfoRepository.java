package com.haiyisec.oa.inventorymanager.domain.repository;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.util.List;

public interface AttributeInfoRepository extends BaseRepository<AttributeInfo, String> {
    //考虑索引问题
    //取单个findOne
    Page<AttributeInfo> findByName(String name, Pageable page);

    AttributeInfo findByName(String name);  //字典项查询使用

    Long countByName(String name);


    Long countByNameAndIdNot(String attributeInfo_name, String attributeInfo_id);
}

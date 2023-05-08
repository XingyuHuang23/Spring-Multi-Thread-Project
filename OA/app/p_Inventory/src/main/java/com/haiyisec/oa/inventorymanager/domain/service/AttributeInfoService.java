package com.haiyisec.oa.inventorymanager.domain.service;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;

import java.util.List;

@Service
public class AttributeInfoService extends BaseService<AttributeInfo> {
    @Autowired
    private AttributeInfoRepository attributeInfoRepository;

    @Override
    public BaseRepository<AttributeInfo, String> getDao() {
        return attributeInfoRepository;
    }


//    public void delete(CheckResult cr, AttributeInfo attributeInfo) {
//        attributeInfoRepository.delete(cr, attributeInfo);
//    }

    //名称要规范
    //动态查询使用框架的方法
    public Page<AttributeInfo> queryByName(String attriName, Pageable page) {
        return attributeInfoRepository.findByName(attriName,page);
    }

    //添加check方法
}

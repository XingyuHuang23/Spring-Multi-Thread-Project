package com.haiyisec.oa.inventorymanager.domain.service;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;

@Slf4j
@Service
public class CommodityService extends BaseService<Commodity> {
    @Autowired
    private CommodityRepository commodityRepository;

    @Override
    public BaseRepository<Commodity, String> getDao() {
        return commodityRepository;
    }

    public void delete(CheckResult cr, Commodity commodity) {
        commodityRepository.delete(cr, commodity);
    }
}

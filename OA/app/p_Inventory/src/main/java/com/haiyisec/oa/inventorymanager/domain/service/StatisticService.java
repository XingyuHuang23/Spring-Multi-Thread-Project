package com.haiyisec.oa.inventorymanager.domain.service;


import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;

import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.repository.InventoryStatisticsRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;

import java.util.List;


@Service
public class StatisticService extends BaseService<InventoryStatistics> {

    @Autowired
    InventoryStatisticsRepository inventoryStatisticsRepository;
    @Override

    public BaseRepository<InventoryStatistics, String> getDao() {
        return inventoryStatisticsRepository;
    }

    public List<InventoryStatistics> queryByKey(String key) {
        return  inventoryStatisticsRepository.findByLikeCommodityName(key);
    }


//    public Page queryByKey(String key, Pageable p) {
//        return inventoryStatisticsRepository.findLikeByCommodityName(key,p);
//    }
}

package com.haiyisec.oa.inventorymanager.domain.repository;

import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.util.List;


public interface InventoryStatisticsRepository extends BaseRepository<InventoryStatistics, String> {

    InventoryStatistics findByCommodityId(String id);


    @Query(value = "SELECT * from  inventory_statistics t1  where t1.commodity_name like CONCAT('%', ?1, '%') ", nativeQuery = true)
    List<InventoryStatistics> findByLikeCommodityName(String key);

    Long countByCommodityId(String commodity_id);
}

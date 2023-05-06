package com.haiyisec.oa.inventorymanager.domain.repository;



import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import org.springframework.data.jpa.repository.Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.util.List;

public interface OutStoreRepository extends BaseRepository<OutStore, String> {

    @Query(value = "SELECT * from out_store t1 join commodity t2 on  t1.commodity_id = t2.id where t2.name like '%' ?1 '%'", nativeQuery = true)
    List<OutStore> findByKey(String key);

    @Query(value = "select sum(DISTINCT outstore_quantity) from out_store where commodity_id = ?1", nativeQuery = true)
    int hadUse(String commodity_id);

    Long countByApplicant(String id);

    Long countByCommodityId(String commodity_id);
}

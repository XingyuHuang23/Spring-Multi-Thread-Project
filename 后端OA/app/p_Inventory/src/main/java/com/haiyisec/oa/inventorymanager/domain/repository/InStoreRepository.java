package com.haiyisec.oa.inventorymanager.domain.repository;


import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import org.springframework.data.jpa.repository.Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.lang.annotation.Native;
import java.util.List;

public interface InStoreRepository extends BaseRepository<InStore, String> {

    @Query(value = "SELECT * from in_store t1 join commodity t2 on  t1.commodity_id = t2.id where t2.name like '%' ?1 '%'", nativeQuery = true)
    List<InStore> findByKey(String key);


    long countByPurchaser(String id);

    long countByCommodityId(String commodity_id);
}

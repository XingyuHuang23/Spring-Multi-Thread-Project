package com.haiyisec.oa.inventorymanager.domain.repository;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.CommodityFail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.util.List;

public interface CommodityFailRepository extends BaseRepository<CommodityFail, String> {
    void deleteByImportMonitorId(String importMonitorId);

    List<CommodityFail> findByTaskId(String taskId);

    @Modifying
    @Query(value = "DELETE FROM commodity_fail WHERE 1=1 and has_del='0' and import_monitor_id=?1", nativeQuery = true)
    void deleteAll(String importMonitorId);
}

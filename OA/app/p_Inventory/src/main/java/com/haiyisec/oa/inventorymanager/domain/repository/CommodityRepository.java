package com.haiyisec.oa.inventorymanager.domain.repository;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import org.springframework.data.jpa.repository.Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.util.List;

public interface CommodityRepository extends BaseRepository<Commodity, String> {
        Long countByName(String name);

        Long countByNameAndIdNot(String commodity_name, String commodity_id);

        Long countByPositionOrType(String id,String id2);

        Long countByCommodityNo(String no);

        Long countByCommodityNoAndIdNot(String no, String commodity_id);

        Commodity findCommodityByNameAndPosition(String name,String position);

        @Query(value = "SELECT * from commodity t3 join(select DISTINCT(t1.commodity_id) as id from in_store t1 join inventory_statistics t2 on t1.commodity_id = t2.commodity_id where t2.rest > 0 and t1.has_del = '0' and t2.has_del = '0') t4 on t3.id = t4.id  where has_del = '0'", nativeQuery = true)
        List<Commodity> findAndRest();  //字典项使用

        List findByPosition(String attributeItemInfo_id); //check使用
}

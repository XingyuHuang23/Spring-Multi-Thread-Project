package com.haiyisec.oa.inventorymanager.domain.service;

import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.repository.InventoryStatisticsRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.OutStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;


@Service
public class OutStoreService extends BaseService<OutStore> {

    @Autowired
    private OutStoreRepository outStoreRepository;
    @Autowired
    private InventoryStatisticsRepository inventoryStatisticsRepository;


    @Override
    public BaseRepository<OutStore, String> getDao() {
        return outStoreRepository;
    }

    public InventoryStatistics rest(String Id) {
            return inventoryStatisticsRepository.findByCommodityId(Id);
    }

    public void outstore(CheckResult cr, OutStore outstore) {

        String commodityId =  outstore.getCommodityId();
        InventoryStatistics inventoryStatistics = inventoryStatisticsRepository.findByCommodityId(commodityId);

        if(inventoryStatistics !=null){
            outStoreRepository.save(cr,outstore);
            inventoryStatistics.setRest(inventoryStatistics.getRest()- outstore.getOutstoreQuantity());

            if(inventoryStatistics.getHadUse()==0){
                inventoryStatistics.setHadUse(outstore.getOutstoreQuantity());
            }else{
                Integer num = Integer.valueOf(outstore.getOutstoreQuantity())+ Integer.valueOf(inventoryStatistics.getHadUse());
                inventoryStatistics.setHadUse(num);
            }
            inventoryStatisticsRepository.save(inventoryStatistics);
        }
    }


    public List<OutStore> queryByKey(String key) {
        key  = key.trim();
        try{
            if(key.equals("")||key==null){
                return outStoreRepository.findAll();
            }else{
                return outStoreRepository.findByKey(key);
            }
        }catch(Exception e){
            return outStoreRepository.findAll();
        }
    }

}

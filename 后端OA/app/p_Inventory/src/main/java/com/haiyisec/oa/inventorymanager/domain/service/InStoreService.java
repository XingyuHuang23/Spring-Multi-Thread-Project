package com.haiyisec.oa.inventorymanager.domain.service;


import com.haiyisec.oa.inventorymanager.domain.check.InstoreCheck;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoItemRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InStoreRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InventoryStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InStoreService extends BaseService<InStore>{


    @Autowired
    private InStoreRepository instoreRepository;
    @Autowired
    private InventoryStatisticsRepository inventoryStatisticsRepository;
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private AttributeInfoItemRepository attributeInfoItemRepository;
    @Override
    public BaseRepository<InStore, String> getDao() {
        return instoreRepository;
    }
    public void save(CheckResult cr, IOutResult or ,List<InStore> instores) {
        List<String> commodityIds = new ArrayList();
        if (instores != null) {
            for(InStore instore: instores){
                for(String id:commodityIds){
                    if(id.equals(instore.getCommodityId())){
                        commodityIds.clear();
                        or.setSuccess(false);
                        or.setMsg("单次入库物品重复，入库失败");
                        return;
                    }
                }
                commodityIds.add(instore.getCommodityId());
            }
        }else{
            cr.setSuccess(false);
            cr.getErrorMsg().append("入库物品为空");
        }



        instoreRepository.save_batch_detached(cr,instores);


        for(InStore instore : instores){

           String commodityId =  instore.getCommodityId();
           InventoryStatistics inventoryStatistics = inventoryStatisticsRepository.findByCommodityId(commodityId);

           if(inventoryStatistics!=null){  //原本有记录，是添加。
               inventoryStatistics.setRest(inventoryStatistics.getRest()+  instore.getInstoreQuantity());
               inventoryStatisticsRepository.save(inventoryStatistics);
           }else{                   //新增物品
               InventoryStatistics inventoryStatistics_new = new InventoryStatistics();
               Commodity commodity = commodityRepository.getOne(commodityId);

               String position = attributeInfoItemRepository.findById(commodity.getPosition()).get().getName();
               inventoryStatistics_new.setCommodityId(instore.getCommodityId());
               inventoryStatistics_new.setStoreId(instore.getStoreId());
               inventoryStatistics_new.setCommodityName(commodity.getName()+"--"+commodity.getUnit()+"--"+position);
               inventoryStatistics_new.setRest(instore.getInstoreQuantity());
               inventoryStatistics_new.setHadUse(0);
               inventoryStatisticsRepository.save(inventoryStatistics_new);
           }

        }
    }


    public List<InStore> queryByKey(String key) {
        key  = key.trim();
        try{
           if(key.equals("")||key==null){
               return  instoreRepository.findAll();
            }else{
                return instoreRepository.findByKey(key);
            }
        }catch(Exception e){
             return instoreRepository.findAll();
        }
    }



}

package com.haiyisec.oa.inventorymanager.domain.service.importservice;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.CommodityFail;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityFailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;

import java.util.List;

@Slf4j
@Service
public class CommodityFailService extends BaseService {
    //@todo 框架的类

    @Autowired
    private CommodityFailRepository commodityFailRepository;

    @Override
    public BaseRepository getDao() {
        return commodityFailRepository;
    }

    public void saveByList(List<CommodityFail> list) {
        commodityFailRepository.save_batch_detached(new CheckResult(), list);
    }

    public void saveOne(CommodityFail commodityFail) {
        commodityFailRepository.save(new CheckResult(), commodityFail);
    }

    public void deleteByImportMonitorId(String importMonitorId) {
        try {
//            commodityFailRepository.deleteByImportMonitorId(importMonitorId);
            commodityFailRepository.deleteAll(importMonitorId);
        }catch (NullPointerException e){
            log.info("可删除数据为空..");
        }

    }

//    public List<CommodityFail> getErrorDatas(String id) {
//        return commodityFailRepository.findByImportMonitorId(id);
//    }
public List<CommodityFail> getErrorDatas(String taskId) {
        return commodityFailRepository.findByTaskId(taskId);
    }
}

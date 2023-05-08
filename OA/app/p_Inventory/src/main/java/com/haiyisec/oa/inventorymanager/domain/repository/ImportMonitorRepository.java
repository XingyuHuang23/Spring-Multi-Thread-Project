package com.haiyisec.oa.inventorymanager.domain.repository;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.service.test.Status;
import org.springframework.data.jpa.repository.Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;

import java.util.List;

public interface ImportMonitorRepository extends BaseRepository<ImportMonitor, String> {

//    List<ImportMonitor> findByAccountId(String accountId);

    ImportMonitor findByUserId(String accountId);

    ImportMonitor findByTaskId(String taskId);

     @Query(value = "update import_monitor set task_status = ?1 where task_id = ?2", nativeQuery = true)
     void setStatus(Status status,String taskId);

    void deleteByUserId(String accountId);

    Long countByTaskIdAndTaskStatus(String taskName,Status status);

    @Query(value = "select * from import_monitor where user_id = ?1 order by bae003 desc limit 1", nativeQuery = true)
    ImportMonitor findLastTask(String userId);

    Long countByTaskIdAndUserId(String taskId,String userId);

    Long countByTaskId(String taskId);

    Long countByTaskStatus(Status taskStatus);

    Long countByModel(String model);

    Long countByModelAndUserIdAndTaskStatus(String model, String userId,Status status);

    List<ImportMonitor> findByTaskStatus(Status work);


    Long countByModelAndTaskStatus(String model, Status check);



    @Query(value = "update import_monitor set task_status = ?1 where task_status = ?2 or task_status = ?3", nativeQuery = true)
    void primaryStatus(Status error,Status work,Status check);
}

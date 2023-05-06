package com.haiyisec.oa.inventorymanager.domain.model.po.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.service.test.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;
import org.zen.frame.base.domain.annotation.query.Zen_Query;
import org.zen.frame.func.threadpool.threadpool.HyThreadPoolExecutor;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Where(clause = "has_del='0'")
@Table(indexes = {
        @Index(name = "UDX_HasDel_TaskId", columnList = "hasDel", unique = true),
        @Index(name = "UDX_HasDel_TaskId", columnList = "taskId", unique = true),
})
//@todo: 名字修改为 importBatch
public class ImportMonitor extends BaseModel {
    //@todo: 继续导入的功能---缓存表---后续的数据处理会比较复杂
    //@todo: 待修改，批次号跟其他字段组成唯一索引
    //@todo: importType 对应不同类型的导入
    private String batch = "ceshi";//导入批次（导入任务类型）标签
    @JsonIgnore
    private String userId;
    private String taskId;
    //@todo: 要修改为枚举型的字典项
    private Status taskStatus;
    private int totalNum;
    private int importFailNum;
    private int successNum;
    private int checkFailNum;
    private String model;
    private HyThreadPoolExecutor importDataPool;
    private HyThreadPoolExecutor checkDataPool;
    @Transient
    private String progressRate = "0";
    @Transient
    private List failDatas  = new CopyOnWriteArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startTime;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime finishTime;

    @Transient
    private AtomicInteger successNumRealTime = new AtomicInteger(0);
    @Transient
    private AtomicInteger failNumRealTime = new AtomicInteger(0);

    @Transient
    private boolean interrupt = false;

}

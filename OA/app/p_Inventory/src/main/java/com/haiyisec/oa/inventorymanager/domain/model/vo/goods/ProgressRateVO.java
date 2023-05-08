package com.haiyisec.oa.inventorymanager.domain.model.vo.goods;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
//@Setter
public class ProgressRateVO {
    //设置内置锁
    private final Object mylock = new Object();

    private int totalNum;
    private AtomicInteger successNum = new AtomicInteger(0);
    private CopyOnWriteArrayList<CommodityExcelVerifyVO> failDatas = new CopyOnWriteArrayList();
    private String startTime;
    private String finishTime;

    public void setTotalNum(int totalNum) {
        synchronized (mylock) {
            this.totalNum = totalNum;
        }
    }

//    public void setSuccessNum(AtomicInteger successNum) {
//        synchronized (mylock){
//            this.successNum = successNum;
//        }
//    }

//    public void setFailDatas(CopyOnWriteArrayList failDatas) {
//        synchronized (mylock){
//            this.failDatas = failDatas;
//        }
//    }

    public void setStartTime(String startTime) {
        synchronized (mylock) {
            this.startTime = startTime;
        }
    }

    public void setFinishTime(String finishTime) {
//        synchronized (mylock){
        this.finishTime = finishTime;
//        }
    }


}

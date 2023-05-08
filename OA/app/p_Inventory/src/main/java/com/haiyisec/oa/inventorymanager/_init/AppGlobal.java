package com.haiyisec.oa.inventorymanager._init;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import java.util.concurrent.ConcurrentHashMap;


public class AppGlobal {
    //public static AtomicInteger successNum = new AtomicInteger(0);
    //以登陆id作为key，ProgressRateVO作为value
    //跳页面先搜ImportMonitor查看任务状态，3个任务状态正常结束finish，中断结束interrupt，异常结束finish with error
    //@todo: uid-map（taskid-vo）  monitor加上业务标志位


    //@todo: 新建数据缓存表，用于宕机后继续导入
//    public static ConcurrentHashMap<String, Map> POOLCACHE=new ConcurrentHashMap<>();


    public static ConcurrentHashMap<String, ImportMonitor> IMPORTRATECACHE=new ConcurrentHashMap<>();

    //定义导入状态
    //@todo 定义为枚举类型的字典  参考yesOrNo字典项
    //@todo 是否要定义准备状态（校验） + preparing
    public static final String WORK = "WORKINNG";//导入中
    public static final String FINISH = "FINISH";//结束
    public static final String INTERRUPT = "INTERRUPT";//用户取消
    public static final String FINISHWITHERROR = "FINISHWITHERROR";//异常结束

    //定义独占状态
//    public static final String
}

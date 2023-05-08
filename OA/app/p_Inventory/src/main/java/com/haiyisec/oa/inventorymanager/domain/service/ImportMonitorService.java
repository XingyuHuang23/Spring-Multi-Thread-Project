package com.haiyisec.oa.inventorymanager.domain.service;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.repository.ImportMonitorRepository;
import com.haiyisec.oa.inventorymanager.domain.service.test.ImportConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.Status;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.frame.func.blocking.Blocking;
import org.zen.frame.func.threadpool.threadpool.HyThreadPoolExecutor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.StrategyType.APP;
import static com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.StrategyType.CUSTOM;
import static com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.StrategyType.USER;

@Service
public class ImportMonitorService  {
    //这里专门使用ImMonitor
    @Autowired
    private ImportMonitorRepository importMonitorRepository;

    @Autowired
    private ImportLimitCheck importLimitCheck;


    private ConcurrentHashMap<String, ImportMonitor> list_importMonitor = new ConcurrentHashMap<>();

//    private ConcurrentHashMap<String, ConcurrentHashMap<String,AtomicInteger>> user_model = new ConcurrentHashMap<>();
//
//    private ConcurrentHashMap<String, AtomicInteger> app_model = new ConcurrentHashMap<>();

//    //@change: 去掉
//    public static final ConcurrentHashMap<String, Blocking> importBowls = new ConcurrentHashMap();

    //@change: check和import的线程池 是否可以直接作为 ImportMonitor 的 游离属性？ 归属到importMonitor
//    public  ConcurrentHashMap<String, HyThreadPoolExecutor> importDataPool = new ConcurrentHashMap<>();
//    public  ConcurrentHashMap<String, HyThreadPoolExecutor> checkDataPool = new ConcurrentHashMap<>();

//    @PostConstruct
//    public void init(){
//        //系统重启之后改变状态
//        //@change: 修改为使用 In 进行查询： findByTaskStatusIn
//        //直接改成所有改变即可，一条update即可。
//      importMonitorRepository.primaryStatus(Status.ERROR,Status.WORK,Status.CHECK);
//
//    }

    public ImportMonitor queryOneByAccountId(String accountId){
        return importMonitorRepository.findByUserId(accountId);
    }


    public ImportMonitor queryOneByTaskId(String taskId){
        return importMonitorRepository.findByTaskId(taskId);
    }

    public void save(ImportMonitor importMonitor){
        importMonitorRepository.save(importMonitor);
    }

    public void delOldImportMonitor(ImportMonitor importMonitor){
        importMonitorRepository.delete(importMonitor);
    }


    public void createMonitor(ImportMonitor importMonitor){

        list_importMonitor.put(importMonitor.getTaskId(), importMonitor);

        save(importMonitor);
    }

    //@change: 修改方法名为 setStatusAndSave
    public void setStatusAndSave(ImportMonitor importMonitor, Status status){

        //ImportMonitor importMonitor = list_importMonitor.get(taskId);
        importMonitor.setTaskStatus(status);

        save(importMonitor);
    }

    public  ImportMonitor getImportMonitor(String taskId) {

        return   list_importMonitor.get(taskId);
    }


    //@change: 方法的参数修改为 IOutResult or,OperationConfig oc,String model,String userId
    //@change: 返回值 修改为 void
//    public IOutResult userWorking(ImportConfig importConfig) {
//        //@change:  or 作为方法的参数传入
//        IOutResult or = new OutResult();
//        Blocking b =importBowls.computeIfAbsent(importConfig.getModel(),(key) -> new Blocking()); //对执行域上锁
//        String str="importing";
//        b.lock(str);
//        try{
//            //@change:  后面的内容修改为：
////            importLimitCheck.check(or,importConfig.getOperationConfig(),model,userId);
//
//
//            if(importConfig.getOperationConfig().getStrategyType() == APP){
//                or = appTypeWorking(importConfig);
//                if(!or.isSuccess()) {
//                    cacheModel(importConfig);
//                }
//                return or;
//            }
//
//            if(importConfig.getOperationConfig().getStrategyType() == USER){
//                or = userTypeWorking(importConfig);
//                if(!or.isSuccess()) {
//                    cacheModel(importConfig);
//                }
//                return  or;
//            }
//
//            if(importConfig.getOperationConfig().getStrategyType() == CUSTOM){
//                or = customTypeWorking(importConfig);
//                if(!or.isSuccess()) cacheModel(importConfig);
//                return  or;
//            }
//
//            return or;
//
//        }catch (Exception e){
//            //@change: 异常需继续抛出 或 在 or中进行体现
//            e.printStackTrace();
//        }finally {
//            b.unlock(str);
//        }
//        return or;
//    }




    public void successNumRealTime(String taskId){

        ImportMonitor importMonitor = list_importMonitor.get(taskId);

        importMonitor.getSuccessNumRealTime().getAndIncrement();
    }

    public void failNumRealTime(String taskId) {

        ImportMonitor importMonitor = list_importMonitor.get(taskId);

        importMonitor.getFailNumRealTime().getAndIncrement();

    }

    public void finished(ImportMonitor importMonitor) {

       // ImportMonitor importMonitor = list_importMonitor.get(taskId);


        //@change: 【已处理】  去掉下方的 app_model,user_model的处理,改为通过
        importLimitCheck.reduceFromLimitMap(importMonitor.getModel(),importMonitor.getUserId());

//        //缓存修改
//        try{
//            if(user_model.get(importMonitor.getUserId()).get(importMonitor.getModel()).get()>=1){
//                user_model.get(importMonitor.getUserId()).get(importMonitor.getModel()).decrementAndGet();
//            }
//        }catch(Exception e){}
//
//        try{
//            if(app_model.get(importMonitor.getModel()).get()>=1){
//                app_model.get(importMonitor.getModel()).decrementAndGet();
//            }
//        }catch(Exception e){}


        importMonitor.setSuccessNum(importMonitor.getSuccessNumRealTime().get());
        importMonitor.setImportFailNum(importMonitor.getFailNumRealTime().get());
        importMonitor.setUserId(importMonitor.getUserId());
        importMonitor.setFinishTime(new DateTime());
        importMonitor.setTaskStatus(Status.FINISH);
        save(importMonitor);

        list_importMonitor.remove(importMonitor.getTaskId());
    }

    public void setStartTime(String taskId) {

        ImportMonitor importMonitor = list_importMonitor.get(taskId);

        importMonitor.setStartTime(new DateTime());
        save(importMonitor);
    }





    public void setImportPool(ImportMonitor importMonitor, HyThreadPoolExecutor datasImportPool) {

        importMonitor.setImportDataPool(datasImportPool);
    }

    public HyThreadPoolExecutor getImportPool(String taskId) {
        ImportMonitor importMonitor = list_importMonitor.get(taskId);
        return importMonitor.getImportDataPool();
    }

    public void interrupt(String taskId) {

        //@change: check和import的线程池 从 ImportMonitor中取
        ImportMonitor importMonitor = list_importMonitor.get(taskId);
        HyThreadPoolExecutor datasCheckPool = importMonitor.getCheckDataPool();

        HyThreadPoolExecutor datasImportPool = importMonitor.getImportDataPool();


        if(datasCheckPool!=null){

            setInterrupted(importMonitor);

            //checkDataPool.remove(taskId); //checkPool不落地，仅在缓存中删除
            datasCheckPool.cancelAllTask();

        }

        if (datasImportPool!= null) {

            setInterrupted(importMonitor);
            datasImportPool.cancelAllTask();
        }

        //@change: 是否有方法可以确认 导入任务是否真正完全停止？
        //@change: 最好能在任务真正完全停止后再从缓存删除该任务，不然不断的 导入-取消 ，会造成大量的中断未停止的导入任务
        //list_importMonitor.remove(taskId);
    }



    public void setInterrupted(ImportMonitor importMonitor) {



        //@change: 【已处理】  去掉下方的 app_model,user_model的处理,改为通过
        //@change: 是否有方法可以确认 导入任务是否真正完全停止？
        //@change: 最好能在任务真正完全停止后再从缓存删除该任务，不然不断的 导入-取消 ，会造成大量的中断未停止的导入任务
        importLimitCheck.reduceFromLimitMap(importMonitor.getModel(),importMonitor.getUserId());

        importMonitor.setInterrupt(true);
        importMonitor.setTaskStatus(Status.INTERRUPT);
        importMonitor.setFinishTime(new DateTime());

        save(importMonitor);

        //@change: 是否有方法可以确认 导入任务是否真正完全停止？
        //@change: 最好能在任务真正完全停止后再从缓存删除该任务，不然不断的 导入-取消 ，会造成大量的中断未停止的导入任务
        //list_importMonitor.remove(taskId);
    }

    public boolean isInterrupted(ImportMonitor importMonitor) {
        //@change: 如果 setInterrupted 的时候不 remove，则下面的判断方式也要修改，需要!=null，且为状态位为中断。

        if(importMonitor.getTaskStatus() == Status.INTERRUPT){
            return true;
        }
        return false;
    }

    public void setCheckPool(ImportMonitor importMonitor, HyThreadPoolExecutor checkPool){

                 importMonitor.setCheckDataPool(checkPool);
    }

    public void removeCheckPool(String taskId) {
        ImportMonitor importMonitor = list_importMonitor.get(taskId);
        importMonitor.setCheckDataPool(null);
    }

    public HyThreadPoolExecutor getCheckPool(String taskId) {
        ImportMonitor importMonitor = list_importMonitor.get(taskId);
        return   importMonitor.getCheckDataPool();
    }

    public ConcurrentHashMap getCacheList() {
        return list_importMonitor;
    }

//    public int getModelTaskNum(String model) {  //还有一个finish时从缓存中删除
//
//        if(app_model.get(model) ==null){
//            return 0;
//        }else {
//            return app_model.get(model).get();
//        }
//
//    }
//
//    public int getModelUserTaskNum(String model,String userId) {
//
//        if(user_model.get(userId)==null){
//            return 0;
//        }else{
//            if( user_model.get(userId).get(model) ==null){
//                return 0;
//            }else{
//                return user_model.get(userId).get(model).get();
//            }
//        }
//
//    }

    public ImportMonitor prepareWork(ImportConfig importConfig) {
        ImportMonitor importMonitor = new ImportMonitor();
        importMonitor.setTaskId(importConfig.getTaskId());
        //录入用户id，按照策略判断

        //@change: userId是否需要？ 是否可以用BAE002取代？
        //@change: 所有的导入都会有导入用户，通过 importConfig.getUserId() 获取

        importMonitor.setUserId(importConfig.getUserId());
        importMonitor.setModel(importConfig.getModel());

        createMonitor(importMonitor);
        return importMonitor;
    }

    public void endCheck(ImportMonitor importMonitor, int successNum, int failNum) {


        importMonitor.setTotalNum(successNum+failNum);

        importMonitor.setCheckFailNum(failNum);

        if (Thread.currentThread().isInterrupted()) {
           setInterrupted(importMonitor);
        }

        save(importMonitor);
    }

    public void clearFailData(ImportMonitor importMonitor) {
        //ImportMonitor importMonitor = list_importMonitor.get(taskId);
        importMonitor.getFailDatas().clear();
    }

    public Collection getFailList(ImportMonitor importMonitor) {
        //ImportMonitor importMonitor = list_importMonitor.get(taskId);
       return importMonitor.getFailDatas();
    }
}

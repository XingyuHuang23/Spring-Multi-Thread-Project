package com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.func.blocking.Blocking;
import org.zen.frame.global.ErrorConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ImportLimitCheck {

    private static final ConcurrentHashMap<String, AtomicInteger> LIMIT_MAP = new ConcurrentHashMap<>();

    @Autowired
    private AppImportStrategy appImportStrategy;
    @Autowired
    private UserImportStrategy userImportStrategy;
    @Autowired
    private Blocking b;

    public boolean check(IOutResult or, OperationConfig oc,String model,String userId) {
        String str="$import:" + model;
        b.lock(str);
        try{
            checkByStrategyType(or,oc,model,userId); //XXX
        }catch (Exception e){
            //@change: 【已处理】  异常需继续抛出 或 在 or中进行体现
            e.printStackTrace();
            or.setSuccess(false);
            or.setErrorCode(ErrorConfig.通用_通用错误_Code);
            or.setMsg(e.getMessage());
        }finally {
            b.unlock(str);
        }
        return or.isSuccess();
    }


    private IOutResult checkByStrategyType(IOutResult or, OperationConfig oc,String model,String userId) {
        switch (oc.getStrategyType()) {
            case APP:
                appImportStrategy.operation(or,(AppImportStrategy.ImportParams)oc.getImportParams());
                break;
            case USER:
                userImportStrategy.operation(or,(UserImportStrategy.ImportParams)oc.getImportParams());
                break;
            case CUSTOM:
                oc.getCustomImportStrategy().operation(or,oc.getImportParams());
                break;
            default:
                break;
        }
        if (or.isSuccess()) {
            addToLimitMap(model,userId);  //对缓存的操作
        }
        return or;
    }

    public void addToLimitMap(String model,String userId){
        AtomicInteger num =LIMIT_MAP.computeIfAbsent(model+":"+userId,(key) -> new AtomicInteger(0));
        num.incrementAndGet();
    }

    public void reduceFromLimitMap(String model,String userId){
        AtomicInteger num =LIMIT_MAP.computeIfAbsent(model+":"+userId,(key) -> new AtomicInteger(0));
        num.decrementAndGet();
    }
    
    public int countByModel(String model){
        AtomicInteger count = new AtomicInteger(0);
        LIMIT_MAP.forEach((k, v) -> {
            if(k.contains(model+":")){ //xxx
                count.set(count.get() + v.get());
            }
        });
        return count.get();
    }

    public int countByModelAndUserId(String model,String userId){
        AtomicInteger count = LIMIT_MAP.get(model+":"+userId);
        return count == null?0:count.get();
    }

}

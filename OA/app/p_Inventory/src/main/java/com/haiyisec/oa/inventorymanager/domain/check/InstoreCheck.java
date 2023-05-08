package com.haiyisec.oa.inventorymanager.domain.check;


import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InStoreRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InventoryStatisticsRepository;
import org.joda.time.DateTime;
import org.zen.frame.api.Domain_DB;
import org.zen.frame.base.domain.annotation.check.Zen_DC_Method;
import org.zen.frame.base.domain.check.obj.CheckMethodCallObject;
import org.zen.frame.base.domain.check.obj.CheckObject;
import org.zen.frame.base.domain.check.service.CheckService;
import org.zen.frame.base.domain.obj.CheckResult;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Des
 * @Date 2021/1/13 11:44
 * @Param 
 * @Return 
 */
public class InstoreCheck extends CheckService<InStore> {


    @Zen_DC_Method(id = "入库时间检查,系统时间比较", msgTemplate = "入库时间大于当前时间，入库失败")
    public static CheckResult instoreTime(CheckMethodCallObject<InStore> cmco) {
         InStore instore = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        ZonedDateTime zonedDateTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        Date today =  Date.from(zonedDateTime.toInstant());

        if (instore != null) {
            InStoreRepository instoreRepository = Domain_DB.getDao(InStore.class, InStoreRepository.class);

            DateTime instoreTime = instore.getInstoreTime();


            if (Arrays.equals(getTimeArray(instoreTime.toDate()), getTimeArray(today))) {    //如果相等表示今天，通过
                return cr;
            }else if(instoreTime.toDate().after(today)){  //表示插入时间比今天大，不能通过
                setFalse_CheckResult(cr, co);
            }
        }else{
            setFalse_CheckResult(cr, co);
        }
        return cr;
    }

    @Zen_DC_Method(id = "入库数量检查", msgTemplate = "物品库存已满！")
    public static CheckResult instoreMax(CheckMethodCallObject<InStore> cmco) {
        InStore instore = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();


        if (instore != null) {

            InventoryStatisticsRepository inventoryStatisticsRepository = Domain_DB.getDao(InventoryStatistics.class, InventoryStatisticsRepository.class);

            InventoryStatistics inventoryStatistics = inventoryStatisticsRepository.findByCommodityId(instore.getCommodityId());  //如果这里空指针异常，则说明不存在

            if(inventoryStatistics.getRest()+instore.getInstoreQuantity()>1000000){
                setFalse_CheckResult(cr, co);
             }

        }else{
            setFalse_CheckResult(cr, co);
        }
        return cr;
    }
    //@todo: xxxxx购买人名单后续补充

    @Zen_DC_Method(id = "购买人检查", msgTemplate = "购买人不在用户列表中，入库失败")
    public static CheckResult purchaserExist(CheckMethodCallObject<InStore> cmco) {
        InStore instore = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (instore != null) {

        }
        return cr;
    }
    @Zen_DC_Method(id = "判断入库时所选物品项是否存在", msgTemplate = "物品不存在，入库失败")
    public static CheckResult commodityIdExist(CheckMethodCallObject<InStore> cmco) {
        InStore instore = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (instore != null) {
            CommodityRepository commodityRepository = Domain_DB.getDao(Commodity.class, CommodityRepository.class);

            try{
                commodityRepository.findById(instore.getCommodityId()).get();  //如果这里空指针异常，则说明不存在
            }catch(Exception e){

                setFalse_CheckResult(cr, co);
            }

        }else{
            setFalse_CheckResult(cr, co);
        }

        return cr;
    }
    @Zen_DC_Method(id = "判断入库时的入库记录id必须为空", msgTemplate = "入库记录id不为空")
    public static CheckResult instoreIdExist(CheckMethodCallObject<InStore> cmco) {
        InStore instore = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (instore != null) {
            InStoreRepository inStoreRepository = Domain_DB.getDao(InStore.class, InStoreRepository.class);


                if(!(instore.getId().equals("")||instore.getId()=="")){  //不为空则报错
                    setFalse_CheckResult(cr, co);
                }

        }else{
            setFalse_CheckResult(cr, co);
        }

        return cr;
    }

//    @Zen_DC_Method(id = "重复入库检查", msgTemplate = "单次入库物品重复，入库失败")
//    public static CheckResult instoreMul(CheckMethodCallObject<InStore> cmco) {
//        InStore instore = cmco.getEntity();
//        CheckResult cr = cmco.getCr();
//        CheckObject co = cmco.getCo();
//
//        if (instore != null) {
//            for(String id:commodityIds){
//                if(id.equals(instore.getCommodityId())){
//                    commodityIds.clear();
//                    setFalse_CheckResult(cr, co);
//                }
//            }
//            commodityIds.add(instore.getCommodityId());
//        }else{
//            setFalse_CheckResult(cr, co);
//        }
//
//        return cr;
//    }


    public static String[]  getTimeArray(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = sdf.format(time);
        return  todayString.split("-");
    }
}

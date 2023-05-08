package com.haiyisec.oa.inventorymanager.domain.check;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InStoreRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InventoryStatisticsRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.OutStoreRepository;
import org.joda.time.DateTime;
import org.zen.frame.api.Domain_DB;
import org.zen.frame.base.domain.annotation.check.Zen_DC_Method;
import org.zen.frame.base.domain.check.obj.CheckMethodCallObject;
import org.zen.frame.base.domain.check.obj.CheckObject;
import org.zen.frame.base.domain.check.service.CheckService;
import org.zen.frame.base.domain.obj.CheckResult;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

public class OutstoreCheck  extends CheckService<OutStore> {


    @Zen_DC_Method(id = "出库时间检查,系统时间比较", msgTemplate = "出库时间大于当前时间，出库失败")
    public static CheckResult outstoreTime(CheckMethodCallObject<OutStore> cmco) {
        OutStore outstore = cmco.getEntity();
        CheckResult cr = cmco.getCr( );
        CheckObject co = cmco.getCo();

        ZonedDateTime zonedDateTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        Date today =  Date.from(zonedDateTime.toInstant());

        if (outstore != null) {

            DateTime outstoreTime = outstore.getOutstoreTime();

            if (Arrays.equals(getTimeArray(outstoreTime.toDate()), getTimeArray(today))) {    //如果相等表示今天，通过
                return cr;
            }else if(outstoreTime.toDate().after(today)){  //表示出库时间比今天大，不能通过
                setFalse_CheckResult(cr, co);
            }
        }else{
            setFalse_CheckResult(cr, co);
        }
        return cr;
    }

    @Zen_DC_Method(id = "出库数量检查", msgTemplate = "出库数量大于库存数量，出库失败")
    public static CheckResult outboundNumber(CheckMethodCallObject<OutStore> cmco) {
        OutStore outstore = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();


        if (outstore != null) {
            InventoryStatisticsRepository InventoryStatisticsRepository = Domain_DB.getDao(InventoryStatistics.class, InventoryStatisticsRepository.class);

            if( Integer.valueOf(InventoryStatisticsRepository.findByCommodityId(outstore.getCommodityId()).getRest())-outstore.getOutstoreQuantity()<0){
                setFalse_CheckResult(cr, co);
            }
          }else{
             setFalse_CheckResult(cr, co);
         }
        return cr;
    }
    @Zen_DC_Method(id = "判断出库时所选物品项是否存在", msgTemplate = "物品不存在，出库失败")
    public static CheckResult commodityIdExist(CheckMethodCallObject<OutStore> cmco) {
        OutStore outstore = cmco.getEntity();
        CheckResult cr = cmco.getCr();
        CheckObject co = cmco.getCo();

        if (outstore != null) {
            CommodityRepository commodityRepository = Domain_DB.getDao(Commodity.class, CommodityRepository.class);
            InventoryStatisticsRepository inventoryStatisticsRepository = Domain_DB.getDao(InventoryStatistics.class, InventoryStatisticsRepository.class);
            if(inventoryStatisticsRepository.findByCommodityId(outstore.getCommodityId())==null){
              setFalse_CheckResult(cr, co);//如果这里空指针异常，则说明不存在
            }
            try{
                commodityRepository.findById(outstore.getCommodityId()).get();  //如果这里空指针异常，则说明不存在

            }catch(Exception e){
                setFalse_CheckResult(cr, co);
            }

        }else{
            setFalse_CheckResult(cr, co);
        }

        return cr;
    }
    public static String[]  getTimeArray(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = sdf.format(time);
        return  todayString.split("-");
    }
}

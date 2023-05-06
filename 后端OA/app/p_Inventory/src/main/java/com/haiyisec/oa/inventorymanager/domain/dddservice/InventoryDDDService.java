package com.haiyisec.oa.inventorymanager.domain.dddservice;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.haiyisec.oa.inventorymanager._init.AppGlobal;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityFailVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.ProgressRateVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.statistic.StatisticVO;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoItemRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InventoryStatisticsRepository;
import com.haiyisec.oa.inventorymanager.domain.service.*;
import com.haiyisec.oa.inventorymanager.domain.service.excelservice.CommodityExcelDataHandler;
import com.haiyisec.oa.inventorymanager.domain.service.excelservice.CommodityExcelDataVerifyHandler;
import lombok.SneakyThrows;
import net.minidev.json.JSONValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.func.excel.ExcelUtil;
import org.zen.frame.global.SortConfig;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.vo.LoginUser_Private;
import org.zen.modules.login.domain.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.stereotype.Service.*;

@Service
public class InventoryDDDService {
    //查询改为queryXXX。。。跟前端方法相对应
    //service不添加@Valid

    @Autowired
    private AttributeInfoService attributeInfoService;
    @Autowired
    private AttributeItemInfoService attributeItemInfoService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private InStoreService inStoreService;
    @Autowired
    private OutStoreService outStoreService;
    @Autowired
    private ImportMonitorService importMonitorService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private InventoryStatisticsRepository inventoryStatisticsRepository;
    @Autowired
    private AttributeInfoItemRepository attributeInfoItemRepositoryService;

    //基础信息--属性
    public void saveAttributeInfo(CheckResult cr, AttributeInfo attributeInfo) {
        attributeInfoService.save(cr, attributeInfo);
    }

    public Page findAllAttributeInfo(Pageable page) {
        return attributeInfoService.queryList(page);
    }
  

    public void delAttributeInfo(CheckResult cr, String attributeInfoId) {
        attributeInfoService.delete(cr, attributeInfoId);
    }

    public Page<AttributeInfo> findSomeAttributeInfo(String key,Pageable page) {
      return attributeInfoService.queryByName(key,page);
    }

    //基础信息--属性选项
    public void saveAttributeInfoItem(CheckResult cr, AttributeInfoItem attributeInfoItem) {
        attributeItemInfoService.save(cr, attributeInfoItem);
    }

    public Page<AttributeInfoItem> queryAllAttributeInfoItem(String attributePOId,Pageable page) {
        return attributeItemInfoService.queryByAttributeInfoId(attributePOId,page);
    }

    public void delAttributeInfoItem(CheckResult cr,String attributeInfoItemId) {
        attributeItemInfoService.delete(cr, attributeInfoItemId);
    }

    /*
     *   物品档案部分
     * */
    public void saveCommodity(CheckResult cr, Commodity commodity) {

        commodityService.save(cr, commodity);
        try{
            InventoryStatistics is = inventoryStatisticsRepository.findByCommodityId(commodity.getId());
            if(is!=null){
                String commodity_name = commodity.getName()+"--"+commodity.getUnit()+"--"+attributeInfoItemRepositoryService.findById(commodity.getPosition()).get().getName();

                is.setCommodityName(commodity_name);
                inventoryStatisticsRepository.save(is);
            }else{
                System.out.println("该物品还未被入库");
            }
        }catch(Exception e){
            System.out.println("该物品还未被入库");
        }

    }

    public Page queryCommodity(Pageable page) {
        return commodityService.queryList(page);
    }

    public void delCommodity(CheckResult cr,String commodityId) {
        commodityService.delete(cr, commodityId);

    }

//    @SneakyThrows
    @SneakyThrows
    public void downloadExcelTemplate(CheckResult cr, HttpServletResponse response) {
        //@todo: 这种下载方式有问题  exception  HttpMediaTypeNotAcceptableException: Could not find acceptable representation
        List<CommodityVO> commodityFailVOList = new ArrayList<>();
//        for (int x = 0; x < 2000; x++) {
//            CommodityVO commodityFailVO = new CommodityFailVO();
//            commodityFailVO.setName("测试物品" + x);
//            commodityFailVO.setCommodityNo("" + x);
//            commodityFailVO.setPosition("位置1");
//            commodityFailVO.setType("类别1");
//            commodityFailVO.setUnit("单位" + x);
//            commodityFailVO.setFailReason("错误了啊...");

//            commodityFailVOList.add(commodityFailVO);
//        }
        ExcelUtil.downExcel(response, "excelTemplate", commodityFailVOList, CommodityVO.class);
    }




    //获取前端进度条情况
//    public String getProgressRateResult(HttpServletRequest request) {
//        LoginUser_Private loginuser = (LoginUser_Private) request.getSession().getAttribute("$loginUser_Private");
//        String loginuserId = loginuser.getId();
//        List<ImportMonitor> oldImportMonitor = importMonitorService.queryOneByAccountId(loginuserId);
//        ProgressRateVO progressRateVO = AppGlobal.IMPORTCACHE.get(loginuserId);
//        DecimalFormat df = new DecimalFormat("0");
//        float progress = ((float) (progressRateVO.getSuccessNum().get() + progressRateVO.getFailDatas().size()) / progressRateVO.getTotalNum()) * 100;
//
//        Map progressRateDates = new HashMap();
//        progressRateDates.put("startTime", progressRateVO.getStartTime());
//        progressRateDates.put("finishTime", progressRateVO.getFinishTime());
//        progressRateDates.put("totalNum", progressRateVO.getTotalNum());
//        progressRateDates.put("successNum", progressRateVO.getSuccessNum());
//        progressRateDates.put("failNum", progressRateVO.getFailDatas().size());
//        progressRateDates.put("status", oldImportMonitor.get(0).getTaskStatus());
//        progressRateDates.put("progressRate", df.format(progress));
//
////        or.addData("progressRateDates", progressRateDates);
//        String progressRateDatesJson = JSONValue.toJSONString(progressRateDates);
//        return progressRateDatesJson;
//    }

//    public void interruptImportExcel() {
//        commodityService.interruptImportExcel();
//    }

    /*
     *  入库部分
     * */
    public void saveInStore(CheckResult cr,IOutResult or, List<InStore> instores) {

        inStoreService.save(cr,or, instores);

    }

    public Page queryInStore(Pageable page) {
        return inStoreService.queryList(page);
    }

    public List<InStore> queryInStoreByKey(String key) {

        return inStoreService.queryByKey(key);
    }

    /*
     *  出库部分
     * */
    public InventoryStatistics queryRest(String id) {
        return outStoreService.rest(id);
    }

    public void saveOutStore( CheckResult cr, OutStore outstore) {
        outStoreService.outstore(cr, outstore);
    }

    public Page queryOutStore(Pageable page) {
        return outStoreService.queryList(page);
    }

    public Commodity getCommodityById(String id) {
        return commodityService.get(id);
    }


    public List<OutStore> queryOutStoreByKey(String key) {
        return outStoreService.queryByKey(key.trim());
    }
    /**
     *
     * 库存管理部分
     * */
    public Page statisticCommodity(Pageable page) {
        return statisticService.queryList(page);
    }
    public List statisticCommodity() {
        return statisticService.queryList();
    }

    public List<InventoryStatistics> queryStatisticByKey(String key) {
        return statisticService.queryByKey(key);
    }




}

package com.haiyisec.modules.inventorymanager.domain.dddservice;

import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.haiyisec.oa.inventorymanager.domain.dddservice.InventoryDDDService;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.repository.*;
import com.haiyisec.utest.BaseTest;
import com.haiyisec.utest.dddutil.DDDCheckTestUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.zen.frame.base.domain.exception.CheckResultException;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.frame.vendor.spring.springmvc.bu.UserUtil;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class InventoryDDDServiceTest extends BaseTest {

    @Autowired
    private InventoryDDDService inventoryDDDService;
    @Autowired
    private AttributeInfoRepository attributeInfoRepository;
    @Autowired
    private AttributeInfoItemRepository attributeInfoItemRepository;
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private InventoryStatisticsRepository inventoryStatisticsRepository;
    @Autowired
    private OutStoreRepository outStoreRepository;
    @Test(testName = "测试保存相同属性名称的校验规则")
    public void check_attributeInfo_uniqueTest() {
        AttributeInfo attributeInfo = new AttributeInfo();
        attributeInfo.setName("test");
        try {
            inventoryDDDService.saveAttributeInfo(new CheckResult(), attributeInfo);
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "该属性名称已存在,不可保存");
        }
       // DDDCheckTestUtil.check_unique(attributeInfo, AttributeInfo.class, "该属性名称已存在,不可保存");
    }


    @Test(testName = "属性名称是否存在子选项检查")
    public void attributeInfo_hasItems_test() {

        try {
            inventoryDDDService.delAttributeInfo(new CheckResult(), dataMap.get("ai"));
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "该属性名称存在子选项,不可删除");
        }
    }

    @Test(testName = "测试检查属性选项名称是否已存在")
    public void check_attributeItemInfo_uniqueTest() {
        AttributeInfoItem attributeItemInfo = new AttributeInfoItem();
        attributeItemInfo.setName("test");
        attributeItemInfo.setAttributeInfoId("4028eaa675da790b0175da7932550000");
        try {
            inventoryDDDService.saveAttributeInfoItem(new CheckResult(), attributeItemInfo);
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "该属性选项已存在,不可保存");
        }
        //DDDCheckTestUtil.check_unique(attributeItemInfo, AttributeInfoItem.class, "该属性选项已存在,不可保存");
    }

    @Test(testName = "测试检查属性选项的attributeInfoId不能为空")
    public void check_attributeItemInfo_attributeInfoIdNotNull() {
        AttributeInfoItem attributeItemInfo = new AttributeInfoItem();
        attributeItemInfo.setName("Test-2");


        DDDCheckTestUtil.check_unique(attributeItemInfo, AttributeInfoItem.class, "[属性选项所属选项id:不能为null]");
    }
        /*
        * 入库部分
        * **/
   public InStore newInStore(){
            InStore instore = new InStore();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                DateTime time = new DateTime(sdf.parse("2020-12-08"));
                instore.setInstoreTime(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            instore.setCommodityId("4028b881763b194501763b45a8630003");
            instore.setInstorePrice(1);
            instore.setInstoreQuantity(100);
            instore.setPurchaser("2");
            instore.setStoreId("1");
            return instore;
        }
//    @Test(testName = "测试单次重复入库")
//    public void checkControllerInStoreMul() throws JsonProcessingException {
//        List<InStore> list = new ArrayList();
//
//        list.add(newInStore());
//        list.add(newInStore());
//        try {
//            inventoryDDDService.saveInStore(new CheckResult(), list);
//        }catch (CheckResultException e){
//            Assert.assertEquals(e.getMessage(), "单次入库物品重复，入库失败");
//        }
//    }
    @Test(testName = "测试入库物品存在")
    public void checkControllerInStoreCommodityExist()  {
        List<InStore> list = new ArrayList();

        InStore instore = new InStore();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            DateTime time = new DateTime(sdf.parse("2020-12-08"));
            instore.setInstoreTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        instore.setCommodityId("ddddddddd");//输入一个不可能存在的id
        instore.setInstorePrice(1);
        instore.setInstoreQuantity(100);
        instore.setPurchaser("2");
        instore.setStoreId("1");
        list.add(instore);
        try {
            inventoryDDDService.saveInStore(new CheckResult(),new OutResult(), list);
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "物品不存在，入库失败");
        }
    }

    @Test(testName = "测试入库时间")
    public void checkControllerInStoreTime() throws JsonProcessingException {
        List<InStore> list = new ArrayList();
        InStore instore = newInStore();
         Date date=new Date();
         Calendar calendar = new GregorianCalendar();
         calendar.setTime(date);
         calendar.add(calendar.DATE,1);//把日期往后增加一天.
         date=calendar.getTime();
         DateTime dateTime = new DateTime(date);
         instore.setInstoreTime(dateTime);
         list.add(instore);
        try {
            inventoryDDDService.saveInStore(new CheckResult(),new OutResult(), list);
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "入库时间大于当前时间，入库失败");
        }
    }
    @BeforeClass
    public void beforeClass(){
        System.out.println("beforeClass");
        UserUtil.setCurThreadUserId("4028b88157d196e70157d19707bf0000");
        prepareTestData();
    }

    /**
     * 出库部分
     * */
    public OutStore newOutStore(String cid,int num){
        OutStore outstore = new OutStore();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            DateTime time = new DateTime(sdf.parse("2020-12-09"));
            outstore.setOutstoreTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outstore.setCommodityId(cid);
        outstore.setDepartment("1");
        outstore.setOutstoreQuantity(num);
        outstore.setApplicant("test");
        outstore.setStoreId("1");
        return outstore;
    }
     Map<String,String> dataMap = new HashMap();

    public void prepareTestData(){
        Map<String,String> map  = new HashMap();
        AttributeInfo attributeInfo = new AttributeInfo();
        attributeInfo.setName("test");
        String ai =  attributeInfoRepository.save(attributeInfo).getId();

        AttributeInfoItem attributeInfoItem = new AttributeInfoItem();
        attributeInfoItem.setName("test");
        attributeInfoItem.setAttributeInfoId(ai);
        String aii =  attributeInfoItemRepository.save(attributeInfoItem).getId();

        Commodity commodity  = new Commodity();
        commodity.setName("test");
        commodity.setStoreId("test");
        commodity.setPosition(aii);
        commodity.setType(aii);
        commodity.setCommodityNo("test");
        commodity.setUnit("test");
        String cid =  commodityRepository.save(commodity).getId();

        //设置在库存中已有入库记录
        InventoryStatistics is = new InventoryStatistics();
        is.setRest(1000); //默认库存1000
        is.setCommodityId(cid);
        is.setHadUse(0);
        is.setCommodityName("test==test==test");
        String isid = inventoryStatisticsRepository.save(is).getId();


        dataMap.put("ai",ai);
        dataMap.put("aii",aii);
        dataMap.put("cid",cid);
        dataMap.put("isid",isid);
    }

    @Test(testName = "测试出库数量")
    public void checkControllerOutStoreNum() {

        OutStore outstore = newOutStore((String)dataMap.get("cid"),10000); //取10000个

        try {
             inventoryDDDService.saveOutStore(new CheckResult(), outstore);
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "出库数量大于库存数量，出库失败");
        }
    }

    @Test(testName = "测试出库时间")
    public void checkControllerOutStoreTime() {

        OutStore outstore = newOutStore(dataMap.get("cid"),100);
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.
        date=calendar.getTime();
        DateTime dateTime = new DateTime(date);
        outstore.setOutstoreTime(dateTime);

        try {
            inventoryDDDService.saveOutStore(new CheckResult(), outstore);
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "出库时间大于当前时间，出库失败");
        }
    }


    @Test(testName = "测试出库物品存在")
    public void checkControllerOutStoreCommodityExist()  {

        OutStore outstore = new OutStore();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            DateTime time = new DateTime(sdf.parse("2020-12-08"));
            outstore.setOutstoreTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outstore.setCommodityId("ddddddddd");//输入一个不可能存在的id
        outstore.setDepartment("1");
        outstore.setOutstoreQuantity(100);
        outstore.setApplicant("2");
        outstore.setStoreId("1");

        try {
            inventoryDDDService.saveOutStore(new CheckResult(), outstore);
        }catch (CheckResultException e){
            Assert.assertEquals(e.getMessage(), "物品不存在，出库失败");
        }
    }
    public void destroyTestData(){
        attributeInfoRepository.deleteById(dataMap.get("ai"));
        attributeInfoItemRepository.deleteById(dataMap.get("aii"));
        commodityRepository.deleteById(dataMap.get("cid"));
        inventoryStatisticsRepository.deleteById(dataMap.get("isid"));
    }

    @AfterClass
    public void afterClass(){
        System.out.println("afterClass");
        destroyTestData();
    }
}

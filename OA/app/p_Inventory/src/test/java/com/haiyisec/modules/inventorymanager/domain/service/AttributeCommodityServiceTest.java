package com.haiyisec.modules.inventorymanager.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.repository.InStoreRepository;
import com.haiyisec.oa.inventorymanager.domain.service.AttributeItemInfoService;
import com.haiyisec.utest.BaseTest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.zen.frame.vendor.spring.springmvc.bu.UserUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttributeCommodityServiceTest extends BaseTest {
    /*
    * 探索是否有测试模板...
    * 单元测试在开发过程中所占用是时间比例
    * 单元测试对项目的进度影响
    * */
    @Autowired
    private AttributeItemInfoService attributeItemInfoService;
    @Autowired
    private InStoreRepository instoreRepository;
    @Test
    public void test0(){
        System.out.println("unit test success....");
    }


    @Test(testName = "入库测试数据初始化")
    public void saveInStoreTest() throws JsonProcessingException {

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

        instoreRepository.save(instore);
//        Assert.assertTrue(!=null);
    }
}

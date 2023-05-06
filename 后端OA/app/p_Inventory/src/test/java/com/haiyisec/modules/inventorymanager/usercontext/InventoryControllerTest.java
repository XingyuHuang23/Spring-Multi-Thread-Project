package com.haiyisec.modules.inventorymanager.usercontext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InStoreRepository;
import com.haiyisec.utest.BaseTest;
import com.haiyisec.utest.util.FrameApiTestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.frame.func.databus.Databus;
import org.zen.frame.func.databus.DatabusPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryControllerTest extends BaseTest {

    private Databus dbs = DatabusPool.get();

    @Autowired
    private AttributeInfoRepository attributeInfoRepository;

    @Autowired
    private CommodityRepository commodityRepository;

    @Autowired
    private InStoreRepository instoreRepository;
    //可统一封装登陆测试用例，缓存token/cookie为一个dbs
    @Test
    public void loginTest() throws JsonProcessingException {
        String password = FrameApiTestUtil.dealPassword("admin", "level2");
        ResponseEntity<String> response = FrameApiTestUtil.login_Json("/Login/login", "admin", password);
        dbs.set("loginCookie", response.getHeaders().get("Set-Cookie").get(0));
    }

    //controller测试入口进入的Test，作为测试数据初始化的作用
    @Test(dependsOnMethods = "loginTest", testName = "AttributeInfo测试数据初始化")
    public void saveAttributeInfoTest() throws JsonProcessingException {
        Map map = new HashMap();
        map.put("name", "Test-1");

        OutResult outResult = FrameApiTestUtil.post("/API/Inventory/systemSet/saveAttributeInfo", map, (String) dbs.get("loginCookie"));
        Assert.assertTrue(outResult.isSuccess());
    }

    @Test(dependsOnMethods = "loginTest", testName = "测试保存字段长度不超过10")
    public void checkControllerValidated() throws JsonProcessingException {

        Map map = new HashMap();
        map.put("name", "Test-1Test-1");

        OutResult outResult = FrameApiTestUtil.post("/API/Inventory/systemSet/saveAttributeInfo", map, (String) dbs.get("loginCookie"));
        Assert.assertFalse(outResult.isSuccess());
        Assert.assertEquals(outResult.getMsg(), "[属性名称:属性名称长度超长]", "错误提示跟期望不符合。。");
    }

    @Test(dependsOnMethods = "loginTest", testName = "DicInfo测试数据初始化", priority = 1)
    public void saveDicInfo() throws JsonProcessingException {

        AttributeInfo lists = attributeInfoRepository.findByName("Test-1");

        Map map = new HashMap();
        map.put("attributeInfoId", lists.getId());

        for (int x = 1; x < 3; x++) {
            map.put("name", "Test-1-item-" + x);
            OutResult outResult = FrameApiTestUtil.post("/API/Inventory/systemSet/saveDicInfo", map, (String) dbs.get("loginCookie"));
            Assert.assertTrue(outResult.isSuccess());
        }

    }

    @Test(dependsOnMethods = "loginTest", testName = "测试属性选项保存字段长度不超过50")
    public void checkControllerValidated2() throws JsonProcessingException {

        AttributeInfo lists = attributeInfoRepository.findByName("Test-1");

        Map map = new HashMap();
        map.put("attributeInfoId", lists.getId());

        map.put("name", "Test-1-item-1Test-1-item-1Test-1-item-1Test-1-item-1Test-1-item-1Test-1-item-1");
        OutResult outResult = FrameApiTestUtil.post("/API/Inventory/systemSet/saveDicInfo", map, (String) dbs.get("loginCookie"));
        Assert.assertFalse(outResult.isSuccess());
        Assert.assertEquals(outResult.getMsg(), "[属性选项名称:属性选项名称长度超长,保存错误]", "错误提示跟期望不符合。。");
    }





   /**
    * 入库部分
    *
   */
   @Test(dependsOnMethods = "loginTest", testName = "物品测试数据初始化")
   public void saveCommodityTest() throws JsonProcessingException {
       Commodity commodity = new Commodity();

   }
   public Map newInStoreMap(String purchaser){
       Map map = new HashMap();
       map.put("instoreTime","2020-12-08");
       map.put("commodityId","4028b881763b194501763b45a8630003");
       map.put("instorePrice","10");
       map.put("inventoryId","1");
       map.put("purchaser", purchaser);
       return map;
   }
    @Test(dependsOnMethods = "loginTest", testName = "测试入库申请人保存字段长度不超过32")
    public void checkControllerInStorePurchaser() throws JsonProcessingException {
        List list = new ArrayList();

        list.add(newInStoreMap("Test-1-item-1Test-1-item-1Test-1-item-1Test-1-item-1Test-1-item-1Test-1-item-1"));
        OutResult outResult = FrameApiTestUtil.post("/API/Inventory/inventoryManage/instore", list, (String) dbs.get("loginCookie"));
        Assert.assertFalse(outResult.isSuccess());
        Assert.assertEquals(outResult.getMsg(), "[saveInStore方法的参数instores[0]方法的参数purchaser:购买人id长度有误]", "错误提示跟期望不符合。。");
        System.out.println(outResult.getMsg());
    }

    @Test(dependsOnMethods = "loginTest", testName = "测试入库购买人不为空")
    public void checkControllerInStorePurchaserNull() throws JsonProcessingException {
        List list = new ArrayList();

        list.add(newInStoreMap("")); //设置购买人为空
        OutResult outResult = FrameApiTestUtil.post("/API/Inventory/inventoryManage/instore", list, (String) dbs.get("loginCookie"));
        Assert.assertFalse(outResult.isSuccess());
        Assert.assertEquals(outResult.getMsg(), "[saveInStore方法的参数instores[0]方法的参数purchaser:购买人id长度有误]", "错误提示跟期望不符合。。");
        System.out.println(outResult.getMsg());
    }
    @Test(dependsOnMethods = "loginTest", testName = "测试入库物品不为空")
    public void checkControllerInStoreCommodityNull() throws JsonProcessingException {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("instoreTime","2020-12-08");
        map.put("instorePrice","10");
        map.put("inventoryId","1");
        map.put("purchaser", "1");
        list.add(map); //设置入库物品为空
        OutResult outResult = FrameApiTestUtil.post("/API/Inventory/inventoryManage/instore", list, (String) dbs.get("loginCookie"));
        Assert.assertFalse(outResult.isSuccess());
        Assert.assertEquals(outResult.getMsg(), "[saveInStore方法的参数instores[0]方法的参数commodityId:不能为null]", "错误提示跟期望不符合。。");
        System.out.println(outResult.getMsg());
    }
}

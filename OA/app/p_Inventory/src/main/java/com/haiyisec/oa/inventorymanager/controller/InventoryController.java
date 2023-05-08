package com.haiyisec.oa.inventorymanager.controller;

import app.frame.vendor.spring.springmvc.common.usercontext.BaseController;
import app.frame.vendor.spring.springmvc.common.usercontext.IControllRunner;
import com.haiyisec.oa.inventorymanager.domain.dddservice.InventoryDDDService;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InStore;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.InventoryStatistics;
import com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage.OutStore;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.CommodityFailService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.ImportService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataHandler2;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataVerifyHandler2;
import com.haiyisec.oa.inventorymanager.domain.service.test.CommodityAdaption;
import com.haiyisec.oa.inventorymanager.domain.service.test.HyImport;
import com.haiyisec.oa.inventorymanager.domain.service.test.ImportConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.StrategyType;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.AppImportStrategy;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.OperationConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.UserImportStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zen.frame.base.domain.context.UserContext;
import org.zen.frame.base.domain.exception.CheckResultException;
import org.zen.frame.base.domain.obj.*;
import org.zen.frame.func.excel.ExcelUtil;
import org.zen.frame.vendor.spring.springmvc.service.CurUserService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.zen.frame.global.SortConfig.BAE003_DESC_AND_ID_DESC;
import static org.zen.frame.vendor.hibernate.util.QueryUtil.searchPO;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/API/Inventory/")//名字规范
public class InventoryController extends BaseController {

    /*
     * 校验：：
     * 1.api使用权限校验， 权限访问接口
     * 2.数据安全， control进入的数据校验@Validated  controller数据基础校验
     *   领域层：：
     *       3.实体保存前的基础校验，长度/唯一性
     *       4.数据操作权限校验（实体增删改的权限），物品保存权限，具有api访问权限，增删改的时候判断：查询库房管理员-->继续保存
     *       5.业务规则的校验
     *
     * 编写单元测试：：编写五种校验是否符合预期
     * 记录每个模块的详细开发时间
     * */

    /*
     * 初期阶段
     * 最后的PO设计时间
     *
     * 实体开发时间/单元测试开始时间
     *
     * 设计文档/边开发边写设计文档
     * */

    @Override
    public UserContext createUserContext() {
        /*
        前端很灵活的时候， dddservice的检查
        电话作为登陆， loginName， dddservice的检查返回的报错信息（校验loginName不能重复）
        前端提示的错误信息loginName不能重复不友好，通过control（用户上下文）进行转换
        * */
        /**
         * @todo: 后续提供业务场景
         */
        return new UserContext().setName("Inventory");
    }

    //添加上层聚合目录
    @Autowired
    private InventoryDDDService inventoryDDDService;
    @Autowired
    private ImportService importService;
    @Autowired
    private ImportMonitorService importMonitorService;

    @Autowired
    private HyImport hyImport;

    @Autowired
    private CommodityAdaption ca;

    @Autowired
    private CommodityFailService commodityFailService;

    @Autowired
    private AppImportStrategy appImportStrategy;
    @Autowired
    private CurUserService curUserService;

    @Autowired
    private UserImportStrategy userImportStrategy;
    @RequestMapping(value = "/systemSet/saveAttributeInfo", method = POST)
    public IBaseResult saveAttributeInfo(@RequestBody @Valid AttributeInfo attributeInfo) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {
                inventoryDDDService.saveAttributeInfo(cr, attributeInfo);
            }
        });
    }

    //查询改为queryXXX。。。跟前端方法相对应
    @RequestMapping(value = "/systemSet/findAllAttributeInfo", method = POST)
    public IBaseResult queryAllAttributeInfo(@RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                Pageable page = getPageableFromRequest(map, "glt_attriGlt", BAE003_DESC_AND_ID_DESC);
                Page<AttributeInfo> page_result = inventoryDDDService.findAllAttributeInfo(page);
                or.addGlt("glt_attriGlt", page_result);
            }
        });
    }

    //todo:条件查询 只按照name查询
    @RequestMapping(value = "/systemSet/findSomeAttributeInfo/{key}", method = POST)
    public IBaseResult queryAttributeInfo(@PathVariable @NotNull String key, @RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                Pageable page = getPageableFromRequest(map, "glt_attriGlt", BAE003_DESC_AND_ID_DESC);
                Page page_result;

                if (key.equals("null") || key.equals("undefined") || key.equals("") || key == null) {
                    page_result = inventoryDDDService.findAllAttributeInfo(page);
                } else {
                    AttributeInfo searchAI = new AttributeInfo();
                    searchAI.setName(key);
                    page_result = searchPO(searchAI, AttributeInfo.class, page);
                }


                or.addGlt("glt_attriGlt", page_result);

            }
        });
    }

    @RequestMapping(value = "/systemSet/delAttributeInfo/{id}", method = POST)
    public IBaseResult delAttributeInfo(@PathVariable String id) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                inventoryDDDService.delAttributeInfo(cr, id);
            }
        });
    }


    @RequestMapping(value = "/systemSet/saveDicInfo", method = POST)
    public IBaseResult saveAttributeInfoItem(@RequestBody @Valid AttributeInfoItem attributeInfoItem) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {

                inventoryDDDService.saveAttributeInfoItem(cr, attributeInfoItem);
                Map map = new HashMap<>();
                Map map2 = new HashMap<>();
                map2.put("pageSize", "10");
                map2.put("curPage", "1");
                map2.put("count", "35");
                map.put("glt_itemGlt_$Property", map2);

                Pageable page = getPageableFromRequest(map, "glt_itemGlt", BAE003_DESC_AND_ID_DESC);
                Page<AttributeInfoItem> page_result = inventoryDDDService.queryAllAttributeInfoItem(attributeInfoItem.getAttributeInfoId(), page);
                or.addGlt("glt_itemGlt", page_result);
            }
        });
    }

    @RequestMapping(value = "/systemSet/findAllDicInfo/{id}", method = POST)
    public IBaseResult queryAllDicInfo(@PathVariable String id, @RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                Pageable page = getPageableFromRequest(map, "glt_itemGlt", BAE003_DESC_AND_ID_DESC);
                Page<AttributeInfoItem> page_result = inventoryDDDService.queryAllAttributeInfoItem(id, page);
                or.addGlt("glt_itemGlt", page_result);
            }
        });
    }

    @RequestMapping(value = "/systemSet/delDicInfo/{id}", method = POST)
    public IBaseResult delAttributeInfoItem(@PathVariable String id) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                String ids[] = id.split("&");
                String attributeInfoId = ids[1];
                inventoryDDDService.delAttributeInfoItem(cr, ids[0]);
                Map map = new HashMap<>();
                Map map2 = new HashMap<>();
                map2.put("pageSize", "10");
                map2.put("curPage", "1");
                map2.put("count", "35");
                map.put("glt_itemGlt_$Property", map2);

                Pageable page = getPageableFromRequest(map, "glt_itemGlt", BAE003_DESC_AND_ID_DESC);
                Page<AttributeInfoItem> page_result = inventoryDDDService.queryAllAttributeInfoItem(attributeInfoId, page);
                or.addGlt("glt_itemGlt", page_result);
            }
        });
    }

    /*
     *
     * 以下为物品档案模块
     *
     * */
    @RequestMapping(value = "/systemSet/saveCommodity", method = POST)
    public IBaseResult saveCommodity(@RequestBody @Valid Commodity commodity) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {
                inventoryDDDService.saveCommodity(cr, commodity);
            }
        });
    }


    @RequestMapping(value = "/systemSet/findAllCommodity", method = POST)
    public IBaseResult queryAllCommodity(@RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {
                Pageable page = getPageableFromRequest(map, "glt_commodity", BAE003_DESC_AND_ID_DESC);

                Page<Commodity> page_result = inventoryDDDService.queryCommodity(page);
                or.addGlt("glt_commodity", page_result);

            }
        });
    }

    @RequestMapping(value = "/systemSet/delCommodity/{id}", method = POST)
    public IBaseResult delCommodity(@PathVariable String id) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                inventoryDDDService.delCommodity(cr, id);
            }
        });
    }

    //todo:物品名称，物品类别 全模糊查询
    @RequestMapping(value = "/systemSet/findCommodity/{key}", method = POST)
    public IBaseResult findCommodity(@PathVariable String key, @RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {

                Pageable page = getPageableFromRequest(map, "glt_commodity", BAE003_DESC_AND_ID_DESC);
                Page page_result;

                if (key.equals("null") || key.equals("undefined") || key.equals("") || key == null) {
                    page_result = inventoryDDDService.queryCommodity(page);
                } else {
                    Commodity search = new Commodity();
                    search.setSearch(key);
                    Map<String, DomainObject.QueryObj> customQueryMap = new HashMap<String, DomainObject.QueryObj>();
                    //动态增加某个字段的查询条件
                    customQueryMap.put("search", new DomainObject.QueryObj()
                            .setWhere(" (a.name like CONCAT('%', #value, '%')" +
                                    "OR " +
                                    "EXISTS (SELECT c.id FROM attribute_info_item c WHERE c.has_del = '0' AND c.attribute_info_id = '4028eab676265c17017626a2b0a20000' AND c.id = a.type AND c.NAME LIKE CONCAT('%', #value, '%')))")
                    );
                    page_result = searchPO(search, Commodity.class, page, customQueryMap);
                }
                or.addGlt("glt_commodity", page_result);
            }
        });
    }



    /*
     * 物品档案批量导入
     * */
    @RequestMapping(value = "/systemSet/importExcelDatas", method = POST)
    public IBaseResult importExcelDatas(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws Exception {
//                inventoryDDDService.importExcelCommodity(request, cr, file);
                String[] excelTitles = {"物品名称","物品类别","物品单位","所属位置","位置编号"};

                //@todo：还是要变为string[]
//                String importMonitorId = importservice.importDatas(cr, file, excelTitles);
//
                CommodityExcelDataHandler2 d = new CommodityExcelDataHandler2();
                d.setNeedHandlerFields(new String[]{"物品类别", "所属位置"});

                CommodityExcelDataVerifyHandler2 v = new CommodityExcelDataVerifyHandler2();


//                SysImportStrategy.ImportParams ip = new SysImportStrategy.ImportParams().setMaxTaskNum(2);//设置业务类别，设置阈值的位置。
//
//                OperationConfig<SysImportStrategy.ImportParams> oc = new OperationConfig<SysImportStrategy.ImportParams>()
//                        .setImportParams(ip).setStrategy(sysImportStrategy);

//                UserImportStrategy.ImportParams ip = new UserImportStrategy.ImportParams().setMaxUserTaskNum(2);//设置业务类别，设置阈值的位置。
//
//                OperationConfig<UserImportStrategy.ImportParams> oc = new OperationConfig<UserImportStrategy.ImportParams>()
//                        .setImportParams(ip).setCustomImportStrategy(userImportStrategy);

                OperationConfig<Integer> oc1 = new OperationConfig<Integer>()
                        .setImportParams(3).setStrategyType(StrategyType.APP);  //app


                UserImportStrategy.ImportParams ip1 = new UserImportStrategy.ImportParams().setMaxUserTaskNum(2).setUserId(curUserService.getLoginUserId());//设置业务类别，设置阈值的位置。

                OperationConfig<UserImportStrategy.ImportParams> oc2 = new OperationConfig<UserImportStrategy.ImportParams>()
                        .setImportParams(ip1).setStrategyType(StrategyType.USER);  //user
//
//
                UserImportStrategy.ImportParams ip2 = new UserImportStrategy.ImportParams().setMaxUserTaskNum(2).setUserId(curUserService.getLoginUserId());//设置业务类别，设置阈值的位置。

                OperationConfig<UserImportStrategy.ImportParams> oc3 = new OperationConfig<UserImportStrategy.ImportParams>()
                        .setImportParams(ip2).setCustomImportStrategy(userImportStrategy).setStrategyType(StrategyType.CUSTOM); //custom，有custom才获取customStrategy


                ImportConfig importConfig = new ImportConfig("物品导入",UUID.randomUUID().toString(), file, excelTitles, CommodityExcelVerifyVO.class, ca);

                importConfig.setHyExcelDataHandler(d);
                importConfig.setHyExcelDataVerifyHandler(v);
                importConfig.setOperationConfig(oc3);

                //String.valueOf(UUID.randomUUID())
                IOutResult or2 = hyImport.importData(cr,or,importConfig);

                String taskId = (String) or2.getData("taskId");
                or.addData("progressRateKey", taskId);
                or.addData("status",or2.getMsg());

            }
        });
    }

    //获取进度数据
//    @RequestMapping(value = "/systemSet/importExcelDatas/progressRate/{id}", produces = "text/event-stream;charset=UTF-8")
//    public String progressRate(HttpServletRequest request, @PathVariable String id) throws Exception {
    //  return importservice.getProgressRateResult(id);
//    }
    //获取最后一次导入任务


    //获取进度数据Test
    @RequestMapping(value = "/systemSet/importExcelDatas/progressRate/{taskId}", produces = "text/event-stream;charset=UTF-8")
    public String progressRate(@PathVariable String taskId) throws Exception {
        return hyImport.getProgressRateResult(taskId);
    }


    @RequestMapping(value = "/systemSet/importExcelDatas/cancelImportTask/{taskId}", method = POST)
    public IBaseResult cancleImportTask(HttpServletRequest request, @PathVariable String taskId) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                hyImport.cancelTask(taskId);
            }
        });
    }

    /*
     * 下载excel模板
     * */
    @RequestMapping(value = "/systemSet/downloadExcelTemplate", method = RequestMethod.GET)
    public IBaseResult downloadExcelTemplate(HttpServletResponse response) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                inventoryDDDService.downloadExcelTemplate(cr, response);
            }
        });
    }

//    @RequestMapping(value = "/systemSet/downloadErrorDatas", method = RequestMethod.GET)
//    public IBaseResult downloadErrorDatas(HttpServletResponse response) throws Exception {
//        return runController(new IControllRunner() {
//            public void run(IOutResult or, CheckResult cr) {
//                importservice.downloadErrorDatas(response);
//            }
//        });
//    }
//@RequestMapping(value = "/systemSet/downloadErrorDatas/{taskId}", method = RequestMethod.GET)
//public IBaseResult downloadErrorDatas(HttpServletResponse response,@PathVariable String taskId) throws Exception {
//    return runController(new IControllRunner() {
//        public void run(IOutResult or, CheckResult cr) {
//
//            testimportService.downloadErrorDatas(response,taskId);
//        }
//    });
//}

    /**
     * 以下为入库信息模块
     */
    @RequestMapping(value = "/inventoryManage/instore", method = POST)
    public IBaseResult saveInStore(@RequestBody @Valid List<InStore> instores) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {

                inventoryDDDService.saveInStore(cr, or, instores);

            }
        });
    }

    @RequestMapping(value = "/inventoryManage/instore_time", method = POST)
    public IBaseResult instore_time() throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                InStore instore = new InStore();

                instore.setInstoreTime(DateTime.now());
                or.addGt("gt_新增入库信息", instore);

            }
        });
    }


    @RequestMapping(value = "/inventoryManage/findAllInStore", method = POST)
    public IBaseResult findAllInStore(@RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {
                Pageable page = getPageableFromRequest(map, "glt_instoreGlt", BAE003_DESC_AND_ID_DESC);

                Page<InStore> page_result = inventoryDDDService.queryInStore(page);
                or.addGlt("glt_instoreGlt", page_result);

            }
        });
    }

    //todo:可根据物品名称、购买人模糊查询，英文大小写不敏感，查询输入的购买时间允许开始时间和结束时间是同一天。
    @RequestMapping(value = "/inventoryManage/queryInstoreByKey/{key}", method = POST)
    public IBaseResult queryInStoreByKey(@PathVariable String key, @RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {
                Pageable page = getPageableFromRequest(map, "glt_instoreGlt", BAE003_DESC_AND_ID_DESC);
                Page page_result;

                if (key.equals("null") || key.equals("undefined") || key.equals("") || key == null) {
                    page_result = inventoryDDDService.queryInStore(page);
                } else {
                    InStore search = new InStore();
                    search.setSearch(key);
                    Map<String, DomainObject.QueryObj> customQueryMap = new HashMap<String, DomainObject.QueryObj>();
                    //动态增加某个字段的查询条件
                    customQueryMap.put("search", new DomainObject.QueryObj()
                            .setWhere(" (exists( SELECT b.id FROM commodity b WHERE b.has_del = '0' AND b.id = a.commodity_id AND b.NAME LIKE CONCAT('%', #value, '%')) " +
                                    "OR " +
                                    "EXISTS (SELECT c.id FROM attribute_info_item c WHERE c.has_del = '0' AND c.attribute_info_id = '4028b8817693ecfb017693f13d1a0000' AND c.id = a.purchaser AND c.NAME LIKE CONCAT('%', #value, '%')))")
                    );
                    page_result = searchPO(search, InStore.class, page, customQueryMap);
                }
                or.addGlt("glt_instoreGlt", page_result);
            }
        });
    }

    /**
     * 以下为出库信息模块
     */
    @RequestMapping(value = "/inventoryManage/getRest/{id}", method = POST)
    public IBaseResult getRest(@PathVariable String id) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {

                InventoryStatistics Data = inventoryDDDService.queryRest(id);
                or.addGt("gt_出库申请表", Data); //只查询一个字段，不需要分页
            }
        });
    }

    @RequestMapping(value = "/inventoryManage/outstore", method = POST)
    public IBaseResult saveInStore(@RequestBody @Valid OutStore outstore) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {

                inventoryDDDService.saveOutStore(cr, outstore);

            }
        });
    }


    @RequestMapping(value = "/inventoryManage/outstore_time", method = POST)
    public IBaseResult outstore_time() throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {
                OutStore outstore = new OutStore();

                outstore.setOutstoreTime(DateTime.now());
                or.addGt("gt_出库申请表", outstore);

            }
        });
    }

    @RequestMapping(value = "/inventoryManage/findAllOutStore", method = POST)
    public IBaseResult findAllOutStore(@RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {

                Pageable page = getPageableFromRequest(map, "glt_outstoreGlt", BAE003_DESC_AND_ID_DESC);
                Page<OutStore> page_result = inventoryDDDService.queryOutStore(page);
                or.addGlt("glt_outstoreGlt", page_result);

            }
        });
    }


    @RequestMapping(value = "/inventoryManage/getCommodity/{id}", method = POST)
    public IBaseResult getCommodity(@PathVariable String id) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) {

                Commodity Data = inventoryDDDService.getCommodityById(id);
                or.addGt("gt_出库申请信息", Data);
            }
        });
    }


    //todo:可根据物品名称、购买人模糊查询，英文大小写不敏感，查询输入的购买时间允许开始时间和结束时间是同一天。
    @RequestMapping(value = "/inventoryManage/queryOutstoreByKey/{key}", method = POST)
    public IBaseResult queryOutStoreByKey(@PathVariable String key, @RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {

                Pageable page = getPageableFromRequest(map, "glt_outstoreGlt", BAE003_DESC_AND_ID_DESC);
                Page page_result;

                if (key.equals("null") || key.equals("undefined") || key.equals("") || key == null) {
                    page_result = inventoryDDDService.queryOutStore(page);
                } else {
                    OutStore search = new OutStore();
                    search.setSearch(key);
                    Map<String, DomainObject.QueryObj> customQueryMap = new HashMap<String, DomainObject.QueryObj>();
                    //动态增加某个字段的查询条件
                    customQueryMap.put("search", new DomainObject.QueryObj()
                            .setWhere(" (exists( SELECT b.id FROM commodity b WHERE b.has_del = '0' AND b.id = a.commodity_id AND b.NAME LIKE CONCAT('%', #value, '%')) " +
                                    "OR " +
                                    "EXISTS (SELECT c.id FROM attribute_info_item c WHERE c.has_del = '0' AND c.attribute_info_id = '4028b8817693ecfb017693f13d1a0000' AND c.id = a.applicant AND c.NAME LIKE CONCAT('%', #value, '%')))")
                    );
                    page_result = searchPO(search, OutStore.class, page, customQueryMap);
                }

                or.addGlt("glt_outstoreGlt", page_result);

            }
        });
    }

    /*
     * 库存统计部分
     * */
    @RequestMapping(value = "/inventoryManage/statistics", method = POST)
    public IBaseResult statisticCommodity(@RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {

                Pageable page = getPageableFromRequest(map, "glt_statisticGlt", BAE003_DESC_AND_ID_DESC);
                Page<InventoryStatistics> page_result = inventoryDDDService.statisticCommodity(page);
                or.addGlt("glt_statisticGlt", page_result);

            }
        });
    }


    @RequestMapping(value = "/inventoryManage/queryStatisticByKey/{name}", method = POST)
    public IBaseResult queryStatisticByKey(@PathVariable String name, @RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {
                Page page_result;
                Pageable page = getPageableFromRequest(map, "glt_statisticGlt", BAE003_DESC_AND_ID_DESC);
                if (name.equals("undefined") || name.equals("") || name == null || name.equals("null")) {
                    page_result = inventoryDDDService.statisticCommodity(page);
                } else {
                    InventoryStatistics searchIS = new InventoryStatistics();
                    searchIS.setCommodityName(name);
                    page_result = searchPO(searchIS, InventoryStatistics.class, page);
                }
                or.addGlt("glt_statisticGlt", page_result);
            }
        });
    }

    //todo：物品名称查询
    @RequestMapping(value = "/inventoryManage/downloadStatisticsExcel/{key}", method = RequestMethod.GET)
    public IBaseResult downloadExcel(HttpServletResponse response, @PathVariable String key) throws Exception {
        return runController(new IControllRunner() {
            @SneakyThrows
            public void run(IOutResult or, CheckResult cr) throws CheckResultException {

                List<InventoryStatistics> listOfDatas;

                if (key.equals("null") || key.equals("undefined") || key.equals("") || key == null) {
                    listOfDatas = inventoryDDDService.statisticCommodity();
                    ExcelUtil.downExcel(response, "Statistics", listOfDatas, InventoryStatistics.class);
                } else {

                    listOfDatas = inventoryDDDService.queryStatisticByKey(key);
                    ExcelUtil.downExcel(response, "Statistics", listOfDatas, InventoryStatistics.class);
                }

            }
        });
    }
}

package com.haiyisec.modules.inventorymanager.domain.service.importservice;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataHandler2;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataVerifyHandler2;
import com.haiyisec.oa.inventorymanager.domain.service.test.*;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.OperationConfig;
import com.haiyisec.utest.BaseTest_Web;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.frame.func.threadpool.threadpool.HyThreadPoolExecutor;
import org.zen.modules.sysmngr.domain.dao.sysuser.SysUserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class ImportTest extends BaseTest_Web {
    @Autowired
    private CommodityAdaption ca;
    @Autowired
    private HyImport hyImport;
    @Autowired
    private ImportMonitorService importMonitorService;
    //@todo:
    @Autowired
    private SysUserRepository sysUserRepository;

    //设置表头
    String[] excelTitles = {"物品名称", "物品类别", "物品单位", "所属位置", "位置编号"};


    //根据文件类型，文件名获取测试文件
    public MultipartFile getFile(String fileName, String type) throws Exception {
        String projectPath = this.getClass().getClassLoader().getResource("").getPath();
        projectPath = projectPath.substring(1);
        String[] _pyPath = projectPath.split("out");
        projectPath = _pyPath[0] + "src/test/resources/import_unitTest/" + type + "/" + fileName;
        projectPath = URLDecoder.decode(projectPath, "UTF-8");

        File file = new File(projectPath);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "text/plain", new FileInputStream(file));

        return multipartFile;
    }

    @BeforeClass
    public void importInit() {

    }

    //进行一次导入验证表头
    public OutResult fileImport(String files, String type, OperationConfig oc) throws Exception {
        MultipartFile multipartFile = getFile(files, type);
        ImportConfig importConfig = new ImportConfig("物品导入",UUID.randomUUID().toString(), multipartFile, excelTitles, CommodityExcelVerifyVO.class, ca);

        //数据处理
        CommodityExcelDataHandler2 commodityExcelDataHandler = new CommodityExcelDataHandler2();
        //数据校验
        CommodityExcelDataVerifyHandler2 commodityExcelDataVerifyHandler = new CommodityExcelDataVerifyHandler2();

        commodityExcelDataHandler.setNeedHandlerFields(new String[]{"物品类别", "所属位置"});

        importConfig.setHyExcelDataHandler(commodityExcelDataHandler);
        importConfig.setHyExcelDataVerifyHandler(commodityExcelDataVerifyHandler);
        if (oc != null)
            importConfig.setOperationConfig(oc);
        //return hyImport.importData(importConfig);
        return null;
    }



    @Test(testName = "excel表头校验",
            dataProviderClass = TestDataProvider.class,
            dataProvider = "excelTitleVerify",
            groups = "excelTest")
    public void excelImportTitleVerify(String excelfiles) {
        try {
            OutResult outResult = fileImport(excelfiles, "excel", null);

            Assert.assertEquals(outResult.getErrorCode(), AppCommonErrorConfig.导入_表头错误_code, excelfiles + " 表头校验失败！");
            Assert.assertEquals(outResult.getMsg(), AppCommonErrorConfig.导入_表头错误_msg, excelfiles + " 表头校验失败！");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(excelfiles + " 文件导入出现未处理错误！断言失败！");
        }
    }

    public Map importOneData(String fileName, String type, OperationConfig oc) throws Exception {
        OutResult outResult = fileImport(fileName, type, oc);

        //给个小延迟等待数据校验线程拉起来
        Thread.sleep(1000);
        String taskId = (String) outResult.getData("taskId");

        ImportMonitor importMonitor = importMonitorService.getImportMonitor(taskId);
        HyThreadPoolExecutor checkPool = importMonitorService.getCheckPool(taskId);
        while (checkPool != null && checkPool.getActiveCount() != 0) {
            //等待数据校验线程运行完毕
        }

        //给个小延迟等待数据导入线程拉起来
        Thread.sleep(1000);
        HyThreadPoolExecutor importPool = importMonitorService.getImportPool(taskId);
        while (importPool != null && importPool.getActiveCount() != 0) {
            //等待导入线程运行完毕
        }

        Map map = new HashMap();
        map.put("outResult", outResult);
        map.put("importMonitor", importMonitor);
        return map;
//            while (true) {
//                //等待线程运行完毕
//            }
    }

    @Test(testName = "小数据量文档的导入",
            dataProviderClass = TestDataProvider.class,
            dataProvider = "excelSmallDatas",
            groups = "excelTest")
    public void importCorrectDatas(String file) {
        try {
            Map result = importOneData(file, "excel", null);
            ImportMonitor importMonitor = (ImportMonitor) result.get("importMonitor");
//            Assert.assertEquals(importMonitor.getCheckFailNum(), 0, "check断言失败");
            Assert.assertEquals(importMonitor.getTotalNum(), 80, "总数对应不上");
            Assert.assertEquals(importMonitor.getSuccessNum() + importMonitor.getCheckFailNum() + importMonitor.getImportFailNum(), 80);
            Assert.assertEquals(importMonitor.getSuccessNumRealTime().get() + importMonitor.getFailNumRealTime().get(), 78);
            Assert.assertEquals(importMonitor.getTaskStatus(), Status.FINISH, "最终状态错误！");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(file + " 文件导入出现未处理错误！断言失败！");
        }
    }

    @Test(testName = "csv表头校验",
            dataProviderClass = TestDataProvider.class,
            dataProvider = "csvTitleVerify",
            groups = "csvTest")
    public void csvImportVerifyTitle(String files) {
        try {
            OutResult outResult = fileImport(files, "csv", null);

            Assert.assertEquals(outResult.getErrorCode(), AppCommonErrorConfig.导入_表头错误_code, files + " 表头校验失败！");
            Assert.assertEquals(outResult.getMsg(), AppCommonErrorConfig.导入_表头错误_msg, files + " 表头校验失败！");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(files + " 文件导入出现未处理错误！断言失败！");
        }
    }

    @Test(testName = "csv表头校验",
            dataProviderClass = TestDataProvider.class,
            dataProvider = "csvCodingVerify",
            groups = "csvTest")
    public void csvImportVerifyCoding(String files) {
        try {
            OutResult outResult = fileImport(files, "csv", null);

            Assert.assertEquals(outResult.getErrorCode(), AppCommonErrorConfig.导入_文件编码错误_code, files + " 文件编码校验失败！");
            Assert.assertEquals(outResult.getMsg(), AppCommonErrorConfig.导入_文件编码错误_msg, files + " 文件编码校验失败！");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(files + " 文件导入出现未处理错误！断言失败！");
        }
    }

    @Test(testName = "csv小数据量导入",
            dataProviderClass = TestDataProvider.class,
            dataProvider = "csvSmallDatas",
            groups = "csvTest")
    public void csvImport(String file) {
        try {
            Map result = importOneData(file, "csv", null);
            ImportMonitor importMonitor = (ImportMonitor) result.get("importMonitor");

//            Assert.assertEquals(importMonitor.getCheckFailNum(), 0, "check断言失败");
            Assert.assertEquals(importMonitor.getTotalNum(), 100, "总数对应不上");
            Assert.assertEquals(importMonitor.getSuccessNum() + importMonitor.getCheckFailNum() + importMonitor.getImportFailNum(), 100);
            Assert.assertEquals(importMonitor.getTaskStatus(), Status.FINISH, "最终状态错误！");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(file + " 文件导入出现未处理错误！断言失败！");
        }
    }

}

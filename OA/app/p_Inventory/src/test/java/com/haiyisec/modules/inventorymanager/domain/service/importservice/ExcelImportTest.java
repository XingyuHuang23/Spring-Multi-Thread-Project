package com.haiyisec.modules.inventorymanager.domain.service.importservice;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.test.AppCommonErrorConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.Status;
import com.haiyisec.utest.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.frame.func.threadpool.threadpool.HyThreadPoolExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ExcelImportTest extends BaseTest {
    @Autowired
    private testService testService;
    @Autowired
    private ImportMonitorService importMonitorService;

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

    public void fileTitleVerify(String[] files, String type) {
        for (int i = 0; i < files.length; i++) {
            try {
                String taskId = String.valueOf(UUID.randomUUID());
                MultipartFile multipartFile = getFile(files[i], type);
                OutResult outResult = testService.test(multipartFile, taskId);
                String titleVerifyErrorMsg = outResult.getErrorCode();

                Assert.assertEquals(titleVerifyErrorMsg, AppCommonErrorConfig.导入_文件格式错误_code, files[i] + " 文件断言失败！");
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail(files[i] + " 文件导入出现未处理错误！断言失败！");
            }
        }

    }

    @Test
    public void excelImportVerifyTitle() {
        String[] excelfiles = {"title_null.xls",
                "title_error.xls",
                "title_more.xls",
                "title_null_error.xls",
                "title_mutilError.xls"};

        fileTitleVerify(excelfiles, "excel");
    }


    public void importOneData(String fileName, String type) {
        try {
            String taskId = String.valueOf(UUID.randomUUID());
            MultipartFile multipartFile = getFile(fileName, type);
            OutResult outResult = testService.test(multipartFile, taskId);

            Thread.sleep(1000);
            ImportMonitor importMonitor = importMonitorService.getImportMonitor(taskId);
            HyThreadPoolExecutor checkPool = importMonitorService.getCheckPool(taskId);
            while (checkPool != null && checkPool.getActiveCount() != 0) {
                //等待校验线程运行完毕
            }
            Thread.sleep(1000);
            HyThreadPoolExecutor importPool = importMonitorService.getImportPool(taskId);
            while (importPool != null && importPool.getActiveCount() != 0) {
                //等待导入线程运行完毕
            }

//            while (true) {
//                //等待线程运行完毕
//            }
            Thread.sleep(1000);
//            Assert.assertEquals(importMonitor.getCheckFailNum(), 0, "check断言失败");
            Assert.assertEquals(importMonitor.getTotalNum(), 80, "总数对应不上");
            Assert.assertEquals(importMonitor.getSuccessNum() + importMonitor.getCheckFailNum() + importMonitor.getImportFailNum(), 80);
          //  Assert.assertEquals(importMonitor.getSuccessNumRealTime().get() + importMonitor.getFailNumRealTime().get(), 78);
            Assert.assertEquals(importMonitor.getTaskStatus(), Status.FINISH, "最终状态错误！");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(fileName + " 文件导入出现未处理错误！断言失败！");
        }
    }

    @Test
    public void importCorrectDatas() {
        importOneData("data_correct_80.xls", "excel");
    }

    @Test
    public void importInCorrectDatas() {
        String[] excelfiles = {"data_null_cells.xls", "data_null_line.xls"};

        for (int i = 0; i < excelfiles.length; i++) {
            importOneData(excelfiles[i], "excel");
        }
    }

    //多线程的单元测试？
    @Test(enabled = false)
    public void mutilImport()     {
        List<String> list = new ArrayList();
        String[] excelfiles = {"data_correct_1000.xls", "data_correct_1000_2.xls", "data_correct_1000_3.xls"};

        for (int x = 0; x < excelfiles.length; x++) {
            try {
                String taskId = String.valueOf(UUID.randomUUID());
                list.add(taskId);
                MultipartFile multipartFile = getFile(excelfiles[x], "excel");
                OutResult outResult = testService.test(multipartFile, taskId);

                Thread.sleep(1000);
                ImportMonitor importMonitor = importMonitorService.getImportMonitor(taskId);
                HyThreadPoolExecutor checkPool = importMonitorService.getCheckPool(taskId);
                while (checkPool != null && checkPool.getActiveCount() != 0) {
                    //等待线程运行完毕
                }
                Thread.sleep(5000);
                HyThreadPoolExecutor importPool = importMonitorService.getImportPool(taskId);
                    while (importPool != null && importPool.getActiveCount() != 0) {
                        //等待线程运行完毕
                }

//            while (true) {
//                //等待线程运行完毕
//            }

//            Assert.assertEquals(importMonitor.getCheckFailNum(), 0, "check断言失败");
                Assert.assertEquals(importMonitor.getTotalNum(), 80, "总数对应不上");
                Assert.assertEquals(importMonitor.getSuccessNum() + importMonitor.getCheckFailNum() + importMonitor.getImportFailNum(), 80);
                Assert.assertEquals(importMonitor.getSuccessNumRealTime().get() + importMonitor.getFailNumRealTime().get(), 80);
                Assert.assertEquals(importMonitor.getTaskStatus(), Status.FINISH, "最终状态错误！");
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail(excelfiles[x] + " 文件导入出现未处理错误！断言失败！");
            }
        }

    }

    @Test(description = "csv错误表头")
    public void csvImportVerifyTitle() {
        String[] excelfiles = {"csv_null_utf-8-bom_titleNull.csv",
                "csv_null_utf-8-bom_titleError.csv"};

        fileTitleVerify(excelfiles, "csv");
    }

    @Test(description = "csv编码格式校验")
    public void csvImportVerifyCoding() {
        String[] excelfiles = {  "csv_oneData_ansi.csv", "csv_oneData_utf-8.csv"};

        fileTitleVerify(excelfiles, "csv");
    }

    @Test(description = "csv正常数据导入1")
    public void csvImport() {
        importOneData("csv_utf-8-bom_100.csv", "csv");
    }

    @Test(description = "csv正常数据导入2")
    public void csvImport2() {
        importOneData("csv_utf-8-bom_500.csv", "csv");
    }
}

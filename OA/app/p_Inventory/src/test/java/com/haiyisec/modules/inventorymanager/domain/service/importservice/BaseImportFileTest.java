package com.haiyisec.modules.inventorymanager.domain.service.importservice;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataHandler2;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataVerifyHandler2;
import com.haiyisec.oa.inventorymanager.domain.service.test.CommodityAdaption;
import com.haiyisec.oa.inventorymanager.domain.service.test.HyImport;
import com.haiyisec.oa.inventorymanager.domain.service.test.ImportConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.OperationConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.StrategyType;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.UserImportStrategy;
import com.haiyisec.utest.BaseTest_Web;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.hssf.usermodel.HSSFShapeContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testng.annotations.BeforeClass;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.frame.func.threadpool.threadpool.HyThreadPoolExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2021/3/19.
 */
@Slf4j
public abstract class BaseImportFileTest extends BaseTest_Web {

    protected static final int MaxLimit = 3;       //限定数量
    protected static final int MutilNum = MaxLimit + 1;

    protected static List<String> LoginUserIds = new ArrayList<String>();
    protected static List<String> ImportTaskType = new ArrayList<String>();

    MultipartFile multipartFile = null;

    public AtomicInteger curTaskTypeIndex = new AtomicInteger(0);
    public AtomicInteger curLoginUserIdIndex = new AtomicInteger(0);

    @Autowired
    private CommodityAdaption ca;

    public abstract ImportFile importFile();

    public abstract String[] excelTitles();

    @BeforeClass
    public void beforeClass() throws Exception {
        if (LoginUserIds.size() == 0) {
            for (int i = 0; i < MutilNum + 1; i++) {
                //todo: LoginUserIds的初始化 ,长度为 MutilNum-1
//                LoginUserIds.add("用户" + (i + 1));
                LoginUserIds.add(String.valueOf(UUID.randomUUID()));
            }
        }
        if (ImportTaskType.size() == 0) {     //长度为MutilNum -1
            for (int i = 0; i < MutilNum + 1; i++) {
                ImportTaskType.add("导入任务类别" + (i + 1));
            }
        }
        if (multipartFile == null) {
            multipartFile = getFile(importFile());
        }

        curTaskTypeIndex.set(0);
        curLoginUserIdIndex.set(0);

    }

    protected ImportConfig getImportConfigByParam(Param curParam, String userId) {

        ImportConfig ic = new ImportConfig(getTaskType(curParam.getImportTaskTypeCount()), multipartFile, excelTitles(), CommodityExcelVerifyVO.class, ca);

        ic.setHyExcelDataHandler(new CommodityExcelDataHandler2());
        ic.setHyExcelDataVerifyHandler(new CommodityExcelDataVerifyHandler2());

        //根据StrategyType 构建不同的 OperationConfig
        switch (curParam.getStrategyType()) {
            case APP:
                ic.setOperationConfig(getOperationConfigByParam_App(curParam));
                break;
            case USER:
                ic.setOperationConfig(getOperationConfigByParam_User(curParam, userId));
                break;
            case CUSTOM:
                ic.setOperationConfig(getOperationConfigByParam_Custom(curParam));
                break;
        }

        return ic;
    }

    private String getTaskType(int importTaskTypeCount) {
        int i = curTaskTypeIndex.getAndIncrement();
        return ImportTaskType.get(getIndexByAtLeastOne(importTaskTypeCount, i));
    }

    protected String getCurUserId(int loginUserCount) {
        int i = curLoginUserIdIndex.getAndIncrement();
        return LoginUserIds.get(getIndexByAtLeastOne(loginUserCount, i));
    }

    private OperationConfig<Integer> getOperationConfigByParam_App(Param curParam) {
        return new OperationConfig<Integer>()
                .setImportParams(MaxLimit)
                .setStrategyType(curParam.getStrategyType());
    }

    private OperationConfig<UserImportStrategy.ImportParams> getOperationConfigByParam_User(Param curParam, String curUserId) {
        return new OperationConfig<UserImportStrategy.ImportParams>()
                .setImportParams(new UserImportStrategy.ImportParams().setMaxUserTaskNum(MaxLimit).setUserId(curUserId))
                .setStrategyType(curParam.getStrategyType());
    }

    private OperationConfig<Integer> getOperationConfigByParam_Custom(Param curParam) {
        //@todo: 自定义策略
        return null;
    }

    /**
     * 在小于等于size时至少每一个都分走一个，剩下的大于size的那些随机
     *
     * @param size
     * @param curIndex
     * @return
     */
    private int getIndexByAtLeastOne(int size, int curIndex) {
        //循环
        if (curIndex <= size - 1) {
            return curIndex;
        } else {
            return RandomUtils.nextInt(0, size);
        }
    }

    //根据文件类型，文件名获取测试文件
    public MultipartFile getFile(ImportFile importFile) throws Exception {
        String projectPath = this.getClass().getClassLoader().getResource("").getPath();
        projectPath = projectPath.substring(1);
        String[] _pyPath = projectPath.split("out");
        projectPath = _pyPath[0] + "src/test/resources/import_unitTest/" + importFile.getImportFileType().name() + "/" + importFile.getFileName();
        projectPath = URLDecoder.decode(projectPath, "UTF-8");

        File file = new File(projectPath);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "text/plain", new FileInputStream(file));

        return multipartFile;
    }

    @Autowired
    private HyImport hyImport;
    @Autowired
    private ImportMonitorService importMonitorService;
    public static AtomicInteger successTaskNum = new AtomicInteger(0);
    public static AtomicInteger failTaskNum = new AtomicInteger(0);
    public static AtomicInteger tatolTaskNum = new AtomicInteger(0);
    //外部调用导入逻辑
    public Map importOneData(ImportConfig importConfig) throws Exception {
        OutResult outResult = hyImport.importData(importConfig);

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

    @Getter
    @Setter
    @Accessors(chain = true)
    @ToString
    public static class Param {
        private StrategyType strategyType;
        private int importTaskTypeCount;
        private int loginUserCount;
        private int successCount;
        private int failCount;

        public Param(StrategyType strategyType, int importTaskTypeCount, int loginUserCount, int successCount, int failCount) {
            this.strategyType = strategyType;
            this.importTaskTypeCount = importTaskTypeCount;
            this.loginUserCount = loginUserCount;
            this.successCount = successCount;
            this.failCount = failCount;
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ToString
    public static class ImportFile {
        private String fileName;
        private ImportFileType ImportFileType;
    }

    public static enum ImportFileType {
        excel, csv
    }


}


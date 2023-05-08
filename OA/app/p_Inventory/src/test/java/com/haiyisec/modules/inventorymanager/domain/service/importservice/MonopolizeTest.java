package com.haiyisec.modules.inventorymanager.domain.service.importservice;

import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.service.test.AppCommonErrorConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.ImportConfig;
import com.haiyisec.oa.inventorymanager.domain.service.test.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.zen.frame.base.domain.obj.OutResult;

import java.util.Map;

import static com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.StrategyType.APP;
import static com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.StrategyType.USER;

@Slf4j
@Test(suiteName = "独占策略")
@Listeners(MonopolizeSuiteListener.class)
public class MonopolizeTest extends BaseImportFileTest {

    @Override
    @Test(enabled = false)
    public ImportFile importFile() {
        return new ImportFile().setFileName("data_correct_1000.xls").setImportFileType(ImportFileType.excel);
    }

    @Override
    @Test(enabled = false)
    public String[] excelTitles() {
        return new String[]{"物品名称", "物品类别", "物品单位", "所属位置", "位置编号"};
    }

    public static Param[] params = {
            //todo: 后续完成特殊情况重启能再次导入的测试 选择一个普遍性最高的情况,模拟三种不同的失败情况,重启后还能再次导入

            new Param(USER, 1, 1, MaxLimit, 1),
            new Param(USER, RandomUtils.nextInt(2, MutilNum + 1), 1, MutilNum, 0),
            new Param(USER, 1, RandomUtils.nextInt(2, MutilNum + 1), MutilNum, 0),
            new Param(USER, RandomUtils.nextInt(2, MutilNum + 1), RandomUtils.nextInt(2, MutilNum + 1), MutilNum, 0),
//
            new Param(APP, 1, 1, MaxLimit, 1),
            new Param(APP, RandomUtils.nextInt(2, MutilNum + 1), 1, MutilNum, 0),
            new Param(APP, 1, RandomUtils.nextInt(2, MutilNum + 1), MaxLimit, 1),
            new Param(APP, RandomUtils.nextInt(2, MutilNum + 1), RandomUtils.nextInt(2, MutilNum + 1), MutilNum, 0)
    };

    private Param curParam;
//    private String loginId = "";

    @Test(testName = "独占策略测试", threadPoolSize = MaxLimit + 1, invocationCount = MaxLimit + 1)
    public void monopolizeTest0() {
//        tatolTaskNum.decrementAndGet();
        int a = tatolTaskNum.getAndIncrement();
        try {
             curParam = MonopolizeSuiteListener.curParam != null ? MonopolizeSuiteListener.curParam : params[0];
//            curParam = params[5];
            String curLoginUserId = getCurUserId(curParam.getLoginUserCount());
//            loginId = curLoginUserId;

            log.info(curLoginUserId + "$$$$$$$$$$$$$$$$$$$" + curParam.toString() + "$$$$$$$$$$$$$$$$$$$" + a);
            ImportConfig importConfig = getImportConfigByParam(curParam, curLoginUserId);

            Map result = importOneData(importConfig);
            OutResult outResult = (OutResult) result.get("outResult");
            if (!outResult.isSuccess()) {
//                if (curParam.getLoginUserCount() > 1) {
//                    Assert.fail();
//                }
                Assert.assertEquals(outResult.getErrorCode(), AppCommonErrorConfig.导入_限定策略_阈值_code);
                Assert.assertEquals(outResult.getMsg(), AppCommonErrorConfig.导入_限定策略_阈值_msg);
                failTaskNum.getAndIncrement();
            } else {
                ImportMonitor importMonitor = (ImportMonitor) result.get("importMonitor");
                Assert.assertEquals(importMonitor.getTotalNum(), 1500, "总数对应不上");
                Assert.assertEquals(importMonitor.getSuccessNum() + importMonitor.getCheckFailNum() + importMonitor.getImportFailNum(), 1500);
                Assert.assertEquals(importMonitor.getTaskStatus(), Status.FINISH, "最终状态错误！");
                successTaskNum.getAndIncrement();
            }

        } catch (Exception e) {
//            failTaskNum.getAndIncrement();
            Assert.fail("程序出错：   " + curParam.toString()+e.toString());
        }
    }

    @Test(dependsOnMethods = "monopolizeTest0", priority = 1, alwaysRun = true)
    public void finishTest() {
        Assert.assertEquals(failTaskNum.get(), curParam.getFailCount());
        Assert.assertEquals(successTaskNum.get(), curParam.getSuccessCount());

        successTaskNum.set(0);
        failTaskNum.set(0);
        tatolTaskNum.set(0);
    }

}

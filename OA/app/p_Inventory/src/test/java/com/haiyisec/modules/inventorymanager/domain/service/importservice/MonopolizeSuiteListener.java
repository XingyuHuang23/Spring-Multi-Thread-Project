package com.haiyisec.modules.inventorymanager.domain.service.importservice;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.util.concurrent.atomic.AtomicInteger;

import static com.haiyisec.modules.inventorymanager.domain.service.importservice.BaseImportFileTest.failTaskNum;
import static com.haiyisec.modules.inventorymanager.domain.service.importservice.BaseImportFileTest.successTaskNum;
import static com.haiyisec.modules.inventorymanager.domain.service.importservice.BaseImportFileTest.tatolTaskNum;

public class MonopolizeSuiteListener implements ISuiteListener {
    public static final AtomicInteger curCount = new AtomicInteger(0);
    public static BaseImportFileTest.Param curParam = null;

    @Override
    public void onFinish(ISuite suite) {
        int a = curCount.incrementAndGet();
        if (curCount.get() < MonopolizeTest.params.length) {
            curParam = MonopolizeTest.params[a];
            suite.run();
        }
    }
}

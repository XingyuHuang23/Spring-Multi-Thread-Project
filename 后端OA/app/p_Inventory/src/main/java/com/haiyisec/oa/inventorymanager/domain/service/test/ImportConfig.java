package com.haiyisec.oa.inventorymanager.domain.service.test;

import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.HyExcelDataHandler;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.HyExcelDataVerifyHandler;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.OperationConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class ImportConfig {

    public ImportConfig() {

    }

    //@change: 增加 userId 参数
    public ImportConfig(String model,String userId,MultipartFile file,String[] titles,Class<?> dataEntity,IHyDataAdaption IHyDataAdaption) {
        this.file = file;
        this.titles = titles;
        this.dataEntity = dataEntity;
        this.IHyDataAdaption = IHyDataAdaption;
        this.model = model;
        this.userId = userId;
//        this.fileConfig = fileConfig;
    }

    //表头
    private String[] titles;
    //导入任务名称
    private String taskId = String.valueOf(UUID.randomUUID());
    //文件
    private MultipartFile file;
    //能否单业务多导入
    private boolean multiImport;

    private HyExcelDataHandler hyExcelDataHandler;
    private HyExcelDataVerifyHandler hyExcelDataVerifyHandler;
    private Class<?> dataEntity;

    private IHyDataAdaption IHyDataAdaption;
    //业务名
    private String model;
    private String userId;
    private OperationConfig operationConfig;

    private FileConfig fileConfig = new FileConfig();
    //同operationConfig一样 抽出去包成另一个config


}

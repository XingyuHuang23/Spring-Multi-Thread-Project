package com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OperationConfig<ImportParams> {

    private ImportStrategy<ImportParams> customImportStrategy; //策略

    private ImportParams importParams; //参数

    private StrategyType strategyType;//策略类型
}

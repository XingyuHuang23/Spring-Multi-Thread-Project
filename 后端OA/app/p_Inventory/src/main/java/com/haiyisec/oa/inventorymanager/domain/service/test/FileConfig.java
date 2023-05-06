package com.haiyisec.oa.inventorymanager.domain.service.test;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FileConfig {

    private int dividedNum = 200;
    private int headRows = 1;
    private int titleRows = 0;
}

package com.haiyisec.oa.inventorymanager.domain.model.vo.goods;


import lombok.Getter;
import lombok.Setter;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;

import java.util.List;

@Getter
@Setter
public class CheckResultVO {


    @Zen_Field("成功数据")
    private List<CommodityExcelVerifyVO> successData;

    @Zen_Field("失败数据")
    private List<CommodityExcelVerifyVO> failData;

    @Zen_Field("taskId")
    private String taskId;
}

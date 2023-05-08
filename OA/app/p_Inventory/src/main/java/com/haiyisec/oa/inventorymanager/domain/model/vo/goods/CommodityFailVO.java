package com.haiyisec.oa.inventorymanager.domain.model.vo.goods;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;

import javax.validation.constraints.Size;

@Getter
@Setter
public class CommodityFailVO extends CommodityVO {
    @Size(min = 1, max = 255, message = "字段太长")
    @Zen_Field("错误原因")
    @Excel(name = "错误原因")
    private String failReason;
}

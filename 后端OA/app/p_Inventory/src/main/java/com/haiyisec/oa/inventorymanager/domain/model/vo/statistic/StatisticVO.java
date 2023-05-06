package com.haiyisec.oa.inventorymanager.domain.model.vo.statistic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
public class StatisticVO {

    @NotNull
    @Size(min = 1,max = 50,message = "字段太长")
    @Zen_Field("物品名称")
    @Excel(name = "物品名称")
    @ExcelEntity
    private String commodityName;

    private String commodityId;

    @NotNull
    @Size(min = 1,max = 10,message = "字段太长")
    @Zen_Field("已使用")
    @Excel(name = "已使用")
    @ExcelEntity
    private String hadUse;

    @NotNull
    @Size(min = 1,max = 32,message = "字段太长")
    @Zen_Field("剩余量")
    @Excel(name = "剩余量")
    @ExcelEntity
    private String rest;
}

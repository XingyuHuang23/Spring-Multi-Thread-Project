package com.haiyisec.oa.inventorymanager.domain.model.vo.goods;

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
public class CommodityVO {

    @Size(min = 1,max = 50,message = "字段太长")
    @Zen_Field("物品名称")
    @Excel(name = "物品名称")
    @ExcelEntity
    private String name;

    @Size(min = 1,max = 32,message = "字段太长")
    @Zen_Field("物品类型")
    @Excel(name = "物品类别")
    @ExcelEntity
    private String type;

    @Size(min = 1,max = 10,message = "字段太长")
    @Zen_Field("物品单位")
    @Excel(name = "物品单位")
    @ExcelEntity
    private String unit;

    @Size(min = 1,max = 32,message = "字段太长")
    @Zen_Field("所属位置")
    @Excel(name = "所属位置")
    @ExcelEntity
    private String position;

    @Size(min = 1,max = 10,message = "字段太长")
    @Zen_Field("位置编号")
    @Excel(name = "位置编号")
    @ExcelEntity
    private String commodityNo;

}

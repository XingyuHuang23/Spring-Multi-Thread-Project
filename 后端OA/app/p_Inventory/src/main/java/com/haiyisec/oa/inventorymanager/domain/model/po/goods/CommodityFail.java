package com.haiyisec.oa.inventorymanager.domain.model.po.goods;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Where(clause = "has_del='0'")
public class CommodityFail extends BaseModel {
    //@todo: 以后修改为公用缓存表
    //@todo: 表头跟数据的映射，最好是拿到原始数据

    //@todo 使用Fail接收读取数据
    @Column(length = 50)
    @Size(max = 50, message = "字段太长")
    @Zen_Field("物品名称")
    @Excel(name = "物品名称")
    @ExcelEntity
    private String name;
    //字典  DicInfoPO_ID
    @Column(length = 50)
    @Size(max = 50, message = "字段太长")
    @Zen_Field("物品类型")
    @Excel(name = "物品类别")
    @ExcelEntity
    private String type;
    //DicInfoPO_ID
    @Column(length = 10)
    @Size(min = 1, max = 10, message = "字段太长")
    @Zen_Field("物品单位")
    @Excel(name = "物品单位")
    @ExcelEntity
    private String unit;
    //DicInfoPO_ID
    @Column(length = 32)
    @Size(min = 1, max = 32, message = "字段太长")
    @Zen_Field("所属位置")
    @Excel(name = "所属位置")
    @ExcelEntity
    private String position;
    @Column(length = 10)
    @Size(min = 1, max = 10, message = "字段太长")
    @Zen_Field("位置编号")
    @Excel(name = "位置编号")
    @ExcelEntity
    private String commodityNo;
    @Column(nullable = true, length = 10)
    @Size(min = 1, max = 10, message = "字段太长")
    @Zen_Field("物品库房")
    @ExcelIgnore
    private String storeId;

    @Column(nullable = false, length = 36)
    @NotNull
    @Size(min = 1, max = 36, message = "字段太长")
    @Zen_Field("导入监控id")
    @ExcelIgnore
    private String importMonitorId;

    @Size(min = 1, max = 255, message = "字段太长")
    @NotNull
    @Zen_Field("导入失败原因")
    @Excel(name = "失败原因")
    @ExcelEntity
    @Column(nullable = false, length = 255)
    private String failReason;

    @Column(nullable = false, length = 36)
    @NotNull
    @Size(min = 1, max = 36)
    @Zen_Field("任务id")
    @ExcelIgnore
    private String taskId;
}

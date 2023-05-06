package com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.base.domain.annotation.query.Zen_Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Where(clause = "has_del='0'")
@Table(indexes = {
        @Index(name = "UDX_HasDel_Name",columnList = "hasDel",unique = true),
        @Index(name = "UDX_HasDel_Name",columnList = "commodityName",unique = true),
        @Index(name = "UDX_HasDel_Name",columnList = "storeId",unique = true)
})

/**
 * @Des
 * @Author Machenike
 * @Date 2021/1/7 10:06
 */
public class InventoryStatistics  extends BaseModel {

    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 32,max = 32,message = "物品id超长")
    @Zen_Field("物品id")
    private String commodityId;

    @Column(nullable = false,length = 96)
    @NotNull
    @Size(min = 1,max = 96)
    @Zen_Field("物品名称")
    @Excel(name = "物品名称")
    @ExcelEntity
    @Zen_Query("like")
    private String commodityName;


    @Column(nullable = false,length = 10)
    @NotNull
    @Min(0)
    @Zen_Field("已使用")
    @Excel(name = "已使用")
    @ExcelEntity
    private int hadUse;

    @Column(nullable = false,length = 10)
    @NotNull
    @Max(1000000)
    @Min(0)
    @Zen_Field("库存剩余")
    @Excel(name = "库存剩余")
    @ExcelEntity
    private int rest;


    @Column(nullable = false,length = 10,columnDefinition="varchar(32) default '000'")
    @Size(min = 1,max = 32)
    @NotNull
    @Zen_Field("库房id")
    @ExcelEntity
    private String storeId;
}

package com.haiyisec.oa.inventorymanager.domain.model.po.goods;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.base.domain.annotation.query.Zen_Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//entity是否使用
//@Builder
@Entity
@Getter
@Setter
@Where(clause = "has_del='0'")
@Table(indexes = {
        @Index(name = "UDX_HasDel_Name_CommodityNo", columnList = "hasDel", unique = true),
        @Index(name = "UDX_HasDel_Name_CommodityNo", columnList = "name", unique = true),
})
public class Commodity extends BaseModel {
    @Column(nullable = false, length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    @Zen_Query("like")
    @Zen_Field("物品名称")
    private String name;

    //字典  DicInfoPO_ID
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    @Zen_Field("物品类型")
    private String type;
    //DicInfoPO_ID
    @Column(nullable = false, length = 10)
    @NotNull
    @Size(min = 1, max = 10)
    @Zen_Field("物品单位")
    private String unit;
    //DicInfoPO_ID
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    @Zen_Field("所属位置")
    private String position;

    @Column(nullable = true, length = 10)
    @Size(min = 0, max = 10)
    @Zen_Field("位置编号")
    private String commodityNo;

    @Column(nullable = true, length = 10)
    @NotNull
    @Size(min = 1, max = 10)
    @Zen_Field("物品库房")
    private String storeId;

    @Transient
    private String search;
}

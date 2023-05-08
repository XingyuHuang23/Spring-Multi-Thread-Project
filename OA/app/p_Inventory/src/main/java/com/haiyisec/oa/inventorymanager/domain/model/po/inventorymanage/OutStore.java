package com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.base.domain.annotation.query.Zen_Query;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Where(clause = "has_del='0'")
public class OutStore extends BaseModel {
    @Column(nullable = false)
    @NotNull
    @Zen_Field("出库时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Zen_Query(value = "<=", column = "bae003")
    @Type(
            type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime"
    )
    private DateTime outstoreTime;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32, message = "出库申请人id不存在")
    @Zen_Field("出库申请人")
    private String applicant;


    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32, message = "物品id不存在")
    @Zen_Field("入库物品id")
    private String commodityId;

    @Column(nullable = false, length = 10)
    @NotNull
//    @Size(min = 0,max = 10,message = "入库物品数量过大,保存错误")
    @Zen_Field("出库物品数量")
    @Min(value = 1, message = "入库物品数量小于1,保存错误")
    @Max(value = 1000000, message = "入库物品数量大于1000000,保存错误")
    private int outstoreQuantity;


    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32, message = "库房id超长")
    @Zen_Field("库房id")
    private String storeId;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32, message = "出库部门id有误")
    @Zen_Field("出库部门id")
    private String department;

    @Transient
    private String search;
}

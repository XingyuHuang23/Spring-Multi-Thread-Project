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

import javax.persistence.*;
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
public class InStore extends BaseModel {

    @Column(nullable = false)
    @NotNull
    @Zen_Field("入库时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Zen_Query(value="<=",column = "bae003")
    @Type(
            type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime"
    )
    private DateTime instoreTime;

    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 1,max = 32,message = "购买人id长度有误")
    @Zen_Field("入库购买人")
    private String purchaser;


    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 1,max = 32,message = "物品id长度有误")
    @Zen_Field("入库物品id")
    private String commodityId;

    @Column(nullable = false,length = 10)
    @NotNull
    @Zen_Field("入库物品数量")
    @Min(value = 1, message = "入库物品数量小于1,保存错误")
    @Max(value = 1000000, message = "入库物品数量大于1000000,保存错误")
    private int instoreQuantity;

    @Column(nullable = false,length =10,columnDefinition = "double(10,2)")
    @Zen_Field("入库物品价格")
    @NotNull
    @Min(value = 0, message = "入库物品价格小于0,保存错误")
    @Max(value = 100000, message = "入库物品价格大于100000,保存错误")
    private double instorePrice;


    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 1,max = 32,message = "库房id有误")
    @Zen_Field("库房id")
    private String storeId;

    @Transient
    private String search;
}

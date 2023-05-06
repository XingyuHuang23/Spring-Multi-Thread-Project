package com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Where(clause = "has_del='0'")
@Table(indexes = {
        @Index(name = "UDX_HasDel_Name_AttributeInfoId",columnList = "hasDel",unique = true),
        @Index(name = "UDX_HasDel_Name_AttributeInfoId",columnList = "name",unique = true),
        @Index(name = "UDX_HasDel_Name_AttributeInfoId",columnList = "attributeInfoId",unique = true),
})
public class AttributeInfoItem extends BaseModel {

    @Column(nullable = false,length = 50)
    @NotNull
    @Size(min = 1,max = 50,message = "属性选项名称长度超长,保存错误")
    @Zen_Field("属性选项名称")
    private String name;

    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 1,max = 32,message = "属性选项所属选项id长度超长,保存错误")
    @Zen_Field("属性选项所属选项id")
    private String attributeInfoId;
}

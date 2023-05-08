package com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.base.domain.annotation.query.Zen_Query;
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
//自己写的sql都要加上hasDel
@Where(clause = "has_del='0'")
@Table(indexes = {
        @Index(name = "UDX_HasDel_Name",columnList = "hasDel",unique = true),
        @Index(name = "UDX_HasDel_Name",columnList = "name",unique = true),
})
public class AttributeInfo extends BaseModel {
    //control基础校验
    @Column(nullable = false,length = 10)
    @NotNull
    @Size(min = 1,max = 10,message = "属性名称长度超长")
    @Zen_Field("属性名称")
    @Zen_Query("like")
    private String name;

}

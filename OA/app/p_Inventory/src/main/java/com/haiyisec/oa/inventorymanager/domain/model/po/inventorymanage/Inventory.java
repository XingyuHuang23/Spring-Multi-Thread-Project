package com.haiyisec.oa.inventorymanager.domain.model.po.inventorymanage;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Where(clause = "has_del='0'")
public class Inventory extends BaseModel {
    @Column(nullable = false,length = 50)
    @NotNull
    @Size(min = 1,max = 50,message = "库房超长")
    @Zen_Field("库房名称")
    private String name;

    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 1,max = 32,message = "部门id超长")
    @Zen_Field("部门")
    private String departmentId;

    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 1,max = 32,message = "管理员id超长")
    @Zen_Field("管理员id")
    private String manager;
}

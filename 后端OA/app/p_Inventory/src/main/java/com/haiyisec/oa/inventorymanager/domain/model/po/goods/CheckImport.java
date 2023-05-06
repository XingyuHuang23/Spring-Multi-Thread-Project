package com.haiyisec.oa.inventorymanager.domain.model.po.goods;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.pojo.Zen_Field;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Where(clause = "has_del='0'")
public class CheckImport extends BaseModel {
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    @Zen_Field("用户id")
    private String userId;


    @Column(nullable = false, length = 36)
    @NotNull
    @Size(min = 1, max = 36)
    @Zen_Field("任务id")
    private String taskId;

    @Transient
    private String search;
}

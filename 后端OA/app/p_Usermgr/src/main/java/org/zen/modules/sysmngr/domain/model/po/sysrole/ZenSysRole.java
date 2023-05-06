package org.zen.modules.sysmngr.domain.model.po.sysrole;

import org.hibernate.annotations.Where;
import org.zen.frame.base.domain.annotation.check.Zen_DC_Unique_Field;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Oscar on 2016/10/12.
 */
@Entity
@Where(clause = "has_del='0'")     //只能加载Entity类上，加在BaseModel或IBaseModel上无效
public class ZenSysRole extends BaseModel {

    @Column(nullable = false,length=100)
    @Zen_DC_Unique_Field(fieldName = "角色名称")
    private String name;
    @Column(length=100)
    private String type;
    @Column(length=100)
    private String orgId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}

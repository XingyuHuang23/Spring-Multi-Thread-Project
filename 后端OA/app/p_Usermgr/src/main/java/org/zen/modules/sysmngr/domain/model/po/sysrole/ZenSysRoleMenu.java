package org.zen.modules.sysmngr.domain.model.po.sysrole;

import org.hibernate.annotations.Where;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Oscar on 2016/10/12.
 */
@Entity
@Where(clause = "has_del='0'")     //只能加载Entity类上，加在BaseModel或IBaseModel上无效
public class ZenSysRoleMenu extends BaseModel {

    @Column(nullable = false,length=255)
    private String roleId;
    @Column(nullable = false,length=255)
    private String menuId;
    @Column(nullable = false,length=255)
    private String menuName;


    public String getRoleId() {
        return roleId;
    }

    public ZenSysRoleMenu setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}

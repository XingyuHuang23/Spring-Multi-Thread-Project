package org.zen.modules.sysmngr.domain.model.vo;

import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysUserRole;
import org.zen.modules.sysmngr.domain.model.po.sysuser.SysUser;
import org.zen.modules.sysmngr.domain.model.po.sysuser.UserExtendPO;

import java.util.List;

/**
 * Created by Oscar on 2016/10/13.
 */
public class ZenSysUserVO {
    private UserExtendPO sysUser;
    private List<ZenSysUserRole> zenSysUserRole;
    private String password;
    private String pwd;

    public UserExtendPO getSysUser() {
        return sysUser;
    }

    public void setSysUser(UserExtendPO sysUser) {
        this.sysUser = sysUser;
    }

    public List<ZenSysUserRole> getZenSysUserRole() {
        return zenSysUserRole;
    }

    public void setZenSysUserRole(List<ZenSysUserRole> zenSysUserRole) {
        this.zenSysUserRole = zenSysUserRole;
    }

    public String getPassword() {
        return password;
    }

    public ZenSysUserVO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

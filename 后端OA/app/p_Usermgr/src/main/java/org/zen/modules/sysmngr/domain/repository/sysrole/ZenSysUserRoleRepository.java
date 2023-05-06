package org.zen.modules.sysmngr.domain.repository.sysrole;

import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysUserRole;

import java.util.List;

/**
 * Created by Oscar on 2016/10/12.
 */
public interface ZenSysUserRoleRepository extends BaseRepository<ZenSysUserRole, String> {
    public List<ZenSysUserRole> findByUserId(String userId);
    public List<ZenSysUserRole> findByRoleId(String roleId);
    public void deleteByUserId(String userId);
}

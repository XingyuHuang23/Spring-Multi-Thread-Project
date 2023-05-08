package org.zen.modules.sysmngr.domain.repository.sysrole;

import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRoleMenu;

import java.util.List;

/**
 * Created by Oscar on 2016/10/12.
 */
public interface ZenSysRoleMenuRepository extends BaseRepository<ZenSysRoleMenu, String> {
    public List<ZenSysRoleMenu> findByRoleId(String roleId);
    public void deleteByRoleId(String roleId);
}

package org.zen.modules.sysmngr.domain.repository.sysrole;

import org.zen.frame.vendor.spring.springdata.jpa.domain.repository.BaseRepository;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRole;

/**
 * Created by Oscar on 2016/10/12.
 */
public interface ZenSysRoleRepository extends BaseRepository<ZenSysRole, String> {
    public Long countByName(String name);
    public Long countByNameAndIdNot(String name, String id);

    public ZenSysRole findOneByName(String name);
}

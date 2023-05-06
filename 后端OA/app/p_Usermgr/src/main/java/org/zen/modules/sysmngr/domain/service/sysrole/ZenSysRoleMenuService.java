package org.zen.modules.sysmngr.domain.service.sysrole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRoleMenu;
import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysRoleMenuRepository;

import java.util.List;

/**
 * Created by Oscar on 2016/10/12.
 */
@Service
@Transactional
public class ZenSysRoleMenuService extends BaseService<ZenSysRoleMenu> {
    @Autowired
    private ZenSysRoleMenuRepository dao;
    @Override
    public ZenSysRoleMenuRepository getDao() {
        return dao;
    }

    public List<ZenSysRoleMenu> queryRoleIdList(String roleId){
        return  dao.findByRoleId(roleId);
    }

    public void deleteRoleId(String roleId){
         dao.deleteByRoleId(roleId);
    }

}

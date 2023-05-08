package org.zen.modules.sysmngr.domain.service.sysrole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRole;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysUserRole;
import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysRoleRepository;
import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysUserRoleRepository;

import java.util.List;

/**
 * Created by Oscar on 2016/10/12.
 */
@Service
@Transactional
public class ZenSysUserRoleService extends BaseService<ZenSysUserRole> {
    @Autowired
    private ZenSysUserRoleRepository dao;

    @Autowired
    private ZenSysRoleRepository dao_role;

    @Override
    public ZenSysUserRoleRepository getDao() {
        return dao;
    }

    public List<ZenSysUserRole> queryUserIdList(String userId){
        return  dao.findByUserId(userId);
    }

    public void deleteByUserId(String userId){
        dao.deleteByUserId(userId);
    }

    public List<ZenSysUserRole> queryByRoleId(String roleId){
        return  dao.findByRoleId(roleId);
    }

    public List<ZenSysUserRole> queryByRoleName(String roleName){
        ZenSysRole role = dao_role.findOneByName(roleName);
        if(role != null){
            return  dao.findByRoleId(role.getId());
        }else {
            return null;
        }

    }
}

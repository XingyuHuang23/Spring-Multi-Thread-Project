package org.zen.modules.sysmngr.domain.service.sysuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRole;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRoleMenu;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysUserRole;
import org.zen.modules.sysmngr.domain.model.po.sysuser.SysUser;
import org.zen.modules.sysmngr.domain.dao.sysuser.SysUserRepository;
import org.zen.modules.sysmngr.domain.model.po.sysuser.UserExtendPO;
import org.zen.modules.sysmngr.domain.repository.sysuser.UserExtendRepository;
import org.zen.modules.sysmngr.domain.service.sysrole.ZenSysRoleMenuService;
import org.zen.modules.sysmngr.domain.service.sysrole.ZenSysRoleService;
import org.zen.modules.sysmngr.domain.service.sysrole.ZenSysUserRoleService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 2016/10/12.
 */
@Service
@Transactional
public class SysUserService extends BaseService<SysUser> {
    @Autowired
    private SysUserRepository dao;
    @Autowired
    private ZenSysUserRoleService zenSysUserRoleService;
    @Autowired
    private ZenSysRoleMenuService zenSysRoleMenuService;
    @Autowired
    private ZenSysRoleService zenSysRoleService;

    @Autowired
    private UserExtendRepository userExtendRepository;

    @Override
    public SysUserRepository getDao() {
        return dao;
    }

    /**
     * 查询用户下的功能菜单
     * @param id
     * @return
     */
    public List<ZenSysRoleMenu> queryIdList(String id){
        List<ZenSysRoleMenu> list_menu=new ArrayList<>();
        List<ZenSysUserRole> list_role=zenSysUserRoleService.queryUserIdList(id);
        for(ZenSysUserRole zenSysUserRole :list_role){
            list_menu.addAll(zenSysRoleMenuService.queryRoleIdList(zenSysUserRole.getRoleId()));
        }
        return list_menu;
    }

    /**
     * 根据roleName 查询 sysuser
     * @return
     */
    public List<SysUser> queryUsersByRoleName(String roleName){
        ZenSysRole role = zenSysRoleService.findOneByName(roleName);
        List<ZenSysUserRole> list_userRole = zenSysUserRoleService.queryByRoleId(role.getId());
        List<String> list_userId = new ArrayList<String>();
        for (ZenSysUserRole zenSysUserRole : list_userRole) {
            list_userId.add(zenSysUserRole.getUserId());
        }
        return dao.findByIdIn(list_userId);
    }

    public String getUserName(String userId){
        SysUser su =  dao.getOne(userId);
        if(su != null){
            return su.getName();
        }else{
            return null;
        }
    }

    public UserExtendPO getUserExtend(String id){
        return userExtendRepository.getOne(id);
    }
}

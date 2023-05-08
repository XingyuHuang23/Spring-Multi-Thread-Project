package org.zen.modules.sysmngr.domain.service.sysrole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zen.frame.base.domain.annotation.dic.Zen_Dic_DD;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRole;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysUserRole;
import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysRoleRepository;
import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysUserRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Oscar on 2016/10/12.
 */
@Service
@Transactional
public class ZenSysRoleService extends BaseService<ZenSysRole> {
    @Autowired
    private ZenSysRoleRepository dao;
    @Autowired
    private ZenSysUserRoleRepository dao_userRole;

    @Override
    public ZenSysRoleRepository getDao() {
        return dao;
    }


    @Zen_Dic_DD(dicName = "dd_角色", beanName = "zenSysRoleService")
    public List<DicItemVO> getDic_DD_Role(Map<String, Object> map_param) {
        List<DicItemVO> list_vo = new ArrayList<DicItemVO>();
        List<ZenSysRole> list_role = dao.findAll();
        for (ZenSysRole zenSysRole : list_role) {
            DicItemVO item = new DicItemVO();
            item.setCode(zenSysRole.getId());
            item.setText(zenSysRole.getName());
            list_vo.add(item);
        }
        return list_vo;
    }


    public ZenSysRole findOneByName(String name) {
        return dao.findOneByName(name);
    }

    /**
     * @Description 判断角色是否绑定用户
     * @param roleid
     * @return boolean
     */
    public boolean isExitUser(String roleid){
       List<ZenSysUserRole> list = dao_userRole.findByRoleId(roleid);
        if(list!=null && list.size()>0){
            return true;
        }else{
            return false;
        }
    }


}

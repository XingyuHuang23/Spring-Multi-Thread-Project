package org.zen.modules.sysmngr.domain.check;

import org.zen.frame.base.domain.check.obj.CheckMethodCallObject;
import org.zen.frame.base.domain.check.service.CheckService_Owner;
import org.zen.modules.sysmngr.domain.model.po.sysuser.SysUser;


public class SysUserCheck extends CheckService_Owner<SysUser> {

//    @Zen_DC_Method(id= "该用户名已存在",msgTemplate = "该用户名已存在")
//    public static CheckResult loginName_Unique(CheckMethodCallObject<SysUser> cmco){
//        SysUser user = cmco.getEntity();
//        CheckResult cr = cmco.getCr();
//        CheckObject co = cmco.getCo();
//
//        if(user != null){
//            try {
//                SysUserRepository dao = Domain_DB.getDao(SysUser.class, SysUserRepository.class);
//
//                Long count = 0L;
//                String id = user.getId();
//                if(id == null || "".equals(id)){
//                    count = dao.countByLoginName(user.getLoginName());
//                }else{
//                    count = dao.countByLoginNameAndIdNot(user.getLoginName(), id);
//                }
//                if (count > 0) {
//                    setFalse_CheckResult(cr,co);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return cr;
//    }

}

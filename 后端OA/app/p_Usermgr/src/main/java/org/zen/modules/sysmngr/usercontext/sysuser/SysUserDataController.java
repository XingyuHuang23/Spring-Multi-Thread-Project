package org.zen.modules.sysmngr.usercontext.sysuser;

import app.frame.vendor.spring.springmvc.common.usercontext.BaseController;
import app.frame.vendor.spring.springmvc.common.usercontext.IControllRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zen.frame.base.domain.context.UserContext;
import org.zen.frame.base.domain.exception.CheckResultException;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IBaseResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.global.SortConfig;
import org.zen.frame.vendor.spring.springmvc.service.ICurUserService;
import org.zen.modules.login.util.PWUtil;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRoleMenu;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysUserRole;
import org.zen.modules.sysmngr.domain.model.po.sysuser.SysUser;
import org.zen.modules.sysmngr.domain.model.vo.ZenSysUserVO;
import org.zen.modules.sysmngr.domain.service.sysrole.ZenSysUserRoleService;
import org.zen.modules.sysmngr.domain.service.sysuser.SysUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Oscar on 2016/10/12.
 */
@RestController
@RequestMapping("/Sysmngr/SysUserData")
public class SysUserDataController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ZenSysUserRoleService zenSysUserRoleService;

    @Autowired
    private ICurUserService curUserService;

    @Override
    public UserContext createUserContext() {
        return new UserContext().setName("Sysmngr.SysUserData");
    }


    @RequestMapping(value = "/userQuery")
//    @PreAuthorize("hasAuthority('aaa')")
//    @Zen_AppSecurity(type="操作权限",auths="aaa && bbb")
    public IBaseResult userQuery(HttpServletRequest request, @RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                Pageable page = getPageableFromRequest(map, "glt_用户列表", SortConfig.BAE003_DESC_AND_ID_DESC);
                Page<SysUser> page_sysUser = sysUserService.queryList(page);
//                or.addGlt("glt_用户列表1", page_sysUser);
                or.addGlt("glt_用户列表", page_sysUser);
            }
        });
    }

    @RequestMapping(value = "/userget/{userid}")
    public IBaseResult userget(HttpServletRequest request, @PathVariable String userid) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                SysUser sysUser = sysUserService.get(userid);
                List<ZenSysUserRole> list_role = zenSysUserRoleService.queryUserIdList(userid);
                or.addGt("gt_用户信息", sysUser);
                or.addGlt("glt_用户角色", list_role);
            }
        });
    }

    @RequestMapping(value = "/userSave")
    public IBaseResult userSave(HttpServletRequest request, @RequestBody final ZenSysUserVO zenSysUserVO) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                boolean isNeedUpdatePwd = false;
                if ("#$nochange$#".equals(zenSysUserVO.getPassword())) {
                    SysUser su = sysUserService.get(zenSysUserVO.getSysUser().getId());
                    zenSysUserVO.getSysUser().setPassword(su.getPassword());
                } else {
                    if(zenSysUserVO.getSysUser().getId() == null){
                        isNeedUpdatePwd = true;
                        zenSysUserVO.getSysUser().setPassword("$tmp$");
                    }else{
                        String pw = PWUtil.transformFront2DB(zenSysUserVO.getSysUser().getId(),zenSysUserVO.getPassword());
                        zenSysUserVO.getSysUser().setPassword(pw);
                    }

                }

                sysUserService.save(cr, zenSysUserVO.getSysUser());

                if (isNeedUpdatePwd) {
                    String pw = PWUtil.transformFront2DB(zenSysUserVO.getSysUser().getId(),zenSysUserVO.getPassword());
                    zenSysUserVO.getSysUser().setPassword(pw);
                    sysUserService.save(cr, zenSysUserVO.getSysUser());
                }

                List<ZenSysUserRole> list_role = zenSysUserVO.getZenSysUserRole();
                if (list_role == null || list_role.size() <= 0) {
                    return;
                }
                String userid = zenSysUserVO.getSysUser().getId();
                zenSysUserRoleService.deleteByUserId(userid);
                for (ZenSysUserRole zenSysUserRole : list_role) {
                    zenSysUserRole.setUserId(userid);
                    zenSysUserRoleService.save(cr, zenSysUserRole);
                }
                or.addData("userId", userid);
            }
        });
    }


    @RequestMapping(value = "/userSaveRold/{userid}")
    public IBaseResult userSaveRold(HttpServletRequest request, @RequestBody List<ZenSysUserRole> list_role, @PathVariable String userid) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                zenSysUserRoleService.deleteByUserId(userid);
                for (ZenSysUserRole zenSysUserRole : list_role) {
                    zenSysUserRole.setUserId(userid);
                    zenSysUserRoleService.save(cr, zenSysUserRole);
                }
            }
        });
    }

    @RequestMapping(value = "/userDelete")
    public IBaseResult userDelete(HttpServletRequest request, @RequestBody List<String> list_userid) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                String loginUserId = curUserService.getLoginUserId();
                for (String userid : list_userid) {
                    if(loginUserId.equals(userid)){
                        cr.setErrorMsg(new StringBuffer("不能删除当前登录用户!"));
                        cr.setSuccess(false);
                        return;
                    }

                    sysUserService.delete(cr, userid);
                    zenSysUserRoleService.deleteByUserId(userid);
                }
            }
        });
    }

    //    @PreAuthorize("hasAnyAuthority('aaa')")
    @RequestMapping(value = "/userMenu")
    public IBaseResult userMenu(HttpServletRequest request) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                List<ZenSysRoleMenu> list = sysUserService.queryIdList(curUserService.getLoginUserId());
                or.addGlt("glt_用户菜单", list);
            }

        });
    }



}

package org.zen.modules.sysmngr.usercontext.sysrole;

import app.frame.vendor.spring.springmvc.common.usercontext.BaseController;
import app.frame.vendor.spring.springmvc.common.usercontext.IControllRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.zen.modules.securitycheck.annotation.Zen_AppSecurity;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRole;
import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRoleMenu;
import org.zen.modules.sysmngr.domain.service.sysrole.ZenSysRoleMenuService;
import org.zen.modules.sysmngr.domain.service.sysrole.ZenSysRoleService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Oscar on 2016/10/12.
 */
@RestController
@RequestMapping("/Sysmngr/SysRoleData")
public class SysRoleDataController extends BaseController {

    @Autowired
    private ZenSysRoleService zenSysRoleService;
    @Autowired
    private ZenSysRoleMenuService zenSysRoleMenuService;

    @Override
    public UserContext createUserContext() {
        return new UserContext().setName("Sysmngr.SysRoldData");
    }

    @RequestMapping(value = "/roleQuery")
    @Zen_AppSecurity(type="数据权限")
    public IBaseResult roleQuery(HttpServletRequest request,@RequestBody Map map) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                Page<ZenSysRole> page_sysRole = zenSysRoleService.queryList(getPageableFromRequest(map, "glt_角色列表",SortConfig.BAE003_DESC_AND_ID_DESC));
//                Page<ZenSysRole> page_sysRole = zenSysRoleService.queryList(getPageableFromRequest(map, "glt_角色列表"));
                or.addGlt("glt_角色列表", page_sysRole);
            }
        });
    }

    @RequestMapping(value = "/roleget/{roleid}")
    public IBaseResult roleGet(HttpServletRequest request, @PathVariable String roleid) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                ZenSysRole zenSysRole = zenSysRoleService.get(roleid);
                List<ZenSysRoleMenu> list_menu = zenSysRoleMenuService.queryRoleIdList(roleid);
                or.addGt("gt_角色信息", zenSysRole);
                or.addGlt("glt_角色权限", list_menu);
            }
        });
    }

    @RequestMapping(value = "/roleSave")
    public IBaseResult roleSave(HttpServletRequest request, @RequestBody ZenSysRole role) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                zenSysRoleService.save(cr, role);

                String roleId = role.getId();
                or.addData("roleId", roleId);
            }
        });
    }


    @RequestMapping(value = "/roleSaveMenu/{roleid}")
    public IBaseResult roleSaveMenu(HttpServletRequest request, @RequestBody List<ZenSysRoleMenu> list_menu, @PathVariable String roleid) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                zenSysRoleMenuService.deleteRoleId(roleid);
                for (ZenSysRoleMenu zenSysRoleMenu : list_menu) {
                    zenSysRoleMenu.setRoleId(roleid);
                    zenSysRoleMenuService.save(cr, zenSysRoleMenu);
                }
            }
        });
    }

    @RequestMapping(value = "/roleDelete")
    public IBaseResult roleDelete(HttpServletRequest request, @RequestBody List<String> list_roleid) throws Exception {
        return runController(new IControllRunner() {
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                for (String roleid : list_roleid) {

                    if(zenSysRoleService.isExitUser(roleid)){
                        cr.setErrorMsg(new StringBuffer("该角色已绑定用户，不可删除!"));
                        cr.setSuccess(false);
                        return;
                    }

                    zenSysRoleMenuService.deleteRoleId(roleid);
                    zenSysRoleService.delete(cr, roleid);
                }
            }
        });
    }

}

package frame;//package frame;
//
//import app.Application;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import org.testng.annotations.Test;
//import org.zen.frame.base.domain.exception.CheckResultException;
//import org.zen.modules.sysmngr.domain.dao.sysuser.SysUserRepository;
//import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRole;
//import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysRoleMenu;
//import org.zen.modules.sysmngr.domain.model.po.sysrole.ZenSysUserRole;
//import org.zen.modules.sysmngr.domain.model.po.sysuser.SysUser;
//import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysRoleMenuRepository;
//import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysRoleRepository;
//import org.zen.modules.sysmngr.domain.repository.sysrole.ZenSysUserRoleRepository;
//
//import java.sql.SQLException;
//
//@WebAppConfiguration    //因为用到了 Scope为session 的Bean,所以测试的时候需要增加该参数，否则会报错： No Scope registered for scope name 'session'
//@SpringApplicationConfiguration(classes = Application.class)
//@Transactional(propagation = Propagation.NEVER)     //此处需要设置为不使用事务，否则并发会造成死锁（原因是 一个事务在修改A记录，删除B记录，另一个事务很可能也在修改或删除 A、B记录的某一条，此时就会锁住）
//@Rollback(value = false)    //设置不将测试数据自动回滚，不然数据库中看不到 增删改的效果
//public class InitDatas extends AbstractTransactionalTestNGSpringContextTests {
//
//    @Autowired
//    private ZenSysRoleRepository dao_role;
//    @Autowired
//    private ZenSysRoleMenuRepository dao_menu;
//    @Autowired
//    private SysUserRepository dao_sysUser;
//    @Autowired
//    private ZenSysUserRoleRepository dao_user_role;
//
//    @Test
//    public void init() throws InterruptedException, SQLException {
////        try {
////            initAdminUser();
////        } catch (CheckResultException e) {
////            e.printStackTrace();
////        }
//
//    }
//
//    private void initAdminUser() throws CheckResultException {
////        CheckResult cr = new CheckResult();
//        SysUser user = null;
//        ZenSysRole role = null;
//        ZenSysRoleMenu menu = null;
//        ZenSysUserRole user_role = null;
//
//        //role
//        role = new ZenSysRole();
//        role.setName("admin");
//        dao_role.save(role);
//
//        //menu
//        menu = new ZenSysRoleMenu();
//        menu.setMenuName("用户管理");
//        menu.setMenuId("0501");
//        menu.setRoleId(role.getId());
//        dao_menu.save(menu);
//
//        menu = new ZenSysRoleMenu();
//        menu.setMenuName("角色管理");
//        menu.setMenuId("0502");
//        menu.setRoleId(role.getId());
//        dao_menu.save(menu);
//
//        //user
//        user = new SysUser();
//        user.setLoginName("admin");
//        user.setName("admin");
//        user.setPassword("px7nc2321qzsxr0f297a57a5a743894a0e4a801fc39x6221bzxz");       //密码为admin
//        dao_sysUser.save(user);
//
//        //user_role
//        user_role = new ZenSysUserRole();
//        user_role.setUserId(user.getId());
//        user_role.setRoleId(role.getId());
//        dao_user_role.save(user_role);
//
//    }
//
//
//
//}
//
//

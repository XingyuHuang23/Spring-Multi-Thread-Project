package org.zen.modules.login.usercontext;

import app.frame.vendor.spring.springmvc.common.usercontext.BaseController;
import app.frame.vendor.spring.springmvc.common.usercontext.IControllRunner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.MediaType;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zen.frame.base.domain.context.UserContext;
import org.zen.frame.base.domain.exception.CheckResultException;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IBaseResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.frame.vendor.spring.springmvc.service.CurUserService;
import org.zen.modules.login.domain.model.vo.LoginUserFrontVO;
import org.zen.modules.login.domain.model.vo.LoginVO;
import org.zen.modules.login.domain.service.LoginService;
import org.zen.modules.security.modules.identifyingcode.domain.model.vo.IIdentifyingCodeVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 已停止维护，尽量不要再使用
 */
@ConditionalOnProperty(name="app.security.loginType", havingValue="jsonLogin")
@RestController
@RequestMapping(value="/Login",method= RequestMethod.POST)
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "/Login", description = "用户登录接口",produces= MediaType.APPLICATION_JSON_UTF8_VALUE,consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController extends BaseController {
    @Override
    public UserContext createUserContext() {
        return new UserContext().setName("Login");
    }

    @Autowired
    private LoginService loginService;

    @Autowired
    private CurUserService curUserService;


//    @PostConstruct
//    private void init() {
//    }


    @RequestMapping("/login")
    public IBaseResult login(final  HttpServletRequest request, final HttpSession httpSession, @RequestBody final LoginVO vo) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                LoginUserFrontVO userReq = vo.getLoginUser();
//                IIdentifyingCodeVO identifyingCodeVO = vo.getIdentifyingCodeVO();
                IIdentifyingCodeVO identifyingCodeVO = null;
                loginService.login(cr, request, httpSession, userReq.getUsername(), userReq.getPassword(), identifyingCodeVO);
                or.addData("loginUser", cr.getOrNewDbs().get("loginUserPublic"));
            }
        });
    }

    @ApiOperation(value = "简单登陆接口", notes = "只用传递用户登录对象即可")
    @ApiResponses({
            @ApiResponse(response = OutResult.class, code = 200, message = "请求成功  \ndatas.list为返回的数据集合")
    })
    @RequestMapping("/loginS")
    public IBaseResult login_Simple(final  HttpServletRequest request, final HttpSession httpSession, @RequestBody LoginUserFrontVO userReq) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                loginService.login(cr, request, httpSession, userReq.getUsername(), userReq.getPassword(), null);
            }
        });
    }

    @RequestMapping("/logout")
    public IBaseResult logout(final  HttpServletRequest request, final HttpSession httpSession) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                loginService.logout(or,cr, request, httpSession);
            }
        });
    }

    @RequestMapping("/getLoginUser")
    public IBaseResult getLoginUser(final  HttpServletRequest request, final HttpSession httpSession) throws Exception {
        return runController(new IControllRunner() {
            @Override
            public void run(IOutResult or, CheckResult cr) throws TransactionSystemException, CheckResultException {
                or.addData("loginUser",curUserService.getLoginUser());
            }
        });
    }

}

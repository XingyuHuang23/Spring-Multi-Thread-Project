package org.zen.modules.login.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.zen.frame.base.domain.exception.CheckResultException;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.func.encrypt.md5.Md5Util;
import org.zen.frame.global.AppProperty;
import org.zen.frame.global.Config;
import org.zen.frame.global.ErrorConfig;
import org.zen.frame.vendor.spring.springcore.event.session.SessionCreateEvent;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.vo.LoginUser_Private;
import org.zen.frame.vendor.spring.springdata.jpa.domain.model.vo.LoginUser_Public;
import org.zen.frame.vendor.spring.springmvc.common.domain.service.BaseService;
import org.zen.modules.login.util.PWUtil;
import org.zen.modules.security.modules.identifyingcode.domain.model.vo.IIdentifyingCodeVO;
import org.zen.modules.security.modules.identifyingcode.domain.service.IIdentifyingCodeService;
import org.zen.modules.sysmngr.domain.dao.sysuser.SysUserRepository;
import org.zen.modules.sysmngr.domain.model.po.sysuser.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoginService extends BaseService<SysUser> implements ILoginService {

    @Autowired
    private SysUserRepository dao;

    @Override
    public SysUserRepository getDao() {
        return dao;
    }

    @Autowired(required = false)
    private IIdentifyingCodeService identifyingCodeService;

    @Autowired
    private AppProperty ap;

    @Autowired
    private ApplicationEventPublisher publisher;




    @Override
    public CheckResult login(CheckResult cr, HttpServletRequest request, HttpSession httpSession, String loginName, String password, IIdentifyingCodeVO identifyingCodeVO) throws TransactionSystemException, CheckResultException {

//        String dealPW = restorePw(password);
//        System.out.println(RsaUtil.decode(ap.getSecurity().getKeyStoreOpt(), dealPW));

        //处理验证码
        identifyingCodeService = null;
        if (identifyingCodeService == null) {
        } else {
            identifyingCodeService.verify(cr, httpSession, identifyingCodeVO);

            if (!cr.isSuccess()) {
                cr.setSuccess(false);
                cr.getErrorMsg().append("安全错误：校验码不正确");
                return cr;
            }
        }

        //处理登录
        SysUser su = dao.findOneByLoginName(loginName);
        if (su == null) {
            cr.setSuccess(false);
            cr.getErrorMsg().append("登录失败！用户名或密码有误");
            cr.addErrorCode(ErrorConfig.安全_登陆账户或密码有误_Code);
            return cr;
        } else {
            if (PWUtil.restorePwFromDB(su.getId(),su.getPassword()).equals(PWUtil.restorePwFromFront(password))) {
                loginSuccessDeal(cr,request,httpSession,su,publisher);
            } else {
                cr.setSuccess(false);
                cr.getErrorMsg().append("登录失败！用户名或密码有误");
                cr.addErrorCode(ErrorConfig.安全_登陆账户或密码有误_Code);
                return cr;
            }
        }


        return cr;
    }

    public static void loginSuccessDeal(CheckResult cr, HttpServletRequest request, HttpSession httpSession,SysUser su,ApplicationEventPublisher publisher){
        //销毁当前session
        if (httpSession != null) {
            httpSession.invalidate();
        }
        //创建新session
        httpSession = request.getSession(true);
        cr.getOrNewDbs().set("session",httpSession);

        //登陆用户_public
        LoginUser_Public loginUserPublic = new LoginUser_Public();
        loginUserPublic.setName(su.getName());
        loginUserPublic.setHash(Md5Util.getMD5(su.getId() + "zen"));
        httpSession.setAttribute(Config.LoginUser_Session, loginUserPublic);
        cr.getOrNewDbs().set("loginUserPublic",loginUserPublic);

        //登陆用户_private
        LoginUser_Private loginUserPrivate = new LoginUser_Private();
        loginUserPrivate.setName(su.getName());
        loginUserPrivate.setId(su.getId());
        loginUserPrivate.setPassword(su.getPassword());
        loginUserPrivate.setLoginName(su.getLoginName());
        httpSession.setAttribute(Config.LoginUser_Private_Session, loginUserPrivate);

        //初始化应用数据Map
        httpSession.setAttribute(Config.AppDataMap_Session,new HashMap<String,Object>());

        //发出事件
        publisher.publishEvent(new SessionCreateEvent().setSession(httpSession));
    }

    @Override
    public CheckResult logout(IOutResult or,CheckResult cr, HttpServletRequest request, HttpSession httpSession) throws TransactionSystemException, CheckResultException {
        if(httpSession != null){
            httpSession.invalidate();
        }

        if(ap != null && ap.getConfig() != null && ap.getConfig().logoutUrl() != null && !"".equals(ap.getConfig().logoutUrl())){
            or.addData("logoutUrl",ap.getConfig().logoutUrl());
        }

        return cr;
    }
}

package com.haiyisec._test;

import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.base.domain.obj.OutResult;
import org.zen.modules.securitycheck.annotation.Zen_AppSecurity;
import org.zen.modules.securitycheck.service.IAppSecurityService;

/**
 *  Created by HB on 2018-08-15.
 */
//@todo: 用作探索，以后删除

@Service
public class MyAppSecurityService implements IAppSecurityService {
    @Override
    public IOutResult check(Zen_AppSecurity appSecurity,Object[] args) {
//        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        HttpServletRequest request = (HttpServletRequest)args[0];
//        System.out.println(request.getHeader("xxx"));
//
        OutResult or = new OutResult();
//        if(appSecurity.type().equals("操作权限")){
//            System.out.println(appSecurity.auths());
//            if(user.getLoginName().equals("admin")){
//                return or;
//            }else{
//                or.setSuccess(false);
//                or.setMsg("xxx");
//                return or;
//            }
//        }
//        if(appSecurity.type().equals("数据权限")){
//            if(user.getLoginName().equals("123")){
//                return or;
//            }else{
//                or.setSuccess(false);
//                return or;
//            }
//        }

        return or;
    }
}

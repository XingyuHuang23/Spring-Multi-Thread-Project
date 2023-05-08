package org.zen.modules.login.domain.model.vo;

/**
 * Created by HB on 2015/12/9.
 */
public class LoginVO {

    private LoginUserFrontVO loginUser;

    public LoginUserFrontVO getLoginUser() {
        return loginUser;
    }

    public LoginVO setLoginUser(LoginUserFrontVO loginUser) {
        this.loginUser = loginUser;
        return this;
    }
}

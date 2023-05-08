package org.zen.modules.login.domain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by HB on 2016-09-28.
 */
@Getter
@Setter
@ToString
@ApiModel(value = "登陆用户VO")
public class LoginUserFrontVO {
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "加密后的密码")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

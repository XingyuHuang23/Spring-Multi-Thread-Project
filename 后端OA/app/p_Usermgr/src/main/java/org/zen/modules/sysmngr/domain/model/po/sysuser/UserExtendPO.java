package org.zen.modules.sysmngr.domain.model.po.sysuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "UserExtend")
@Getter
@Setter
@Where(clause = "has_del='0'")

/**
 * 用户扩展表
 */
public class UserExtendPO extends SysUser {
    public UserExtendPO() {

    }

    public UserExtendPO(String id) {
        this.setId(id);
    }

    public UserExtendPO(String name, String loginName, String password) {
        this.setName(name);
        this.setLoginName(loginName);
        this.setPassword("1");
        this.pwd = password;
    }

    @Column(
//            nullable = false,
            length = 1000
    )
    @JsonIgnore
    private String pwd;
}

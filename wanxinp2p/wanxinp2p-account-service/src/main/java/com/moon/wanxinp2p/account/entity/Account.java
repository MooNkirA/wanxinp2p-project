package com.moon.wanxinp2p.account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 账户实体类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-19 22:10
 * @description
 */
@Data
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 118689251780731883L;

    /* 主键 */
    @TableId("ID")
    private Long id;
    /* 用户名 */
    @TableField("USERNAME")
    private String username;
    /* 手机号 */
    @TableField("MOBILE")
    private String mobile;
    /* 密码 */
    @TableField("PASSWORD")
    private String password;
    /* 加密盐 */
    @TableField("SALT")
    private String salt;
    /* 账号状态 */
    @TableField("STATUS")
    private Integer status;
    /* 域(c：c端用户；b：b端用户) */
    @TableField("DOMAIN")
    private String domain;

    @Getter
    public enum DomainEnum {
        C("c", "c端用户"),
        b("b", "b端用户");

        private final String code;
        private final String desc;

        DomainEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

}

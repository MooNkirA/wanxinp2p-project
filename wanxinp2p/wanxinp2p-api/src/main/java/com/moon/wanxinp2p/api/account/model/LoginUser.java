package com.moon.wanxinp2p.api.account.model;

import lombok.Data;

/**
 * 封装当前登录用户信息的实体类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-24 10:04
 * @description
 */
@Data
public class LoginUser {

    private String tenantId;
    private String departmentId;
    private String payload;
    private String username;
    private String mobile;
    private String userAuthorities;
    private String clientId;

}

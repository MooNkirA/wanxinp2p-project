package com.moon.wanxinp2p.account.controller;

import com.moon.wanxinp2p.account.service.AccountService;
import com.moon.wanxinp2p.api.account.AccountAPI;
import com.moon.wanxinp2p.common.domain.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一账户服务控制层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 13:45
 * @description
 */
@RestController
@Api(value = "统一账号服务", tags = "Account")
public class AccountController implements AccountAPI {

    @Autowired
    private AccountService accountService;

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     * @return
     */
    @Override
    @ApiOperation("获取手机验证码")
    @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String")
    @GetMapping("/sms/{mobile}")
    public RestResponse<Object> getSMSCode(@PathVariable String mobile) {
        return accountService.getSMSCode(mobile);
    }
}

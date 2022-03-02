package com.moon.hmilydemo.bank2.controller;

import com.moon.hmilydemo.bank2.service.AccountInfoTccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收款请求控制类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 10:16
 * @description
 */
@RestController
public class Bank2Controller {

    @Autowired
    private AccountInfoTccService accountInfoTccService;

    @RequestMapping("/transfer")
    public Boolean transfer(@RequestParam("amount") Double amount) {
        return accountInfoTccService.updateAccountBalance("2", amount);
    }

}

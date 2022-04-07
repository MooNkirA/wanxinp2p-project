package com.moon.hmilydemo.bank1.controller;

import com.moon.hmilydemo.bank1.service.AccountInfoTccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 转账请求控制类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 10:16
 * @description
 */
@RestController
public class Bank1Controller {

    @Autowired
    private AccountInfoTccService accountInfoTccService;

    @RequestMapping("/transfer")
    public String test(@RequestParam("amount") Double amount) {
        accountInfoTccService.transfer("1", amount);
        return "bank1向bank2转账:" + amount;
    }

}

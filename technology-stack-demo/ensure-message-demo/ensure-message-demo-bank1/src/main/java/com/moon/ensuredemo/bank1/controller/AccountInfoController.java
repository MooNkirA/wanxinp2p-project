package com.moon.ensuredemo.bank1.controller;

import com.moon.ensuredemo.bank1.model.AccountChangeEvent;
import com.moon.ensuredemo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountInfoController {

    @Autowired
    private AccountInfoService accountInfoService;

    @GetMapping("/transfer")
    public String transfer() {
        accountInfoService.sendUpdateAccountBalanceMsg(new AccountChangeEvent("1", 100, System.currentTimeMillis()));
        return "转账成功";
    }

}

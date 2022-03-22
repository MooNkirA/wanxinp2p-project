package com.moon.ensuredemo.bank2.service;

import com.moon.ensuredemo.bank2.model.AccountChangeEvent;

public interface AccountInfoService {
    /**
     * 更新帐号余额
     *
     * @param accountChange
     */
    void updateAccountBalance(AccountChangeEvent accountChange);
}

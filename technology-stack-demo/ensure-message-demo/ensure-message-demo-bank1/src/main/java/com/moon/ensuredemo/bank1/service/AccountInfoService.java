package com.moon.ensuredemo.bank1.service;

import com.moon.ensuredemo.bank1.model.AccountChangeEvent;

public interface AccountInfoService {

    /**
     * 更新帐号余额-发送消息
     *
     * @param accountChange
     */
    void sendUpdateAccountBalanceMsg(AccountChangeEvent accountChange);

    /**
     * 更新帐号余额-本地事务
     *
     * @param accountChange
     */
    void doUpdateAccountBalance(AccountChangeEvent accountChange);

}

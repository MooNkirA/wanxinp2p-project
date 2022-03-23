package com.moon.ensuredemo.bank1.service;

import com.moon.ensuredemo.bank1.dao.AccountInfoDao;
import com.moon.ensuredemo.bank1.message.BankMessageProducer;
import com.moon.ensuredemo.bank1.model.AccountChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private BankMessageProducer bankMessageProducer;

    @Autowired
    private AccountInfoDao accountInfoDao;

    /**
     * 更新帐号余额-发送消息
     *
     * @param accountChange
     */
    @Override
    public void sendUpdateAccountBalanceMsg(AccountChangeEvent accountChange) {
        bankMessageProducer.sendAccountChangeEvent(accountChange);
    }

    /**
     * 更新帐号余额-本地事务
     *
     * @param accountChange
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void doUpdateAccountBalance(AccountChangeEvent accountChange) {
        // 扣减余额
        accountInfoDao.subtractAccountBalance(accountChange.getAccountNo(), accountChange.getAmount());
        // 新增交易记录（与扣减余额操作在同一个事务中）
        accountInfoDao.addTx(accountChange.getTxNo());
    }
}

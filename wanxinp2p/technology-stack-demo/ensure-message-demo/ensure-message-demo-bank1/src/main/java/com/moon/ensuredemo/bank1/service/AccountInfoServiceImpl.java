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
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private BankMessageProducer bankMessageProducer;

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Override
    public void updateAccountBalance(AccountChangeEvent accountChange) {
        bankMessageProducer.sendAccountChangeEvent(accountChange);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void doUpdateAccountBalance(AccountChangeEvent accountChange) {
        accountInfoDao.subtractAccountBalance(accountChange.getAccountNo(), accountChange.getAmount());
        accountInfoDao.addTx(accountChange.getTxNo());
    }
}

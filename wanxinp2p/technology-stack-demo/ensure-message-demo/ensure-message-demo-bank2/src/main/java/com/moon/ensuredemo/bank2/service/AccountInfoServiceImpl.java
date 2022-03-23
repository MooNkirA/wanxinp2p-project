package com.moon.ensuredemo.bank2.service;

import com.moon.ensuredemo.bank2.dao.AccountInfoDao;
import com.moon.ensuredemo.bank2.model.AccountChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateAccountBalance(AccountChangeEvent accountChange) {
        log.info("bank2 工程 AccountInfoServiceImpl 执行本地事务");
        int isExistsTx = accountInfoDao.isExistTx(accountChange.getTxNo());
        if (isExistsTx == 0) {
            // 当交易记录表没有记录，才新增
            accountInfoDao.addAccountBalance(accountChange.getAccountNo(), accountChange.getAmount());
            accountInfoDao.addTx(accountChange.getTxNo());
        }
    }
}

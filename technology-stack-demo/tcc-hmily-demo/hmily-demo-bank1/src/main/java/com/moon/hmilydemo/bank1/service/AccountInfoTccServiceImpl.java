package com.moon.hmilydemo.bank1.service;

import com.moon.hmilydemo.bank1.dao.AccountInfoDao;
import com.moon.hmilydemo.bank1.feignClient.Bank2Client;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:53
 * @description
 */
@Service
public class AccountInfoTccServiceImpl implements AccountInfoTccService {

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Autowired
    private Bank2Client bank2Client;

    /**
     * 业务方法，相当于 TCC 中的 try 阶段。
     * 在此方法上需要标识 @Hmily 注解，指定成功提交与失败回滚的方法
     *
     * @param accountNo
     * @param amount
     */
    @Override
    @Hmily(confirmMethod = "commit", cancelMethod = "rollback")
    public void transfer(String accountNo, double amount) {
        System.out.println("******** Bank1 Service transfer begin...  ");
        // 执行账户扣减方法
        accountInfoDao.subtractAccountBalance(accountNo, amount);

        // 远程调用 bank2 收款方法
        if (!bank2Client.transfer(amount)) {
            throw new RuntimeException("bank2 exception");
        }
    }

    /**
     * 成功确认方法，在 try 阶段成功后执行
     *
     * @param accountNo
     * @param amount
     */
    @Override
    public void commit(String accountNo, double amount) {
        System.out.println("******** Bank1 Service commit...");
    }

    /**
     * 失败回滚方法，在 try 阶段出现异常后执行
     *
     * @param accountNo
     * @param amount
     */
    @Override
    public void rollback(String accountNo, double amount) {
        // 转账失败，调用账户增加方法
        accountInfoDao.addAccountBalance(accountNo, amount);
        System.out.println("******** Bank1 Service rollback...  ");
    }
}

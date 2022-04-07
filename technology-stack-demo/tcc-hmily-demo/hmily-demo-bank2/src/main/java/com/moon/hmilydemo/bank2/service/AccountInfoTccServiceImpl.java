package com.moon.hmilydemo.bank2.service;

import com.moon.hmilydemo.bank2.dao.AccountInfoDao;
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

    /**
     * 业务方法，相当于 TCC 中的 try 阶段。
     * 在此方法上需要标识 @Hmily 注解，指定成功提交与失败回滚的方法
     *
     * @param accountNo
     * @param amount
     */
    @Override
    @Transactional  // 本地事务，hmily 只会回滚远程调用时发现异常的事务。这里还是要处理本地事务
    @Hmily(confirmMethod = "commit", cancelMethod = "rollback")
    public Boolean updateAccountBalance(String accountNo, double amount) {
        System.out.println("******** Bank2 Service updateAccountBalance begin...  ");
        // 执行账户增加方法
        accountInfoDao.addAccountBalance(accountNo, amount);
        System.out.println("******** insert...  ");

        // 模拟出现异常
        if (Double.compare(amount, 44) == 0) {
            throw new RuntimeException("模拟异常！！！");
        }
        return true;
    }

    /**
     * 成功确认方法，在 try 阶段成功后执行
     *
     * @param accountNo
     * @param amount
     */
    @Override
    public Boolean commit(String accountNo, double amount) {
        System.out.println("******** Bank2 Service commit...");
        return true;
    }

    /**
     * 失败回滚方法，在 try 阶段出现异常后执行
     *
     * @param accountNo
     * @param amount
     */
    @Override
    public Boolean rollback(String accountNo, double amount) {
        // 在更新后失败，调用账户扣减方法
        accountInfoDao.subtractAccountBalance(accountNo, amount);
        System.out.println("******** Bank2 Service rollback...  ");
        return true;
    }
}

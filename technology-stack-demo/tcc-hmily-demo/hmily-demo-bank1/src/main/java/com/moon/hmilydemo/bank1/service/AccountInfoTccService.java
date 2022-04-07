package com.moon.hmilydemo.bank1.service;

/**
 * 业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:51
 * @description
 */
public interface AccountInfoTccService {

    void transfer(String accountNo, double amount);

    void commit(String accountNo, double amount);

    void rollback(String accountNo, double amount);

}

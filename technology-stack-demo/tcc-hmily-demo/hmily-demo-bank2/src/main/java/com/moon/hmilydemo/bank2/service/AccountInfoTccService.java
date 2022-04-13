package com.moon.hmilydemo.bank2.service;

/**
 * 业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:51
 * @description
 */
public interface AccountInfoTccService {

    Boolean updateAccountBalance(String accountNo, double amount);

    Boolean commit(String accountNo, double amount);

    Boolean rollback(String accountNo, double amount);

}

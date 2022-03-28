package com.moon.wanxinp2p.depository.service;

import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;

/**
 * 银行用户业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-14 23:16
 * @description
 */
public interface BankConsumerService {

    /**
     * 远程调用存管系统获取用户余额信息
     *
     * @param userNo 用户编码
     * @return
     */
    BalanceDetailsDTO getBalance(String userNo);

}

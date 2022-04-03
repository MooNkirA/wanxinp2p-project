package com.moon.wanxinp2p.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.api.consumer.model.BankCardDTO;
import com.moon.wanxinp2p.consumer.entity.BankCard;

/**
 * 用户绑定银行卡信息 服务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 11:50
 * @description
 */
public interface BankCardService extends IService<BankCard> {

    /**
     * 获取银行卡信息
     *
     * @param consumerId 用户id
     * @return
     */
    BankCardDTO getByConsumerId(Long consumerId);

    /**
     * 获取银行卡信息
     *
     * @param cardNumber 卡号
     * @return
     */
    BankCardDTO getByCardNumber(String cardNumber);

}

package com.moon.wanxinp2p.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.BankCardDTO;
import com.moon.wanxinp2p.consumer.entity.BankCard;
import com.moon.wanxinp2p.consumer.mapper.BankCardMapper;
import com.moon.wanxinp2p.consumer.service.BankCardService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户绑定银行卡信息 服务实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 14:16
 * @description
 */
@Service
public class BankCardServiceImpl extends ServiceImpl<BankCardMapper, BankCard> implements BankCardService {

    /**
     * 获取银行卡信息
     *
     * @param consumerId 用户id
     * @return
     */
    @Override
    public BankCardDTO getByConsumerId(Long consumerId) {
        BankCard bankCard = this.getOne(new QueryWrapper<BankCard>().lambda().eq(BankCard::getConsumerId, consumerId));
        return convertEntityToDTO(bankCard);
    }

    /**
     * 获取银行卡信息
     *
     * @param cardNumber 卡号
     * @return
     */
    @Override
    public BankCardDTO getByCardNumber(String cardNumber) {
        BankCard bankCard = this.getOne(new QueryWrapper<BankCard>().lambda().eq(BankCard::getCardNumber, cardNumber));
        return convertEntityToDTO(bankCard);
    }

    private BankCardDTO convertEntityToDTO(BankCard bankCard) {
        if (bankCard == null) {
            return null;
        }
        BankCardDTO bankCardDTO = new BankCardDTO();
        BeanUtils.copyProperties(bankCard, bankCardDTO);
        return bankCardDTO;
    }

}

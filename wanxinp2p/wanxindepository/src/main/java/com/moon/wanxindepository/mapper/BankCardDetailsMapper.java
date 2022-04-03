package com.moon.wanxindepository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.wanxindepository.entity.BankCardDetails;
import org.springframework.stereotype.Repository;

/**
 * 银行卡明细 Mapper 接口
 */
@Repository
public interface BankCardDetailsMapper extends BaseMapper<BankCardDetails> {
    /**
     * 根据银行卡ID获取余额
     *
     * @param bankCardId
     * @return
     */
    BankCardDetails selectByBankCardId(Long bankCardId);
}

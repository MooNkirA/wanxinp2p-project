package com.moon.wanxindepository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.wanxindepository.entity.BankCard;
import org.springframework.stereotype.Repository;

/**
 * 银行用户银行卡信息 Mapper 接口
 */
@Repository
public interface BankCardMapper extends BaseMapper<BankCard> {

//	Page<BankCard> queryBankCards(Page page, @Param(value = "card") BankCard bankCard);

}

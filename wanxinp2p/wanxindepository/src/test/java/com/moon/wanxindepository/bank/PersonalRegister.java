package com.moon.wanxindepository.bank;

import com.moon.wanxindepository.entity.BankCard;
import com.moon.wanxindepository.model.BankCardRequest;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * 生成用户开户测试数据
 */
public class PersonalRegister {
    public static void main(String[] args) {
        BankCardRequest bankCardRequest = new BankCardRequest();
        bankCardRequest.setFullname(ChineseNameGenerator.getInstance().generate());
        bankCardRequest.setIdNumber(ChineseIDCardNumberGenerator.getInstance().generate());
        bankCardRequest.setMobile(ChineseMobileNumberGenerator.getInstance().generate());
        bankCardRequest.setBalance(new BigDecimal(100000));
        BankCard bankCard = BankCardNumberGenerator.getInstance().generateAll();
        BeanUtils.copyProperties(bankCard, bankCardRequest);
        bankCardRequest.setPassword("888888");
        bankCardRequest.setUserType(1);
        System.out.println(bankCardRequest);
    }
}

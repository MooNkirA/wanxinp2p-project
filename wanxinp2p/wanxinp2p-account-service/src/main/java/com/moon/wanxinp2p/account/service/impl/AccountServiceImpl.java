package com.moon.wanxinp2p.account.service.impl;

import com.moon.wanxinp2p.account.service.AccountService;
import com.moon.wanxinp2p.account.service.SmsService;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 统一账户业务实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 22:47
 * @description
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private SmsService smsService;

    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     * @return
     */
    @Override
    public RestResponse<Object> getSMSCode(String mobile) {
        return smsService.getSMSCode(mobile);
    }

}

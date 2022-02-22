package com.moon.wanxinp2p.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.account.entity.Account;
import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountLoginDTO;
import com.moon.wanxinp2p.api.account.model.AccountRegisterDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 统一账户业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 22:46
 * @description
 */
public interface AccountService extends IService<Account> {

    RestResponse<Object> getSMSCode(String mobile);

    Integer checkMobile(String mobile, String key, String code);

    AccountDTO register(AccountRegisterDTO accountRegisterDTO);

    AccountDTO login(AccountLoginDTO accountLoginDTO);
}

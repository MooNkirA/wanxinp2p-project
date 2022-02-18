package com.moon.wanxinp2p.account.service;

import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 统一账户业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 22:46
 * @description
 */
public interface AccountService {

    RestResponse<Object> getSMSCode(String mobile);

}

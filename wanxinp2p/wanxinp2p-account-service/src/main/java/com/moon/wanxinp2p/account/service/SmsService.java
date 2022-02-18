package com.moon.wanxinp2p.account.service;

import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 短信服务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 16:58
 * @description
 */
public interface SmsService {

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     * @return
     */
    RestResponse<Object> getSMSCode(String mobile);

}

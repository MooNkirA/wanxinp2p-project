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

    /**
     * 校验验证码
     *
     * @param key  校验标识 redis中的键
     * @param code 短信验证码
     */
    void verifySmsCode(String key, String code);

}

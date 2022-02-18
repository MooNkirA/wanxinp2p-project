package com.moon.wanxinp2p.api.account;

import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 统一账号 API 接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 16:43
 * @description
 */
public interface AccountAPI {

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     * @return
     */
    RestResponse<Object> getSMSCode(String mobile);


}

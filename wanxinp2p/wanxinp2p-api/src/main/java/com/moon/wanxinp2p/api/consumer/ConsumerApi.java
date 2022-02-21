package com.moon.wanxinp2p.api.consumer;

import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 用户中心接口API
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 17:45
 * @description
 */
public interface ConsumerApi {

    /**
     * 用户注册  保存用户信息
     *
     * @param consumerRegisterDTO
     * @return
     */
    RestResponse register(ConsumerRegisterDTO consumerRegisterDTO);

}

package com.moon.wanxinp2p.api.consumer;

import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.lang.Nullable;

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
    RestResponse<Nullable> register(ConsumerRegisterDTO consumerRegisterDTO);

    /**
     * 生成开户请求数据
     *
     * @param consumerRequest 开户信息
     * @return
     */
    RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest);

    /**
     * 根据手机号获得当前登录用户
     *
     * @param mobile
     * @return
     */
    RestResponse<ConsumerDTO> getCurrConsumer(String mobile);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    RestResponse<ConsumerDTO> getMyConsumer();

}

package com.moon.wanxinp2p.transaction.agent;

import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务 feign 远程调用接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-05 17:36
 * @description
 */
@FeignClient("consumer-service") // 服务 id
public interface ConsumerApiAgent {

    /**
     * 根据手机号获得当前登录用户
     *
     * @param mobile
     * @return
     */
    @GetMapping("/consumer/l/currConsumer/{mobile}")
    RestResponse<ConsumerDTO> getCurrConsumer(@PathVariable("mobile") String mobile);

}

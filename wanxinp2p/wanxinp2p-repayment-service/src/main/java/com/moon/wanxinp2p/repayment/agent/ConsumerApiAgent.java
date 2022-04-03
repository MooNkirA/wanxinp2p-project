package com.moon.wanxinp2p.repayment.agent;

import com.moon.wanxinp2p.api.consumer.model.BorrowerDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务 feign 代理接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-31 10:56
 * @description
 */
@FeignClient(value = "consumer-service")
public interface ConsumerApiAgent {

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/consumer/l/borrowers/{id}")
    RestResponse<BorrowerDTO> getBorrowerMobile(@PathVariable("id") Long id);

}

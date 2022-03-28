package com.moon.wanxinp2p.repayment.agent;

import com.moon.wanxinp2p.api.depository.model.UserAutoPreTransactionRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 存管代理服务 feign 远程调用接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-27 15:34
 * @description
 */
@FeignClient("depository-agent-service")
public interface DepositoryAgentApiAgent {

    /**
     * 银行存管预处理
     *
     * @param userAutoPreTransactionRequest
     * @return
     */
    @PostMapping("/depository-agent/l/user-auto-pre-transaction")
    RestResponse<String> userAutoPreTransaction(UserAutoPreTransactionRequest userAutoPreTransactionRequest);

}

package com.moon.wanxinp2p.repayment.agent;

import com.moon.wanxinp2p.api.depository.model.UserAutoPreTransactionRequest;
import com.moon.wanxinp2p.api.repayment.model.RepaymentRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 存管代理服务 feign 远程调用接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-27 15:34
 * @description
 */
@FeignClient(name = "depository-agent-service", path = "/depository-agent")
public interface DepositoryAgentApiAgent {

    /**
     * 银行存管预处理
     *
     * @param userAutoPreTransactionRequest
     * @return
     */
    @PostMapping("/l/user-auto-pre-transaction")
    RestResponse<String> userAutoPreTransaction(@RequestBody UserAutoPreTransactionRequest userAutoPreTransactionRequest);

    /**
     * 银行存管确认还款
     *
     * @param repaymentRequest
     * @return
     */
    @PostMapping("/l/confirm-repayment")
    RestResponse<String> confirmRepayment(@RequestBody RepaymentRequest repaymentRequest);

}

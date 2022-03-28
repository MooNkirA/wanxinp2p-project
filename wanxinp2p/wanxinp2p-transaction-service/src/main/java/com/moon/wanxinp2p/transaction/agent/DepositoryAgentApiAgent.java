package com.moon.wanxinp2p.transaction.agent;

import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;
import com.moon.wanxinp2p.api.depository.model.LoanRequest;
import com.moon.wanxinp2p.api.depository.model.UserAutoPreTransactionRequest;
import com.moon.wanxinp2p.api.transaction.model.ModifyProjectStatusDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 存管代理服务 feign 远程调用接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-07 21:51
 * @description
 */
// path 属性用于定义该接口中所有方法的请求前缀
@FeignClient(value = "depository-agent-service", path = "/depository-agent")
public interface DepositoryAgentApiAgent {

    @PostMapping(value = "/l/createProject")
    RestResponse<String> createProject(@RequestBody ProjectDTO projectDTO);

    @GetMapping("/l/balances/{userNo}")
    RestResponse<BalanceDetailsDTO> getBalance(@PathVariable("userNo") String userNo);

    @PostMapping("/l/user-auto-pre-transaction")
    RestResponse<String> userAutoPreTransaction(@RequestBody UserAutoPreTransactionRequest userAutoPreTransactionRequest);

    @PostMapping("/l/confirm-loan")
    RestResponse<String> confirmLoan(@RequestBody LoanRequest loanRequest);

    @PostMapping("/l/modify-project-status")
    RestResponse<String> modifyProjectStatus(@RequestBody ModifyProjectStatusDTO modifyProjectStatusDTO);
}

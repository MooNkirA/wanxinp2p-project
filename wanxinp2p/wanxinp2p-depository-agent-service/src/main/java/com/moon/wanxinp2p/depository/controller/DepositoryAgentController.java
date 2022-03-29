package com.moon.wanxinp2p.depository.controller;

import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.DepositoryAgentApi;
import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;
import com.moon.wanxinp2p.api.depository.model.DepositoryBaseResponse;
import com.moon.wanxinp2p.api.depository.model.DepositoryResponseDTO;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.api.depository.model.LoanRequest;
import com.moon.wanxinp2p.api.depository.model.UserAutoPreTransactionRequest;
import com.moon.wanxinp2p.api.repayment.model.RepaymentRequest;
import com.moon.wanxinp2p.api.transaction.model.ModifyProjectStatusDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.depository.service.BankConsumerService;
import com.moon.wanxinp2p.depository.service.DepositoryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 存管代理服务控制层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-28 10:09
 * @description
 */
@Api(value = "存管代理服务", tags = "depository-agent")
@RestController
public class DepositoryAgentController implements DepositoryAgentApi {

    @Autowired
    private DepositoryRecordService depositoryRecordService;

    @Autowired
    private BankConsumerService bankConsumerService;

    /**
     * 开通存管账户
     *
     * @param consumerRequest 开户信息
     * @return
     */
    @ApiOperation("生成开户请求数据")
    @ApiImplicitParam(name = "consumerRequest", value = "开户信息",
            required = true, dataType = "ConsumerRequest", paramType = "body")
    @PostMapping("/l/consumers")
    @Override
    public RestResponse<GatewayRequest> createConsumer(@RequestBody ConsumerRequest consumerRequest) {
        return RestResponse.success(depositoryRecordService.createConsumer((consumerRequest)));
    }

    /**
     * 向银行存管系统发送标的信息
     *
     * @param projectDTO
     * @return
     */
    @ApiOperation(value = "向存管系统发送标的信息")
    @ApiImplicitParam(name = "projectDTO", value = "向存管系统发送标的信息",
            required = true, dataType = "ProjectDTO", paramType = "body")
    @PostMapping("/l/createProject")
    @Override
    public RestResponse<String> createProject(@RequestBody ProjectDTO projectDTO) {
        return getRestResponse(depositoryRecordService.createProject(projectDTO));
    }

    /**
     * 获取当前登录用户余额信息
     *
     * @param userNo 用户编码
     * @return
     */
    @ApiOperation("获取用户可用余额")
    @ApiImplicitParam(name = "userNo", value = "用户编码", required = true, dataType = "String")
    @GetMapping("/l/balances/{userNo}")
    @Override
    public RestResponse<BalanceDetailsDTO> getBalance(@PathVariable String userNo) {
        return RestResponse.success(bankConsumerService.getBalance(userNo));
    }

    /**
     * 预授权处理
     *
     * @param userAutoPreTransactionRequest 预授权处理信息
     * @return
     */
    @ApiOperation(value = "预授权处理")
    @ApiImplicitParam(name = "userAutoPreTransactionRequest", value = "平台向存管系统发送标的信息",
            required = true, dataType = "UserAutoPreTransactionRequest", paramType = "body")
    @PostMapping("/l/user-auto-pre-transaction")
    @Override
    public RestResponse<String> userAutoPreTransaction(@RequestBody UserAutoPreTransactionRequest userAutoPreTransactionRequest) {
        return getRestResponse(depositoryRecordService.userAutoPreTransaction(userAutoPreTransactionRequest));
    }

    /**
     * 统一处理响应信息
     *
     * @param depositoryResponse
     * @return
     */
    private RestResponse<String> getRestResponse(DepositoryResponseDTO<DepositoryBaseResponse> depositoryResponse) {
        // 获取响应数据
        DepositoryBaseResponse respData = depositoryResponse.getRespData();
        // 设置响应结果和响应信息
        final RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setResult(respData.getRespCode());
        restResponse.setMsg(respData.getRespMsg());
        return restResponse;
    }

    /**
     * 审核标的满标放款
     *
     * @param loanRequest
     * @return
     */
    @ApiOperation(value = "审核标的满标放款")
    @ApiImplicitParam(name = "loanRequest", value = "标的满标放款信息", required = true,
            dataType = "LoanRequest", paramType = "body")
    @PostMapping("l/confirm-loan")
    @Override
    public RestResponse<String> confirmLoan(@RequestBody LoanRequest loanRequest) {
        return getRestResponse(depositoryRecordService.confirmLoan(loanRequest));
    }

    /**
     * 修改标的状态
     *
     * @param modifyProjectStatusDTO
     * @return
     */
    @ApiOperation(value = "修改标的状态")
    @ApiImplicitParam(name = "modifyProjectStatusDTO", value = "修改标的状态DTO", required = true,
            dataType = "ModifyProjectStatusDTO", paramType = "body")
    @PostMapping("l/modify-project-status")
    @Override
    public RestResponse<String> modifyProjectStatus(@RequestBody ModifyProjectStatusDTO modifyProjectStatusDTO) {
        return getRestResponse(depositoryRecordService.modifyProjectStatus(modifyProjectStatusDTO));
    }

    /**
     * 还款确认
     *
     * @param repaymentRequest 还款信息
     * @return
     */
    @ApiOperation(value = "确认还款")
    @ApiImplicitParam(name = "repaymentRequest", value = "还款信息", required = true,
            dataType = "RepaymentRequest", paramType = "body")
    @PostMapping("l/confirm-repayment")
    @Override
    public RestResponse<String> confirmRepayment(@RequestBody RepaymentRequest repaymentRequest) {
        return getRestResponse(depositoryRecordService.confirmRepayment(repaymentRequest));
    }
}

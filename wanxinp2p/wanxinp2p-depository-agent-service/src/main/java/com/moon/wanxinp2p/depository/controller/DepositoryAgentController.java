package com.moon.wanxinp2p.depository.controller;

import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.DepositoryAgentApi;
import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;
import com.moon.wanxinp2p.api.depository.model.DepositoryBaseResponse;
import com.moon.wanxinp2p.api.depository.model.DepositoryResponseDTO;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.depository.service.BankConsumerService;
import com.moon.wanxinp2p.depository.service.DepositoryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
        DepositoryResponseDTO<DepositoryBaseResponse> responseDTO = depositoryRecordService.createProject(projectDTO);
        // 获取响应数据
        DepositoryBaseResponse respData = responseDTO.getRespData();
        // 设置响应结果和响应信息
        RestResponse<String> response = new RestResponse<>();
        response.setResult(respData.getRespCode());
        response.setMsg(respData.getRespMsg());
        return response;
    }

    /**
     * 获取当前登录用户余额信息
     *
     * @param userNo 用户编码
     * @return
     */
    @Override
    public RestResponse<BalanceDetailsDTO> getBalance(String userNo) {
        return RestResponse.success();
    }
}

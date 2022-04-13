package com.moon.wanxinp2p.depository.controller;

import com.alibaba.fastjson.JSON;
import com.moon.wanxinp2p.api.depository.model.DepositoryConsumerResponse;
import com.moon.wanxinp2p.api.depository.model.DepositoryRechargeResponse;
import com.moon.wanxinp2p.api.depository.model.DepositoryWithdrawResponse;
import com.moon.wanxinp2p.common.constants.ServiceNameConstants;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.moon.wanxinp2p.depository.message.GatewayMessageProducer;
import com.moon.wanxinp2p.depository.service.DepositoryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接收银行存管系统的开户结果回调通知控制类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-28 22:58
 * @description
 */
@Api(value = "银行存管系统通知服务", tags = "depository-agent")
@RestController
@Log4j2
public class DepositoryNotifyController {

    @Autowired
    private DepositoryRecordService depositoryRecordService;

    @Autowired
    private GatewayMessageProducer gatewayMessageProducer;

    @ApiOperation("接受银行存管系统开户回调结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "请求的存管接口名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformNo", value = "平台编号，平台与存管系统签约时获取", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "对reqData参数的签名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reqData", value = "业务数据报文，json格式", required = true, dataType = "String", paramType = "query")
    })
    // params 属性用于指定限制请求参数的条件
    @GetMapping(value = "/gateway", params = "serviceName=PERSONAL_REGISTER")
    public String receiveDepositoryCreateConsumerResult(@RequestParam("serviceName") String serviceName,
                                                        @RequestParam("platformNo") String platformNo,
                                                        @RequestParam("signature") String signature,
                                                        @RequestParam("reqData") String reqData) {
        // 获取银行存管系统返回的响应字符串，需要Base64解码后，再封装成 DepositoryConsumerResponse 实体
        DepositoryConsumerResponse depositoryConsumerResponse = JSON.parseObject(EncryptUtil.decodeUTF8StringBase64(reqData), DepositoryConsumerResponse.class);
        // 1.更新请求记录的状态
        depositoryRecordService.modifyRequestStatus(depositoryConsumerResponse.getRequestNo(), depositoryConsumerResponse.getStatus());
        log.info("更新请求记录状态为 {} ....", depositoryConsumerResponse.getStatus());

        // 2.发送异步消息给用户中心服务
        gatewayMessageProducer.personalRegister(depositoryConsumerResponse);

        // 3.返回结果
        return "OK";
    }

    @ApiOperation("接受银行存管系统充值返回结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "请求的银行存管接口名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformNo", value = "平台编号，平台与存管系统签约时获取", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "对reqData参数的签名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reqData", value = "业务数据报文，json格式", required = true, dataType = "String", paramType = "query"),})
    @GetMapping(value = "/gateway", params = "serviceName=" + ServiceNameConstants.NAME_RECHARGE)
    public String receiveDepositoryRechargeResult(@RequestParam("serviceName") String serviceName,
                                                  @RequestParam("platformNo") String platformNo,
                                                  @RequestParam("signature") String signature,
                                                  @RequestParam("reqData") String reqData) {
        // 获取银行存管系统返回的响应字符串，需要Base64解码后，再封装成 DepositoryConsumerResponse 实体
        DepositoryRechargeResponse rechargeResponse = JSON.parseObject(EncryptUtil.decodeUTF8StringBase64(reqData), DepositoryRechargeResponse.class);
        // 1.更新请求记录的状态
        depositoryRecordService.modifyRequestStatus(rechargeResponse.getRequestNo(), rechargeResponse.getStatus());
        log.info("更新充值返回结果请求记录状态为 {} , 交易状态为 {}", rechargeResponse.getStatus(), rechargeResponse.getTransactionStatus());

        // 2.发送异步消息给用户中心服务
        gatewayMessageProducer.recharge(rechargeResponse);

        // 3.返回结果
        return "OK";
    }

    @ApiOperation("接受银行存管系统提现返回结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "请求的存管接口名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformNo", value = "平台编号，平台与存管系统签约时获取", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "对reqData参数的签名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reqData", value = "业务数据报文，json格式", required = true, dataType = "String", paramType = "query"),})
    @GetMapping(value = "/gateway", params = "serviceName=" + ServiceNameConstants.NAME_WITHDRAW)
    public String receiveDepositoryWithdrawResult(@RequestParam("serviceName") String serviceName,
                                                  @RequestParam("platformNo") String platformNo,
                                                  @RequestParam("signature") String signature,
                                                  @RequestParam("reqData") String reqData) {
        // 获取银行存管系统返回的响应字符串，需要Base64解码后，再封装成 DepositoryConsumerResponse 实体
        DepositoryWithdrawResponse withdrawResponse = JSON.parseObject(EncryptUtil.decodeUTF8StringBase64(reqData), DepositoryWithdrawResponse.class);
        // 1.更新请求记录的状态
        depositoryRecordService.modifyRequestStatus(withdrawResponse.getRequestNo(), withdrawResponse.getStatus());
        log.info("更新提现返回结果请求记录状态为 {} , 交易状态为 {}", withdrawResponse.getStatus(), withdrawResponse.getTransactionStatus());

        // 2.发送异步消息给用户中心服务
        gatewayMessageProducer.withdraw(withdrawResponse);

        // 3.返回结果
        return "OK";
    }


}

package com.moon.wanxinp2p.depository.controller;

import com.alibaba.fastjson.JSON;
import com.moon.wanxinp2p.api.depository.model.DepositoryConsumerResponse;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.moon.wanxinp2p.depository.message.GatewayMessageProducer;
import com.moon.wanxinp2p.depository.service.DepositoryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

        // 2.发送异步消息给用户中心服务
        gatewayMessageProducer.personalRegister(depositoryConsumerResponse);

        // 3.返回结果
        return "OK";
    }

}

package com.moon.wanxinp2p.consumer.controller;

import com.moon.wanxinp2p.api.consumer.ConsumerApi;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户服务的Controller", tags = "Consumer", description = "用户服务API")
public class ConsumerController implements ConsumerApi {

    @Autowired
    private ConsumerService consumerService;

    /**
     * 用户注册  保存用户信息
     *
     * @param consumerRegisterDTO
     * @return
     */
    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "consumerRegisterDTO", value = "注册信息",
            required = true, dataType = "AccountRegisterDTO", paramType = "body")
    @PostMapping(value = "/consumers")
    @Override
    public RestResponse<Nullable> register(@RequestBody ConsumerRegisterDTO consumerRegisterDTO) {
        // 调用业务接口注册
        consumerService.register(consumerRegisterDTO);
        // 无异常，代表注册成功
        return RestResponse.success();
    }
}

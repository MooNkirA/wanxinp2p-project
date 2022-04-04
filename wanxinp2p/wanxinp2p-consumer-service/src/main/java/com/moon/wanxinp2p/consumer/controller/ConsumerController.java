package com.moon.wanxinp2p.consumer.controller;

import com.moon.wanxinp2p.api.account.model.LoginUser;
import com.moon.wanxinp2p.api.consumer.ConsumerApi;
import com.moon.wanxinp2p.api.consumer.model.BorrowerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.moon.wanxinp2p.consumer.common.util.SecurityUtil;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import com.moon.wanxinp2p.consumer.service.RechargeRecordService;
import com.moon.wanxinp2p.consumer.service.WithdrawRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户服务的Controller", tags = "Consumer", description = "用户服务API")
public class ConsumerController implements ConsumerApi {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private WithdrawRecordService withdrawRecordService;

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

    @ApiOperation("过网关受保护资源，进行认证拦截测试")
    @ApiImplicitParam(name = "jsonToken", value = "访问令牌", required = true, dataType = "String")
    @GetMapping(value = "/m/consumers/test")
    public RestResponse<String> testResources(String jsonToken) {
        return RestResponse.success(EncryptUtil.decodeUTF8StringBase64(jsonToken));
    }

    /**
     * 生成开户请求数据
     *
     * @param consumerRequest 开户信息
     * @return
     */
    @ApiOperation("生成开户请求数据")
    @ApiImplicitParam(name = "consumerRequest", value = "开户信息", required = true, dataType = "ConsumerRequest", paramType = "body")
    @PostMapping("/my/consumers")
    @Override
    public RestResponse<GatewayRequest> createConsumer(@RequestBody ConsumerRequest consumerRequest) {
        // 从 gateway 转发请求时设置的用户信息数据中，获取用户的手机号
        LoginUser user = SecurityUtil.getUser();
        consumerRequest.setMobile(user.getMobile());
        return consumerService.createConsumer(consumerRequest);
    }

    /**
     * 根据手机号获得当前登录用户（用于其他微服务远程调用）
     *
     * @param mobile
     * @return
     */
    @ApiOperation("获取登录用户信息")
    @GetMapping("/l/currConsumer/{mobile}")
    @Override
    public RestResponse<ConsumerDTO> getCurrConsumer(@PathVariable("mobile") String mobile) {
        // 调用业务层根据手机号查询方法
        return RestResponse.success(consumerService.getByMobile(mobile));
    }

    /**
     * 获取当前登录用户（用于前端直接请求用户服务）
     *
     * @return
     */
    @ApiOperation("获取登录用户信息")
    @GetMapping("/my/consumers")
    @Override
    public RestResponse<ConsumerDTO> getMyConsumer() {
        // 使用工具类，从请求域中获取到用户手机号
        ConsumerDTO dto = consumerService.getByMobile(SecurityUtil.getUser().getMobile());
        return RestResponse.success(dto);
    }

    /**
     * 获取借款人用户信息
     *
     * @param id 用户id
     * @return
     */
    @ApiOperation("获取借款人用户信息")
    @ApiImplicitParam(name = "id", value = "用户标识", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/my/borrowers/{id}")
    @Override
    public RestResponse<BorrowerDTO> getBorrower(@PathVariable Long id) {
        return RestResponse.success(consumerService.getBorrower(id));
    }

    /**
     * 获取借款人用户信息-供微服务访问
     *
     * @param id 用户标识
     * @return
     */
    @ApiOperation("获取借款人用户信息-供微服务访问")
    @ApiImplicitParam(name = "id", value = "用户标识", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/l/borrowers/{id}")
    @Override
    public RestResponse<BorrowerDTO> getBorrowerMobile(@PathVariable Long id) {
        return RestResponse.success(consumerService.getBorrower(id));
    }

    /**
     * 获取当前登录用户余额信息
     *
     * @return
     */
    @ApiOperation("获取用户可用余额")
    @GetMapping("/my/balances")
    @Override
    public RestResponse<BalanceDetailsDTO> getMyBalance() {
        // 使用工具类，从请求域中获取到用户手机号，再根据手机查询到用户数据
        ConsumerDTO dto = consumerService.getByMobile(SecurityUtil.getUser().getMobile());
        return consumerService.getBalanceFromDepository(dto.getUserNo());
    }

    /**
     * 生成充值请求数据
     *
     * @param amount      充值金额
     * @param callbackUrl 回调地址
     * @return
     */
    @ApiOperation("生成充值请求数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "金额", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "callbackURL", value = "通知结果回调Url", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/my/recharge-records")
    @Override
    public RestResponse<GatewayRequest> createRechargeRecord(@RequestParam String amount, @RequestParam String callbackUrl) {
        return rechargeRecordService.createRechargeRecord(amount, callbackUrl);
    }

    /**
     * 生成用户提现数据
     *
     * @param amount      提现金额
     * @param callbackUrl 回调地址
     * @return
     */
    @ApiOperation("生成用户提现数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "金额", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "callbackUrl", value = "通知结果回调Url", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/my/withdraw-records")
    @Override
    public RestResponse<GatewayRequest> createWithdrawRecord(@RequestParam String amount, @RequestParam String callbackUrl) {
        return withdrawRecordService.createWithdrawRecord(amount, callbackUrl);
    }
}

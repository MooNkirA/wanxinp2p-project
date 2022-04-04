package com.moon.wanxinp2p.api.consumer;

import com.moon.wanxinp2p.api.consumer.model.BorrowerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.lang.Nullable;

/**
 * 用户中心接口API
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 17:45
 * @description
 */
public interface ConsumerApi {

    /**
     * 用户注册  保存用户信息
     *
     * @param consumerRegisterDTO
     * @return
     */
    RestResponse<Nullable> register(ConsumerRegisterDTO consumerRegisterDTO);

    /**
     * 生成开户请求数据
     *
     * @param consumerRequest 开户信息
     * @return
     */
    RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest);

    /**
     * 根据手机号获得当前登录用户
     *
     * @param mobile
     * @return
     */
    RestResponse<ConsumerDTO> getCurrConsumer(String mobile);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    RestResponse<ConsumerDTO> getMyConsumer();

    /**
     * 获取借款人用户信息
     *
     * @param id 用户id
     * @return
     */
    RestResponse<BorrowerDTO> getBorrower(Long id);

    /**
     * 获取借款人用户信息-供微服务访问
     *
     * @param id 用户标识
     * @return
     */
    RestResponse<BorrowerDTO> getBorrowerMobile(Long id);

    /**
     * 获取当前登录用户余额信息
     *
     * @return
     */
    RestResponse<BalanceDetailsDTO> getMyBalance();

    /**
     * 生成充值请求数据
     *
     * @param amount      充值金额
     * @param callbackUrl 回调地址
     * @return
     */
    RestResponse<GatewayRequest> createRechargeRecord(String amount, String callbackUrl);

    /**
     * 生成用户提现数据
     *
     * @param amount      提现金额
     * @param callbackUrl 回调地址
     * @return
     */
    RestResponse<GatewayRequest> createWithdrawRecord(String amount, String callbackUrl);
}

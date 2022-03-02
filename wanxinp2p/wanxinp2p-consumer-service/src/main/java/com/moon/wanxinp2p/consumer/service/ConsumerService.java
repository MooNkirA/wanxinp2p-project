package com.moon.wanxinp2p.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.DepositoryConsumerResponse;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.consumer.entity.Consumer;

/**
 * 用户业务层接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 18:04
 * @description
 */
public interface ConsumerService extends IService<Consumer> {

    /**
     * 检测用户是否存在
     *
     * @param mobile
     * @return
     */
    Integer checkMobile(String mobile);

    /**
     * 用户注册
     *
     * @param consumerRegisterDTO
     * @return
     */
    void register(ConsumerRegisterDTO consumerRegisterDTO);

    /**
     * 生成开户数据
     *
     * @param consumerRequest
     * @return
     */
    RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest);

    /**
     * 更新开户结果
     *
     * @param response
     * @return
     */
    Boolean modifyResult(DepositoryConsumerResponse response);

}

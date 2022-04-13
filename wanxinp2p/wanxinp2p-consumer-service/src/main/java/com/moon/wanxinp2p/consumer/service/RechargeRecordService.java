package com.moon.wanxinp2p.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.api.depository.model.DepositoryRechargeResponse;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.consumer.entity.RechargeRecord;

/**
 * 充值记录业务层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-04 12:10
 * @description
 */
public interface RechargeRecordService extends IService<RechargeRecord> {

    /**
     * 生成充值请求数据
     *
     * @param amount      充值金额
     * @param callbackURL 回调地址
     * @return
     */
    RestResponse<GatewayRequest> createRechargeRecord(String amount, String callbackURL);

    /**
     * 更新充值结果
     *
     * @param response
     * @return
     */
    Boolean modifyResult(DepositoryRechargeResponse response);

}

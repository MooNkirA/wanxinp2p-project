package com.moon.wanxinp2p.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.api.depository.model.DepositoryWithdrawResponse;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.consumer.entity.WithdrawRecord;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 提现记录业务层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-04 12:10
 * @description
 */
public interface WithdrawRecordService extends IService<WithdrawRecord> {

    /**
     * 生成提现请求数据
     *
     * @param amount      提现金额
     * @param callbackUrl 回调地址
     * @return
     */
    RestResponse<GatewayRequest> createWithdrawRecord(@RequestParam String amount, @RequestParam String callbackUrl);

    /**
     * 更新提现结果
     *
     * @param response
     * @return
     */
    Boolean modifyResult(DepositoryWithdrawResponse response);

}

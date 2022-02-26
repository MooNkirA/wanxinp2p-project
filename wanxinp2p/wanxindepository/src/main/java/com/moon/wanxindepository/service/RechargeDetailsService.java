package com.moon.wanxindepository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxindepository.entity.RechargeDetails;
import com.moon.wanxindepository.model.RechargeRequest;
import com.moon.wanxindepository.model.RechargeResponse;

/**
 * 充值记录表 服务类
 */
public interface RechargeDetailsService extends IService<RechargeDetails> {

    /**
     * 用户充值
     *
     * @param rechargeRequest
     * @return
     */
    RechargeResponse recharge(RechargeRequest rechargeRequest);

}

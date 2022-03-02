package com.moon.wanxindepository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxindepository.entity.WithdrawDetails;
import com.moon.wanxindepository.model.WithdrawRequest;
import com.moon.wanxindepository.model.WithdrawResponse;

/**
 * 用户余额明细表 服务类
 */
public interface WithdrawDetailsService extends IService<WithdrawDetails> {

    /**
     * 用户提现
     *
     * @param withdrawRequest
     * @return
     */
    WithdrawResponse withDraw(WithdrawRequest withdrawRequest);

}

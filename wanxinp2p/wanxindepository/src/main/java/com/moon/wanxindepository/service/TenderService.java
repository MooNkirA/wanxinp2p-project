package com.moon.wanxindepository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxindepository.entity.Tender;
import com.moon.wanxindepository.model.ConfirmLoanResponse;
import com.moon.wanxindepository.model.UserAutoPreTransactionRequest;
import com.moon.wanxindepository.model.UserAutoPreTransactionResponse;

/**
 * 投标信息表 服务类
 */
public interface TenderService extends IService<Tender> {

    /**
     * 投标预授权处理
     *
     * @param preTransactionRequest
     * @return
     */
    UserAutoPreTransactionResponse autoPreTransactionForTender(UserAutoPreTransactionRequest preTransactionRequest);

    /**
     * 放款确认
     *
     * @param reqData
     * @return
     */
    ConfirmLoanResponse confirmLoan(String reqData);

}

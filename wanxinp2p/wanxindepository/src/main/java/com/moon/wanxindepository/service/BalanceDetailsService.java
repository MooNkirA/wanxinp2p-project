package com.moon.wanxindepository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxindepository.entity.BalanceDetails;
import com.moon.wanxindepository.model.BalanceDetailsDTO;
import com.moon.wanxindepository.model.ConfirmRepaymentResponse;
import com.moon.wanxindepository.model.RechargeRequest;
import com.moon.wanxindepository.model.UserAutoPreTransactionRequest;
import com.moon.wanxindepository.model.UserAutoPreTransactionResponse;
import com.moon.wanxindepository.model.WithdrawRequest;

import java.math.BigDecimal;

/**
 * 用户余额明细表 服务类
 */
public interface BalanceDetailsService extends IService<BalanceDetails> {

    /**
     * 根据用户编码获取余额信息
     *
     * @param userNo
     * @return
     */
    BalanceDetailsDTO getP2PBalanceDetails(String userNo);

    /**
     * 校验用户余额是否充足
     *
     * @param userNo
     * @param freezeAmount
     * @param response     若为null则余额充足，否则返回异常response
     * @return
     */
    UserAutoPreTransactionResponse verifyEnough(String userNo, BigDecimal freezeAmount, UserAutoPreTransactionResponse response);

    /**
     * 根据预处理请求信息冻结用户金额
     *
     * @param preTransactionRequest
     * @return
     */
    Boolean freezeBalance(UserAutoPreTransactionRequest preTransactionRequest);

    /**
     * 增加用户余额
     *
     * @param balanceDetails
     * @param amount
     * @return
     */
    Boolean increaseBalance(BalanceDetails balanceDetails, BigDecimal amount);

    /**
     * 扣减用户余额
     *
     * @param balanceDetails
     * @param amount
     * @return
     */
    Boolean decreaseBalance(BalanceDetails balanceDetails, BigDecimal amount);

    /**
     * 用户充值
     *
     * @param rechargeRequest
     * @return
     */
    Boolean recharge(RechargeRequest rechargeRequest);

    /**
     * 用户提现
     *
     * @param withDrawRequest
     * @return
     */
    Boolean withDraw(WithdrawRequest withDrawRequest);


    /**
     * 根据用户编号获取余额信息
     *
     * @param userNo
     * @return
     */
    BalanceDetails getByUserNo(String userNo);

    /**
     * 用户开户添加初始化余额信息
     *
     * @param balanceDetails
     * @return
     */
    Boolean addForPersonalRegister(BalanceDetails balanceDetails);

    /**
     * 还款确认：预处理冻结金额
     *
     * @param preTransactionRequest
     * @return
     */
    UserAutoPreTransactionResponse autoPreTransactionForRepayment(UserAutoPreTransactionRequest preTransactionRequest);

    /**
     * 放款确认
     *
     * @param reqData
     * @return
     */
    ConfirmRepaymentResponse confirmRepayment(String reqData);

}

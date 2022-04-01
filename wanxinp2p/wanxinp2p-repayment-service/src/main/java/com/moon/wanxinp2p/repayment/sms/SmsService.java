package com.moon.wanxinp2p.repayment.sms;

import java.math.BigDecimal;

/**
 * 发送短信接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-31 10:39
 * @description
 */
public interface SmsService {

    /**
     * 发送还款短信通知
     *
     * @param mobile 还款人手机号
     * @param date   日期
     * @param amount 应还金额
     */
    void sendRepaymentNotify(String mobile, String date, BigDecimal amount);

}

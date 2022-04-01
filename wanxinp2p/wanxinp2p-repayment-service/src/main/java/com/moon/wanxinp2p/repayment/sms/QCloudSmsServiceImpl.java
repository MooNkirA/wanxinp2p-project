package com.moon.wanxinp2p.repayment.sms;

import com.github.qcloudsms.SmsSingleSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 短信发送接口实现 - 腾讯云短信服务
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-31 11:08
 * @description
 */
@Service
@Log4j2
public class QCloudSmsServiceImpl implements SmsService {

    @Value("${sms.qcloud.appId}")
    private int appId;

    @Value("${sms.qcloud.appKey}")
    private String appKey;

    @Value("${sms.qcloud.templateId}")
    private int templateId;

    @Value("${sms.qcloud.sign}")
    private String sign;

    /**
     * 发送还款短信通知
     *
     * @param mobile 还款人手机号
     * @param date   日期
     * @param amount 应还金额
     */
    @Override
    public void sendRepaymentNotify(String mobile, String date, BigDecimal amount) {
        log.info("给手机号{}，发送还款提醒。还款日：{}, 金额：{}", mobile, date, amount);
        /* 因个人无法申请到短信模板，所以此处不调用腾讯短信 API */
        /*SmsSingleSender ssender = new SmsSingleSender(appId, appKey);
        try {
            ssender.sendWithParam("86", mobile, templateId, new String[]{date, amount.toString()}, sign, "", "");
        } catch (Exception ex) {
            log.error("发送失败：{}", ex.getMessage());
        }*/
    }

}

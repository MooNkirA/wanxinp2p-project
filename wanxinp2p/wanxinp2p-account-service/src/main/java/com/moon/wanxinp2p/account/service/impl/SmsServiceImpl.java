package com.moon.wanxinp2p.account.service.impl;

import com.moon.wanxinp2p.account.common.enums.AccountErrorCode;
import com.moon.wanxinp2p.account.service.SmsService;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 短信服务接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 17:01
 * @description
 */
@Service
public class SmsServiceImpl implements SmsService {

    /* 读取配置中心短信相关的配置 */
    @Value("${sms.url}")
    private String smsURL;

    @Value("${sms.enable}")
    private Boolean smsEnable;

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     * @return
     */
    @Override
    public RestResponse<Object> getSMSCode(String mobile) {
        // 判断是否开启短信验证
        if (smsEnable) {
            return OkHttpUtil.post(smsURL + "/generate?effectiveTime=300&name=sms", "{\"mobile\":" + mobile + "}");
        }
        // 不开启则默认成功
        return RestResponse.success();
    }

    /**
     * 校验验证码
     *
     * @param key  校验标识 redis中的键
     * @param code 短信验证码
     */
    @Override
    public void verifySmsCode(String key, String code) {
        if (smsEnable) {
            // 拼接请求 url
            StringBuilder params = new StringBuilder("/verify?name=sms");
            params.append("&verificationKey=").append(key).append("&verificationCode=").append(code);
            // 发送 post 请求
            RestResponse smsResponse = OkHttpUtil.post(smsURL + params, "");
            if (smsResponse.getCode() != CommonErrorCode.SUCCESS.getCode() || smsResponse.getResult().toString().equalsIgnoreCase("false")) {
                // 校验不通过，抛出异常
                throw new BusinessException(AccountErrorCode.E_140152);
            }
        }
    }

}

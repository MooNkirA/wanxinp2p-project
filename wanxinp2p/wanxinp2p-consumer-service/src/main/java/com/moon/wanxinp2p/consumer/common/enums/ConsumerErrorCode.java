package com.moon.wanxinp2p.consumer.common.enums;

import com.moon.wanxinp2p.common.domain.ErrorCode;
import lombok.Getter;

/**
 * 用户服务响应枚举
 * 成功响应码：0成功；
 * 异常响应码：-1熔断、 -2 标准参数校验不通过 -3会话超时
 * 前两位:服务标识；中间两位:模块标识；后两位:异常标识
 * c端用户服务异常编码以14开始
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 18:21
 * @description
 */
@Getter
public enum ConsumerErrorCode implements ErrorCode {

    // E_140101(140101, "不存在的用户信息"), // 转移到 CommonErrorCode
    E_140102(140102, "请求失败"),
    E_140105(140105, "用户已开户"),
    E_140106(140106, "注册失败"),
    E_140107(140107, "用户已存在"),
    E_140108(140108, "身份信息不一致"),
    E_140131(140131, "用户充值失败"),
    E_140132(140132, "用户存管账户未开通成功"),
    E_140133(140133, "充值金额不合法"),
    E_140141(140141, "用户提现失败"),
    E_140151(140151, "银行卡已被绑定"),
    E_140152(140152, "用户未绑定银行卡");

    private final int code;
    private final String desc;

    ConsumerErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ConsumerErrorCode getErrorCode(int code) {
        for (ConsumerErrorCode errorCode : ConsumerErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }

}

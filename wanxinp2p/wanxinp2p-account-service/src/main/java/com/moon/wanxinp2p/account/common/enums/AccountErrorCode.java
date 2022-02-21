package com.moon.wanxinp2p.account.common.enums;

import com.moon.wanxinp2p.common.domain.ErrorCode;
import lombok.Getter;

/**
 * 统一账户服务响应枚举
 * 成功响应码：0成功；
 * 异常响应码：-1熔断、 -2 标准参数校验不通过 -3会话超时
 * 前两位:服务标识；中间两位:模块标识；后两位:异常标识
 * 统一账号服务异常编码 以13开始
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-19 13:11
 * @description
 */
@Getter
public enum AccountErrorCode implements ErrorCode {

    E_130101(130101, "用户名已存在"),
    E_130104(130104, "用户未注册"),
    E_130105(130105, "用户名或密码错误"),
    E_140141(140141, "注册失败"),

    E_140151(140151, "获取短信验证码失败"),
    E_140152(140152, "验证码错误");

    private final int code;
    private final String desc;

    AccountErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AccountErrorCode getErrorCode(int code) {
        for (AccountErrorCode errorCode : AccountErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }

}

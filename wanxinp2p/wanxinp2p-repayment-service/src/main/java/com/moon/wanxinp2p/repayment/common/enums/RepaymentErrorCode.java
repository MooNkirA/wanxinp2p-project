package com.moon.wanxinp2p.repayment.common.enums;


import com.moon.wanxinp2p.common.domain.ErrorCode;

/**
 * 异常编码 0成功、-1熔断、 -2 标准参数校验不通过 -3会话超时
 * 前两位:服务标识
 * 中间两位:模块标识
 * 后两位:异常标识
 * 还款服务异常编码 17
 */
public enum RepaymentErrorCode implements ErrorCode {
    //////////////////////////////////// 还款服务异常编码 17  //////////////////////////
    /**
     * 标的编码不存在
     */
    E_170101(150101, "不支持一次性还款（含本息）方式"),
    E_170102(150102, "不支持先息后本还款方式"),
    E_170103(150103, "不支持等额本金还款方式"),
    E_170104(150104, "非支持的还款方式"),
    E_170105(150105, "银行存管系统确认还款失败"),
    ;

    private final int code;
    private final String desc;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    RepaymentErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static RepaymentErrorCode setErrorCode(int code) {
        for (RepaymentErrorCode errorCode : RepaymentErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }
}

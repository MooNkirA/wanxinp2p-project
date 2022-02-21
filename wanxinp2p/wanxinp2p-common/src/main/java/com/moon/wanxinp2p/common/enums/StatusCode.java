package com.moon.wanxinp2p.common.enums;

import lombok.Getter;

@Getter
public enum StatusCode {

    /**
     * 发/同布失败
     */
    STATUS_FAIL(2, "发/同布失败"),
    /**
     * 已发/同布
     */
    STATUS_IN(1, "已发/同布"),
    /**
     * 未发/同布
     */
    STATUS_OUT(0, "未发/同布");

    private final Integer code;
    private final String desc;

    StatusCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

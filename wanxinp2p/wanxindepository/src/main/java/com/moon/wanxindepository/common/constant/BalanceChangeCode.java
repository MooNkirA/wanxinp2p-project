package com.moon.wanxindepository.common.constant;

/**
 * 余额变动类型枚举类
 */
public enum BalanceChangeCode {

    NEW(0, "开户"),
    INCREASE(1, "增加"),
    DECREASE(2, "减少"),
    FREEZE(3, "冻结"),
    UNFREEZE(4, "解冻");

    private Integer code;
    private String desc;

    BalanceChangeCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

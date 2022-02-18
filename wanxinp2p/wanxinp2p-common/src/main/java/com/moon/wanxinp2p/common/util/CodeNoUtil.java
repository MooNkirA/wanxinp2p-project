package com.moon.wanxinp2p.common.util;

import com.moon.wanxinp2p.common.enums.CodePrefixCode;

import java.util.UUID;

/**
 * 标的编码, 请求流水号生成工具
 */
public class CodeNoUtil {

    /**
     * 使用UUID使用编码
     *
     * @param prefixCode 前缀用与标识不同业务, 已用枚举类型规定业务名称
     * @return java.lang.String
     */
    public static String getNo(CodePrefixCode prefixCode) {
        return prefixCode.getCode() + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}

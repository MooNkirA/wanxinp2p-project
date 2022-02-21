package com.moon.wanxinp2p.common.exception;

import com.moon.wanxinp2p.common.domain.ErrorCode;

/**
 * 自定义业务异常类。
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-19 13:16
 * @description
 */
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BusinessException() {
        super();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}

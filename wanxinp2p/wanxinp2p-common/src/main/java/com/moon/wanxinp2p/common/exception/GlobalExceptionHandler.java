package com.moon.wanxinp2p.common.exception;

import com.google.common.collect.ImmutableMap;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Function;

/**
 * 统一异常捕获类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-19 13:26
 * @description
 */
/*
 * @ControllerAdvice 控制器增强，在spring 3.2中新增的注解
 *  如果类中所有方法都返回json格式。可以使用 @RestControllerAdvice 注解，是 @ControllerAdvice 与 @ResponseBody 的组合体
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Exception>, Function<Exception, RestResponse<Nullable>>> EXCEPTIONS;

    // 定义map的builder对象，使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Exception>, Function<Exception, RestResponse<Nullable>>> builder = ImmutableMap.builder();

    /* 定义加入一些基础的异常类型判断，返回相应的响应对象 */
    static {
        // BusinessException 处理
        builder.put(BusinessException.class, e -> {
            BusinessException be = (BusinessException) e;

            if (CommonErrorCode.CUSTOM.equals(be.getErrorCode())) {
                return new RestResponse<>(be.getErrorCode().getCode(), be.getMessage());
            } else {
                return new RestResponse<>(be.getErrorCode().getCode(), be.getErrorCode().getDesc());
            }
        });
        // NoHandlerFoundException 处理
        builder.put(NoHandlerFoundException.class, e -> new RestResponse<>(404, "找不到资源"));
        builder.put(HttpRequestMethodNotSupportedException.class, e -> new RestResponse<>(405, "method 方法不支持"));
        builder.put(HttpMediaTypeNotSupportedException.class, e -> new RestResponse<>(415, "不支持媒体类型"));
    }

    /**
     * 异常后的处理方法，value指定需要捕获的异常的类型，通常一种异常类型定义一个方法处理。
     * 这里懒，所以一个方法全部处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public RestResponse<Nullable> exceptionGet(HttpServletRequest request, HttpServletResponse response, Exception e) {
        // 判断ImmutableMap对像是否已经构建
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }

        // 从 ImmutableMap 中找异常类型所对应的响应对象，如果找到直接响应给用户，如果找不到给用户响应99999异常
        Function<Exception, RestResponse<Nullable>> function = EXCEPTIONS.get(e.getClass());
        if (function != null) {
            return function.apply(e);
        } else {
            // 记录其他未知异常日志
            LOGGER.error("【系统异常】{}", e.getMessage());
            // 其他未知异常，统一返回99999异常
            return new RestResponse<>(CommonErrorCode.UNKOWN.getCode(), CommonErrorCode.UNKOWN.getDesc());
        }
    }

}

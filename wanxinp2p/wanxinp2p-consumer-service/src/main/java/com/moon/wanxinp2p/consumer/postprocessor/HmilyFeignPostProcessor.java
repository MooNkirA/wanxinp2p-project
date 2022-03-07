package com.moon.wanxinp2p.consumer.postprocessor;

import feign.InvocationHandlerFactory;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.springcloud.feign.HmilyFeignInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 后置处理器，将 @FeignClient 代理接口中没有被 @Hmily 注解修饰的方法，从 HmilyFeignInterceptor 中移除。
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-06 17:27
 * @description
 */
@Component
@Slf4j
public class HmilyFeignPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        // 对所有含有 @FeignClient 的bean进行处理
        if (AnnotationUtils.findAnnotation(clazz, FeignClient.class) != null) {
            // 排除含有 @Controller 和 @RestController 注解的bean
            if (AnnotationUtils.findAnnotation(clazz, Controller.class) != null ||
                    AnnotationUtils.findAnnotation(clazz, RestController.class) != null) {
                return bean;
            }
            try {
                // 获取代理类中的 FeignInvocationHandler
                Field h = bean.getClass().getSuperclass().getDeclaredField("h");
                boolean hAccessible = h.isAccessible();
                h.setAccessible(true);
                Object feignInvocationHandler = h.get(bean);
                /*
                 * 获取 FeignInvocationHandler 中 dispatch 字段的 Map<Method, MethodHandler> dispatch 属性。
                 * dispatch中包含feign代理的方法 和 SynchronousMethodHandler
                 */
                Field dispatchField = feignInvocationHandler.getClass().getDeclaredField("dispatch");
                boolean dispatchAccessible = dispatchField.isAccessible();
                dispatchField.setAccessible(true);
                Map<Method, InvocationHandlerFactory.MethodHandler> dispatch =
                        (Map<Method, InvocationHandlerFactory.MethodHandler>) dispatchField.get(feignInvocationHandler);

                /*
                 * SynchronousMethodHandler 中的 List<RequestInterceptor> requestInterceptors 字段
                 * 加载了Hmily对feign的拦截器 HmilyFeignInterceptor
                 */
                for (Map.Entry<Method, InvocationHandlerFactory.MethodHandler> entry : dispatch.entrySet()) {
                    /*
                     * 没有添加 @Hmily 注解的方法不需要被 Hmily 拦截处理，
                     * 否则会因为加载的 HmilyTransactionContext 为 null 导致 NullPointerException
                     */
                    if (AnnotationUtils.findAnnotation(entry.getKey(), Hmily.class) == null) {
                        Field riField = entry.getValue().getClass().getDeclaredField("requestInterceptors");
                        boolean riAccessible = riField.isAccessible();
                        riField.setAccessible(true);
                        List<RequestInterceptor> requestInterceptors = (List<RequestInterceptor>) riField.get(entry.getValue());
                        for (RequestInterceptor interceptor : requestInterceptors) {
                            if (interceptor instanceof HmilyFeignInterceptor) {
                                requestInterceptors.remove(interceptor);
                                break;
                            }
                        }
                        riField.setAccessible(riAccessible);
                        log.info("{}.{} 方法移除 HmilyFeignInterceptor", beanName, entry.getKey().getName());
                    }
                }
                dispatchField.setAccessible(dispatchAccessible);
                h.setAccessible(hAccessible);
            } catch (Exception e) {
                log.warn("{} exception", beanName);
                e.printStackTrace();
            }
        }
        return bean;
    }

}

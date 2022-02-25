package com.moon.wanxinp2p.consumer.config;

import com.moon.wanxinp2p.consumer.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-24 10:09
 * @description
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 注册自定义的拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
    }

}

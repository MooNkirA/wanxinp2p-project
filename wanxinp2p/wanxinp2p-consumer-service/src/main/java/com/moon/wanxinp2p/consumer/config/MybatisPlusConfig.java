package com.moon.wanxinp2p.consumer.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus 配置类
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 17:55
 * @description
 */
@Configuration
@MapperScan("com.moon.wanxinp2p.consumer.mapper")
public class MybatisPlusConfig {
}

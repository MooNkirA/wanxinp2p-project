package com.moon.wanxinp2p.account.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus 配置类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-19 21:58
 * @description
 */
@Configuration
@MapperScan("com.moon.wanxinp2p.account.mapper")
public class MybatisPlusConfig {
}

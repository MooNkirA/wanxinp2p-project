package com.moon.wanxinp2p.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 配置 Spring Security OAuth2 采用 jwt 令牌方式
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-23 15:37
 * @description
 */
@Configuration
public class JWTConfig {

    private final static String SIGNING_KEY = "wanxin123";

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY); // 对称秘钥，资源服务器使用该秘钥来解密
        converter.setAccessTokenConverter(new ClientDefaultAccessTokenConverter());
        return converter;
    }

}

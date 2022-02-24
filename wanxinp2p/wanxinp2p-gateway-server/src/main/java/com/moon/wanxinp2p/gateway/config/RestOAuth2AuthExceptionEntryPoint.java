package com.moon.wanxinp2p.gateway.config;

import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.gateway.common.util.HttpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 修改网关错误响应与平台整体风格一致，无需关注
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-23 15:37
 * @description
 */
public class RestOAuth2AuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        RestResponse<String> restResponse = new RestResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        HttpUtil.writerError(restResponse, response);
    }

}


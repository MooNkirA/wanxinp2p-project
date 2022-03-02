package com.moon.wanxinp2p.gateway.config;

import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.gateway.common.util.HttpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

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
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        RestResponse<String> restResponse = new RestResponse<>(HttpStatus.FORBIDDEN.value(), "没有权限");
        HttpUtil.writerError(restResponse, response);
    }

}

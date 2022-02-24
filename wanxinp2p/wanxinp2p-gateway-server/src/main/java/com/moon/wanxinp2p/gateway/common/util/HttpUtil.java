package com.moon.wanxinp2p.gateway.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moon.wanxinp2p.common.domain.RestResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP 工具
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-23 15:44
 * @description
 */
public class HttpUtil {

    public static void writerError(RestResponse restResponse, HttpServletResponse response) throws IOException {
        response.setContentType("application/json,charset=utf-8");
        response.setStatus(restResponse.getCode());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), restResponse);
    }

}

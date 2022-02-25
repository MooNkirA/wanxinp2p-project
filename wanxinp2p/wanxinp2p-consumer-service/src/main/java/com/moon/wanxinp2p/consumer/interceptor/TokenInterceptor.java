package com.moon.wanxinp2p.consumer.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.moon.wanxinp2p.api.account.model.LoginUser;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token 令牌拦截处理
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-24 9:57
 * @description
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取网关转发的用户json字符串
        String jsonToken = request.getParameter("jsonToken");
        if (StringUtils.isNotBlank(jsonToken)) {
            // 将字符串转成实体对象
            LoginUser loginUser = JSON.parseObject(EncryptUtil.decodeUTF8StringBase64(jsonToken),
                    new TypeReference<LoginUser>() {
                    });
            // 设置到请求域
            request.setAttribute("jsonToken", loginUser);
        }
        return true;
    }

}

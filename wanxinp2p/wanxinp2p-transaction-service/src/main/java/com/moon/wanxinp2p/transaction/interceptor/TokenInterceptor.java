package com.moon.wanxinp2p.transaction.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.moon.wanxinp2p.transaction.model.LoginUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token 拦截处理
 */
public class TokenInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        String jsonToken = httpServletRequest.getParameter("jsonToken");
        if (StringUtils.isNotBlank(jsonToken)) {
            LoginUser loginUser = JSON
                    .parseObject(EncryptUtil.decodeUTF8StringBase64(jsonToken), new TypeReference<LoginUser>() {
                    });

            httpServletRequest.setAttribute("jsonToken", loginUser);
        } //PS: else部分是作为测试用的，上线时需要删掉
        else {
            LoginUser loginUser = new LoginUser();
            loginUser.setUsername("yuan");
            httpServletRequest.setAttribute("jsonToken", loginUser);
        }

        return true;
    }

}

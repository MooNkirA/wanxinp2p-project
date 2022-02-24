package com.moon.wanxinp2p.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网关服务权限过滤器
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-23 16:27
 * @description
 */
@Component
public class AuthFilter extends ZuulFilter {

    /**
     * 过滤器的类型
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre"; // 前置过滤器，可以在请求被路由之前调用
    }

    /**
     * 过滤器执行顺序
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否启用此过滤器
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器处理逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 1.获取 Spring Security OAuth2 的认证信息对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)) {
            // instanceof 判断已包含 authentication == null
            return null; // 无 token 访问网关内资源，直接返回 null
        }

        // 2.将当前登录的用户以及接入客户端的信息放入Map中
        OAuth2Authentication oauth = (OAuth2Authentication) authentication;
        // 定义转发的数据结构
        Map<String, String> jsonToken = new HashMap<>(oauth.getOAuth2Request().getRequestParameters());

        /*
         * 3.将 jsonToken 写入转发微服务的 request 中，
         *  微服务就能通过 request.getParams("jsonToken") 获取转发的数据
         */
        // 获取 zuul 上下文对象
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        request.getParameterMap(); // 关键步骤，一定要get一下，下面这行代码才能取到值

        Map<String, List<String>> params = context.getRequestQueryParams();
        if (params == null) {
            params = new HashMap<>();
        }
        List<String> paramsList = new ArrayList<>();
        paramsList.add(EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));
        params.put("jsonToken", paramsList);
        context.setRequestQueryParams(params);
        return null;
    }

}

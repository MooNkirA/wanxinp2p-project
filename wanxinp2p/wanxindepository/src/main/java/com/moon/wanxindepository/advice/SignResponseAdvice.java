package com.moon.wanxindepository.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.wanxindepository.common.util.RSAUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * 统一拦截返回数据进行签名
 */
@ControllerAdvice
public class SignResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Value("${depository.privateKey}")
    private String depositoryPrivateKey;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue mappingJacksonValue, MediaType mediaType,
                                           MethodParameter methodParameter, ServerHttpRequest serverHttpRequest,
                                           ServerHttpResponse serverHttpResponse) {
        if (serverHttpRequest.getURI().getPath().contains("swagger")) {
            return;
        }
        JSONObject body = JSON.parseObject(JSON.toJSONString(mappingJacksonValue.getValue()));
        if (body != null && StringUtils.isNotBlank(body.getString("respData"))) {
            String signature = RSAUtil.sign(body.getString("respData"), depositoryPrivateKey, "utf-8");
            body.put("signature", signature);
            mappingJacksonValue.setValue(body);
        }
    }
}

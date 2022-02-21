package com.moon.wanxinp2p.consumer.agent;

import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountRegisterDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign 调用模板接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-21 10:15
 * @description
 */
/*
 * @FeignClient 注解，用于标识当前接口为Feign调用微服务的核心接口
 *  value/name属性：指定需要调用的服务提供者的名称
 */
@FeignClient("account-service")
public interface AccountApiAgent {

    /*
     * 创建需要调用的微服务接口方法，SpringCloud 对 Feign 进行了增强兼容了 SpringMVC 的注解
     *  使用的两个注意点：
     *  1. FeignClient 接口有参数时，必须在参数加@PathVariable("XXX")和@RequestParam("XXX")注解，并且必须要指定对应的参数值（原来SpringMVC是可以省略）
     *  2. feignClient 返回值为复杂对象时，其对象类型必须有无参构造函数
     *  3. 方法的名称不需要与被调用的服务接口名称一致
     */
    @PostMapping(value = "/account/l/accounts")
    RestResponse<AccountDTO> register(@RequestBody AccountRegisterDTO accountRegisterDTO);

}

package com.moon.hmilydemo.bank1.feignClient;

import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * benk2 服务调用
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:36
 * @description
 */
@FeignClient(value = "hmily-demo-bank2") // 调用的服务id
public interface Bank2Client {

    @GetMapping("/bank2/transfer")
    // @Hmily 注解为hmily分布式事务接口标识，表示该接口参与hmily分布式事务
    @Hmily
    Boolean transfer(@RequestParam("amount") Double amount);

}

package com.moon.wanxinp2p.uaa.agent;

import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountLoginDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import feign.hystrix.FallbackFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * 账户微服务 feign 调用服务降级处理实现类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-10 16:54
 * @description
 */
@Component
@Log4j2
public class AccountFallbackFactory implements FallbackFactory<AccountApiAgent> {

    /**
     * 服务降级时调用的方法
     *
     * @param cause
     * @return
     */
    @Override
    public AccountApiAgent create(Throwable cause) {
        return new AccountApiAgent() {
            @Override
            public RestResponse<AccountDTO> login(AccountLoginDTO accountLoginDTO) {
                // 记录异常日志
                log.error("AccountApiAgent fallback; reason was: {}", cause.getMessage(), cause);
                // 返回降级处理
                return RestResponse.result(CommonErrorCode.E_999995);
            }
        };
    }

}

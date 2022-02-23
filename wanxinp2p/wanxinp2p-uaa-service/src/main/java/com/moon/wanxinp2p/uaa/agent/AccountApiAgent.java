package com.moon.wanxinp2p.uaa.agent;

import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountLoginDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 统一账户 Feign 远程调用接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-22 15:20
 * @description
 */
@FeignClient("account-service")
public interface AccountApiAgent {

    @PostMapping(value = "/account/l/accounts/session")
    RestResponse<AccountDTO> login(AccountLoginDTO accountLoginDTO);

}

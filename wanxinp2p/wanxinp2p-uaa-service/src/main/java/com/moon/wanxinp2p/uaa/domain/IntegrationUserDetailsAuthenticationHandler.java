package com.moon.wanxinp2p.uaa.domain;

import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountLoginDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.uaa.agent.AccountApiAgent;
import com.moon.wanxinp2p.uaa.common.utils.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class IntegrationUserDetailsAuthenticationHandler {

    /**
     * 认证处理过程
     *
     * @param domain             用户域，如b端用户、c端用户等，用户扩展
     * @param authenticationType 认证类型，如密码认证，短信认证等，用于扩展
     * @param token              Spring Security 的 token 对象，可提取用户名、密码等用于认证的信息
     * @return UnifiedUserDetails 自定义的 UserDetails 接口实现，用于登录成功后返回的存放相关信息
     */
    public UnifiedUserDetails authentication(String domain, String authenticationType,
                                             UsernamePasswordAuthenticationToken token) {
        // 从客户端获取用户名并校验
        String username = token.getName();
        if (StringUtils.isBlank(username)) {
            throw new BadCredentialsException("账户为空");
        }

        // 获取密码并校验
        String presentedPassword = (String) token.getCredentials();
        if (StringUtils.isBlank(presentedPassword)) {
            throw new BadCredentialsException("密码为空");
        }

        // 远程调用统一账户服务，组装请求参数
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO();
        accountLoginDTO.setDomain(domain);
        accountLoginDTO.setUsername(username);
        accountLoginDTO.setMobile(username);
        accountLoginDTO.setPassword(presentedPassword);
        // 远程服务调用，进行账户密码校验
        AccountApiAgent accountApiAgent = ApplicationContextHelper.getBean(AccountApiAgent.class);
        RestResponse<AccountDTO> response = accountApiAgent.login(accountLoginDTO);

        // 请求异常处理
        if (response == null || response.getCode() != 0) {
            throw new BadCredentialsException("登录失败");
        }

        // 登录成功，把用户数据封装到 UnifiedUserDetails 对象中
        UnifiedUserDetails unifiedUserDetails =
                new UnifiedUserDetails(response.getResult().getUsername(), presentedPassword, AuthorityUtils.createAuthorityList());
        unifiedUserDetails.setMobile(response.getResult().getMobile());
        return unifiedUserDetails;
    }

    private UnifiedUserDetails getUserDetails(String username) {
        Map<String, UnifiedUserDetails> userDetailsMap = new HashMap<>();
        userDetailsMap.put("admin",
                new UnifiedUserDetails("admin", "111111", AuthorityUtils.createAuthorityList("ROLE_PAGE_A", "PAGE_B")));
        userDetailsMap.put("moon",
                new UnifiedUserDetails("moon", "111111", AuthorityUtils.createAuthorityList("ROLE_PAGE_A", "PAGE_B")));

        userDetailsMap.get("admin").setDepartmentId("1");
        userDetailsMap.get("admin").setMobile("13800000000");
        userDetailsMap.get("admin").setTenantId("1");
        Map<String, List<String>> au1 = new HashMap<>();
        au1.put("ROLE1", new ArrayList<>());
        au1.get("ROLE1").add("p1");
        au1.get("ROLE1").add("p2");
        userDetailsMap.get("admin").setUserAuthorities(au1);
        Map<String, Object> payload1 = new HashMap<>();
        payload1.put("res", "res1111111");
        userDetailsMap.get("admin").setPayload(payload1);


        userDetailsMap.get("moon").setDepartmentId("2");
        userDetailsMap.get("moon").setMobile("13800000001");
        userDetailsMap.get("moon").setTenantId("1");
        Map<String, List<String>> au2 = new HashMap<>();
        au2.put("ROLE2", new ArrayList<>());
        au2.get("ROLE2").add("p3");
        au2.get("ROLE2").add("p4");
        userDetailsMap.get("moon").setUserAuthorities(au2);

        Map<String, Object> payload2 = new HashMap<>();
        payload2.put("res", "res222222");
        userDetailsMap.get("moon").setPayload(payload2);

        return userDetailsMap.get(username);
    }

}

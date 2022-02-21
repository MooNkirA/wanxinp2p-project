package com.moon.wanxinp2p.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.account.entity.Account;
import com.moon.wanxinp2p.account.mapper.AccountMapper;
import com.moon.wanxinp2p.account.service.AccountService;
import com.moon.wanxinp2p.account.service.SmsService;
import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountRegisterDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.StatusCode;
import com.moon.wanxinp2p.common.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 统一账户业务实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 22:47
 * @description
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private SmsService smsService;

    // 注入是否开启短信校验标识
    @Value("${sms.enable}")
    private Boolean smsEnable;

    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     * @return
     */
    @Override
    public RestResponse<Object> getSMSCode(String mobile) {
        return smsService.getSMSCode(mobile);
    }

    /**
     * 校验短信验证码
     *
     * @param mobile 手机号码
     * @param key    校验标识 redis中的键
     * @param code   验证码
     * @return
     */
    @Override
    public Integer checkMobile(String mobile, String key, String code) {
        // 调用短信服务校验方法，如果校验不通过，方法会抛出 BusinessException
        smsService.verifySmsCode(key, code);

        // 查询数据库，手机号是否存在 count() 是 mp 封装的通用业务层方法
        int count = this.count(new QueryWrapper<Account>()
                .lambda()
                .eq(Account::getMobile, mobile));
        // 存在返回 1，不存在返回 0
        return count > 0 ? 1 : 0;
    }

    /**
     * 统一账户注册
     *
     * @param accountRegisterDTO 账户注册的信息
     * @return
     */
    @Override
    public AccountDTO register(AccountRegisterDTO accountRegisterDTO) {
        // mybatis-plus 操作数据是建立映射的实体类，所以这里 dto 转 Account
        Account account = new Account();
        account.setUsername(accountRegisterDTO.getUsername());
        account.setMobile(accountRegisterDTO.getMobile());
        account.setPassword(PasswordUtil.generate(accountRegisterDTO.getPassword()));

        /*
         * 密码需要加密。这里需要特别注意：
         *   当初需求设计是，如果当短信验证码功能关闭时，用户是直接使用手机号做为密码来登陆
         *   所以这里注册时，就判断是否开启短信验证，如果开启，则设置手机号做为密码
         */
        account.setPassword(PasswordUtil.generate(smsEnable ? accountRegisterDTO.getMobile() : accountRegisterDTO.getPassword()));
        // 补充一些常量
        account.setDomain(Account.DomainEnum.C.getCode());
        account.setStatus(StatusCode.STATUS_OUT.getCode());

        // 新增数据
        this.save(account);

        AccountDTO dto = new AccountDTO();
        BeanUtils.copyProperties(account, dto);
        return dto;
    }

}

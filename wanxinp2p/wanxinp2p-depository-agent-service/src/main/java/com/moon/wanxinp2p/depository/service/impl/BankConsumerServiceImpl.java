package com.moon.wanxinp2p.depository.service.impl;

import com.alibaba.fastjson.JSON;
import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.depository.service.BankConsumerService;
import com.moon.wanxinp2p.depository.service.ConfigService;
import com.moon.wanxinp2p.depository.service.OkHttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 银行用户业务接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-14 23:16
 * @description
 */
@Service
@Slf4j
public class BankConsumerServiceImpl implements BankConsumerService {

    // 注入配置读取工具类
    @Autowired
    private ConfigService configService;

    @Autowired
    private OkHttpService okHttpService;

    /**
     * 远程调用存管系统获取用户余额信息
     *
     * @param userNo 用户编码
     * @return
     */
    @Override
    public BalanceDetailsDTO getBalance(String userNo) {
        // 拼接远程调用url
        String url = configService.getDepositoryUrl() + "/balance-details/" + userNo;
        // 发送get请求
        String responseBody = okHttpService.doGet(url);
        if (StringUtils.hasText(responseBody)) {
            return JSON.parseObject(responseBody, BalanceDetailsDTO.class);
        } else {
            log.warn("调用存管系统{}获取余额失败", url);
            throw new BusinessException(CommonErrorCode.E_100106);
        }
    }
}

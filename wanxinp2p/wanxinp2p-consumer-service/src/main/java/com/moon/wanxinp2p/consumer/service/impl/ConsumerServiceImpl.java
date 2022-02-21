package com.moon.wanxinp2p.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.common.enums.CodePrefixCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.CodeNoUtil;
import com.moon.wanxinp2p.consumer.common.enums.ConsumerErrorCode;
import com.moon.wanxinp2p.consumer.entity.Consumer;
import com.moon.wanxinp2p.consumer.mapper.ConsumerMapper;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户业务层接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 18:07
 * @description
 */
@Service
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService {

    /**
     * 检测用户是否存在
     *
     * @param mobile
     * @return
     */
    @Override
    public Integer checkMobile(String mobile) {
        return getByMobile(mobile) == null ? 0 : 1;
    }

    /**
     * 用户注册
     *
     * @param consumerRegisterDTO
     * @return
     */
    @Override
    public void register(ConsumerRegisterDTO consumerRegisterDTO) {
        // 判断用户是否已注册
        if (checkMobile(consumerRegisterDTO.getMobile()) == 1) {
            // 已注册则抛出业务异常
            throw new BusinessException(ConsumerErrorCode.E_140107);
        }

        Consumer consumer = new Consumer();
        BeanUtils.copyProperties(consumerRegisterDTO, consumer);
        consumer.setUsername(CodeNoUtil.getNo(CodePrefixCode.CODE_NO_PREFIX));
        consumerRegisterDTO.setUsername(consumer.getUsername());
        consumer.setUserNo(CodeNoUtil.getNo(CodePrefixCode.CODE_REQUEST_PREFIX));
        consumer.setIsBindCard(0);

        // 保存用户
        save(consumer);
    }

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile 手机号
     * @return
     */
    private ConsumerDTO getByMobile(String mobile) {
        // 根据手机号查询
        Consumer consumer = this.getOne(new QueryWrapper<Consumer>().lambda().eq(Consumer::getMobile, mobile));

        if (consumer != null) {
            // 转 dto
            ConsumerDTO dto = new ConsumerDTO();
            BeanUtils.copyProperties(consumer, dto);
            return dto;
        }

        return null;
    }

}

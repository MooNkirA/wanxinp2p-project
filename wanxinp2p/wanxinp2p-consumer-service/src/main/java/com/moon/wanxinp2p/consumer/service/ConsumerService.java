package com.moon.wanxinp2p.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.api.consumer.model.BorrowerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.BalanceDetailsDTO;
import com.moon.wanxinp2p.api.depository.model.DepositoryConsumerResponse;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.consumer.entity.Consumer;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 用户业务层接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 18:04
 * @description
 */
public interface ConsumerService extends IService<Consumer> {

    /**
     * 检测用户是否存在
     *
     * @param mobile
     * @return
     */
    Integer checkMobile(String mobile);

    /**
     * 用户注册
     *
     * @param consumerRegisterDTO
     * @return
     */
    void register(ConsumerRegisterDTO consumerRegisterDTO);

    /**
     * 生成开户数据
     *
     * @param consumerRequest
     * @return
     */
    RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest);

    /**
     * 更新开户结果
     *
     * @param response
     * @return
     */
    Boolean modifyResult(DepositoryConsumerResponse response);

    /**
     * 通过手机号获取当前用户信息
     *
     * @param mobile
     * @return
     */
    ConsumerDTO getByMobile(String mobile);

    /**
     * 获取借款人基本信息
     *
     * @param id 用户id
     * @return
     */
    BorrowerDTO getBorrower(Long id);

    /**
     * 远程调用存管系统获取用户余额信息
     *
     * @param userNo
     * @return
     */
    RestResponse<BalanceDetailsDTO> getBalanceFromDepository(String userNo);

    /**
     * 提交身份证图片给百度AI进行识别
     *
     * @param file 被上传的文件
     * @param flag 身份证正反面  取值front 或 back
     * @return Map集合 识别成功后把身份证上的姓名和身份证号存到map中返回
     */
    Map<String, String> imageRecognition(MultipartFile file, String flag);
}

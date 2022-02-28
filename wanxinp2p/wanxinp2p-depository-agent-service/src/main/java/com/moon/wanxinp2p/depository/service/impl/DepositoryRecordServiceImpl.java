package com.moon.wanxinp2p.depository.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.enums.StatusCode;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.moon.wanxinp2p.common.util.RSAUtil;
import com.moon.wanxinp2p.depository.common.constants.DepositoryConstants;
import com.moon.wanxinp2p.depository.common.enums.DepositoryRequestTypeCode;
import com.moon.wanxinp2p.depository.entity.DepositoryRecord;
import com.moon.wanxinp2p.depository.mapper.DepositoryRecordMapper;
import com.moon.wanxinp2p.depository.service.ConfigService;
import com.moon.wanxinp2p.depository.service.DepositoryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 存管记录业务接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 23:08
 * @description
 */
@Service
public class DepositoryRecordServiceImpl extends ServiceImpl<DepositoryRecordMapper, DepositoryRecord> implements DepositoryRecordService {

    // 注入配置读取工具类
    @Autowired
    private ConfigService configService;

    /**
     * 开通存管账户
     *
     * @param consumerRequest 开户信息
     * @return
     */
    @Override
    public GatewayRequest createConsumer(ConsumerRequest consumerRequest) {
        /* 1.保存开户信息 */
        saveDepositoryRecord(consumerRequest);

        /* 2.封装返回数据，并进行签名 */
        // 将开户的数据转成json字符串
        String reqData = JSON.toJSONString(consumerRequest);
        // 调用工具类方法，使用私钥对 json 字符串签名
        String sign = RSAUtil.sign(reqData, configService.getP2pPrivateKey(), DepositoryConstants.UTF8);

        // 创建接口返回实体
        GatewayRequest gatewayRequest = new GatewayRequest();
        // 请求的存管接口名，详见《银行存管接口说明.pdf》
        gatewayRequest.setServiceName(DepositoryConstants.INTERFACT_NAME_REGISTER);
        // 平台编号，平台与存管系统签约时获取。配置在apollo上
        gatewayRequest.setPlatformNo(configService.getP2pCode());
        // 业务数据报文，json格式。进行转码
        gatewayRequest.setReqData(EncryptUtil.encodeURL(EncryptUtil.encodeUTF8StringBase64(reqData)));
        // 签名
        gatewayRequest.setSignature(EncryptUtil.encodeURL(sign));
        // 银行存管系统地址。配置在apollo上
        gatewayRequest.setDepositoryUrl(configService.getDepositoryUrl() + "/gateway");
        return gatewayRequest;
    }

    /**
     * 保存开户信息
     *
     * @param consumerRequest
     */
    private void saveDepositoryRecord(ConsumerRequest consumerRequest) {
        DepositoryRecord record = new DepositoryRecord();
        // 请求流水号
        record.setRequestNo(consumerRequest.getRequestNo());
        // 请求类型
        record.setRequestType(DepositoryRequestTypeCode.CONSUMER_CREATE.getCode());
        // 业务实体类型
        record.setObjectType(DepositoryConstants.OBJECT_TYPE_CONSUMER);
        // 关联业务实体标识
        record.setObjectId(consumerRequest.getId());
        // 请求时间
        record.setCreateDate(LocalDateTime.now());
        // 数据同步状态（未）
        record.setRequestStatus(StatusCode.STATUS_OUT.getCode());
        this.save(record); // MP 提供的保存方法
    }

}

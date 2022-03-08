package com.moon.wanxinp2p.depository.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.DepositoryBaseResponse;
import com.moon.wanxinp2p.api.depository.model.DepositoryResponseDTO;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.api.depository.model.ProjectRequestDataDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.constants.CommonConstants;
import com.moon.wanxinp2p.common.constants.ServiceNameConstants;
import com.moon.wanxinp2p.common.enums.StatusCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.EncryptUtil;
import com.moon.wanxinp2p.common.util.RSAUtil;
import com.moon.wanxinp2p.depository.common.enums.DepositoryErrorCode;
import com.moon.wanxinp2p.depository.common.enums.DepositoryRequestTypeCode;
import com.moon.wanxinp2p.depository.entity.DepositoryRecord;
import com.moon.wanxinp2p.depository.mapper.DepositoryRecordMapper;
import com.moon.wanxinp2p.depository.service.ConfigService;
import com.moon.wanxinp2p.depository.service.DepositoryRecordService;
import com.moon.wanxinp2p.depository.service.OkHttpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private OkHttpService okHttpService;

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
        String sign = RSAUtil.sign(reqData, configService.getP2pPrivateKey(), CommonConstants.UTF8);

        // 创建接口返回实体
        GatewayRequest gatewayRequest = new GatewayRequest();
        // 请求的存管接口名，详见《银行存管接口说明.pdf》
        gatewayRequest.setServiceName(ServiceNameConstants.NAME_PERSONAL_REGISTER);
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
     * 根据请求流水号更新请求状态
     *
     * @param requestNo
     * @param requestsStatus
     * @return
     */
    @Override
    public Boolean modifyRequestStatus(String requestNo, Integer requestsStatus) {
        // 使用 LambdaUpdateWrapper 方式根据流水号更新状态与确认时间字段
        return this.update(
                Wrappers.<DepositoryRecord>lambdaUpdate()
                        .eq(DepositoryRecord::getRequestNo, requestNo)
                        .set(DepositoryRecord::getRequestStatus, requestsStatus)
                        .set(DepositoryRecord::getConfirmDate, LocalDateTime.now())
        );
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
        record.setObjectType(CommonConstants.OBJECT_TYPE_CONSUMER);
        // 关联业务实体标识
        record.setObjectId(consumerRequest.getId());
        // 请求时间
        record.setCreateDate(LocalDateTime.now());
        // 数据同步状态（未）
        record.setRequestStatus(StatusCode.STATUS_OUT.getCode());
        this.save(record); // MP 提供的保存方法
    }

    /**
     * 保存标的
     *
     * @param projectDTO
     * @return
     */
    @Override
    @Transactional
    public DepositoryResponseDTO<DepositoryBaseResponse> createProject(ProjectDTO projectDTO) {
        // 1. 本地保存交易记录
        DepositoryRecord depositoryRecord = new DepositoryRecord()
                .setRequestNo(projectDTO.getRequestNo()) // 设置请求流水号
                .setRequestType(DepositoryRequestTypeCode.CREATE.getCode()) // 设置请求类型
                .setObjectType("Project") // 设置关联业务实体类型
                .setObjectId(projectDTO.getId()) // 设置关联业务实体标识
                .setCreateDate(LocalDateTime.now()) // 设置请求时间
                .setRequestStatus(StatusCode.STATUS_OUT.getCode()); // 设置数据同步状态
        // 保存数据
        save(depositoryRecord);

        // 2. 签名数据，将 ProjectDTO 转换为银行存管系统的请求类型 ProjectRequestDataDTO
        ProjectRequestDataDTO requestDataDTO = new ProjectRequestDataDTO();
        BeanUtils.copyProperties(projectDTO, requestDataDTO);
        // 转成 JSON 字符串
        String jsonString = JSON.toJSONString(requestDataDTO);
        // 使用 base64 编码
        String reqData = EncryptUtil.encodeUTF8StringBase64(jsonString);

        // 3. 使用 OKHttpClient 发送 Http 请求向银行存管系统发送数据(标的信息)，根据结果修改状态并返回结果
        String url = configService.getDepositoryUrl() + "/service";
        return sendHttpGet(url, reqData, depositoryRecord);
    }

    private DepositoryResponseDTO<DepositoryBaseResponse> sendHttpGet(String url, String reqData,
                                                                      DepositoryRecord depositoryRecord) {
        /*
         * 银行存管系统接收的4大参数: serviceName, platformNo, reqData, signature。
         * signature会在okHttp拦截器(SignatureInterceptor)中处理
         */
        // 平台编号
        String platformNo = configService.getP2pCode();
        // redData签名
        // 发送请求, 获取结果, 如果检验签名失败, 拦截器会在结果中放入: "signature", "false"
        String responseBody = okHttpService
                .doSyncGet(url + "?serviceName=CREATE_PROJECT&platformNo=" + platformNo + "&reqData=" + reqData);
        DepositoryResponseDTO<DepositoryBaseResponse> depositoryResponse = JSON
                .parseObject(responseBody, new TypeReference<DepositoryResponseDTO<DepositoryBaseResponse>>() {
                });

        // 封装返回的处理结果
        depositoryRecord.setResponseData(responseBody);

        // 响应后, 根据结果更新数据库( 进行签名判断 )。判断签名(signature)是为 false, 如果是说明验签失败!
        boolean isFail = "false".equals(depositoryResponse.getSignature());

        // 根据响应是否成功，设置数据同步状态
        depositoryRecord.setRequestStatus(isFail ?
                StatusCode.STATUS_FAIL.getCode() : StatusCode.STATUS_IN.getCode());
        // 设置消息确认时间
        depositoryRecord.setConfirmDate(LocalDateTime.now());
        // 更新数据库
        updateById(depositoryRecord);

        if (isFail) {
            // 抛业务异常
            throw new BusinessException(DepositoryErrorCode.E_160101);
        }

        return depositoryResponse;
    }
}

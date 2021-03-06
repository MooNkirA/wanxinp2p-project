package com.moon.wanxinp2p.depository.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.DepositoryBaseResponse;
import com.moon.wanxinp2p.api.depository.model.DepositoryResponseDTO;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.api.depository.model.LoanRequest;
import com.moon.wanxinp2p.api.depository.model.ProjectRequestDataDTO;
import com.moon.wanxinp2p.api.depository.model.RechargeRequest;
import com.moon.wanxinp2p.api.depository.model.UserAutoPreTransactionRequest;
import com.moon.wanxinp2p.api.depository.model.WithdrawRequest;
import com.moon.wanxinp2p.api.repayment.model.RepaymentRequest;
import com.moon.wanxinp2p.api.transaction.model.ModifyProjectStatusDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.cache.Cache;
import com.moon.wanxinp2p.common.constants.CommonConstants;
import com.moon.wanxinp2p.common.constants.ServiceNameConstants;
import com.moon.wanxinp2p.common.enums.PreprocessBusinessTypeCode;
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

import java.time.LocalDateTime;

/**
 * ??????????????????????????????
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 23:08
 * @description
 */
@Service
public class DepositoryRecordServiceImpl extends ServiceImpl<DepositoryRecordMapper, DepositoryRecord> implements DepositoryRecordService {

    // ???????????????????????????
    @Autowired
    private ConfigService configService;

    @Autowired
    private OkHttpService okHttpService;

    @Autowired
    private Cache cache;

    /**
     * ??????????????????
     *
     * @param consumerRequest ????????????
     * @return
     */
    @Override
    public GatewayRequest createConsumer(ConsumerRequest consumerRequest) {
        /* 1.?????????????????? */
        saveDepositoryRecord(consumerRequest);

        /* 2.???????????????????????????????????? */
        // ????????????????????????json?????????
        String reqData = JSON.toJSONString(consumerRequest);
        // ??????????????????????????????????????? json ???????????????
        String sign = RSAUtil.sign(reqData, configService.getP2pPrivateKey(), CommonConstants.UTF8);

        // ????????????????????????
        GatewayRequest gatewayRequest = new GatewayRequest();
        // ????????????????????????????????????????????????????????????.pdf???
        gatewayRequest.setServiceName(ServiceNameConstants.NAME_PERSONAL_REGISTER);
        // ???????????????????????????????????????????????????????????????apollo???
        gatewayRequest.setPlatformNo(configService.getP2pCode());
        // ?????????????????????json?????????????????????
        gatewayRequest.setReqData(EncryptUtil.encodeURL(EncryptUtil.encodeUTF8StringBase64(reqData)));
        // ??????
        gatewayRequest.setSignature(EncryptUtil.encodeURL(sign));
        // ????????????????????????????????????apollo???
        gatewayRequest.setDepositoryUrl(configService.getDepositoryUrl() + "/gateway");
        return gatewayRequest;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param requestNo
     * @param requestsStatus
     * @return
     */
    @Override
    public Boolean modifyRequestStatus(String requestNo, Integer requestsStatus) {
        // ?????? LambdaUpdateWrapper ??????????????????????????????????????????????????????
        return this.update(
                Wrappers.<DepositoryRecord>lambdaUpdate()
                        .eq(DepositoryRecord::getRequestNo, requestNo)
                        .set(DepositoryRecord::getRequestStatus, requestsStatus)
                        .set(DepositoryRecord::getConfirmDate, LocalDateTime.now())
        );
    }

    /**
     * ??????????????????
     *
     * @param consumerRequest
     */
    private void saveDepositoryRecord(ConsumerRequest consumerRequest) {
        DepositoryRecord record = new DepositoryRecord();
        // ???????????????
        record.setRequestNo(consumerRequest.getRequestNo());
        // ????????????
        record.setRequestType(DepositoryRequestTypeCode.CONSUMER_CREATE.getCode());
        // ??????????????????
        record.setObjectType(CommonConstants.OBJECT_TYPE_CONSUMER);
        // ????????????????????????
        record.setObjectId(consumerRequest.getId());
        // ????????????
        record.setCreateDate(LocalDateTime.now());
        // ???????????????????????????
        record.setRequestStatus(StatusCode.STATUS_OUT.getCode());
        this.save(record); // MP ?????????????????????
    }

    /**
     * ????????????
     *
     * @param projectDTO
     * @return
     */
    @Override
    public DepositoryResponseDTO<DepositoryBaseResponse> createProject(ProjectDTO projectDTO) {
        // 1. ?????? DepositoryRecord ????????????????????????????????????
        DepositoryRecord depositoryRecord = new DepositoryRecord()
                .setRequestNo(projectDTO.getRequestNo()) // ?????????????????????
                .setRequestType(DepositoryRequestTypeCode.CREATE.getCode()) // ??????????????????
                .setObjectType("Project") // ??????????????????????????????
                .setObjectId(projectDTO.getId()); // ??????????????????????????????

        // ?????????????????????????????????????????????????????????
        DepositoryResponseDTO<DepositoryBaseResponse> responseDTO = this.handleIdempotent(depositoryRecord);
        // ???????????????????????????????????????????????????????????????????????????????????????????????????
        if (responseDTO != null) {
            // ????????????????????????
            return responseDTO;
        }

        // ?????????????????????????????????1???????????????????????????????????????????????????????????????
        depositoryRecord = this.getEntityByRequestNo(projectDTO.getRequestNo());

        // 2. ?????????????????? ProjectDTO ?????????????????????????????????????????? ProjectRequestDataDTO
        ProjectRequestDataDTO requestDataDTO = new ProjectRequestDataDTO();
        BeanUtils.copyProperties(projectDTO, requestDataDTO);
        // ?????? JSON ?????????
        String jsonString = JSON.toJSONString(requestDataDTO);
        // ?????? base64 ??????
        String reqData = EncryptUtil.encodeUTF8StringBase64(jsonString);

        // 3. ?????? OKHttpClient ?????? Http ???????????????????????????????????????(????????????)??????????????????????????????????????????
        return sendHttpGet("CREATE_PROJECT", reqData, depositoryRecord);
    }

    /**
     * ???????????????????????????????????? GET ??????
     * 2022.03.19 ??????????????????????????? url ?????????????????????????????????????????????????????????
     *
     * @param serviceName      ?????????????????????????????????
     * @param reqData          ???????????????????????????
     * @param depositoryRecord ??????????????????
     * @return
     */
    private DepositoryResponseDTO<DepositoryBaseResponse> sendHttpGet(String serviceName, String reqData,
                                                                      DepositoryRecord depositoryRecord) {
        /*
         * ???????????????????????????4?????????: serviceName, platformNo, reqData, signature???
         * signature??????okHttp?????????(SignatureInterceptor)?????????
         */
        // ????????????
        String platformNo = configService.getP2pCode();
        // ?????????????????? url
        String url = configService.getDepositoryUrl() + "/service";
        // redData??????
        // ????????????, ????????????, ????????????????????????, ??????????????????????????????: "signature", "false"
        String responseBody = okHttpService
                .doSyncGet(url + "?serviceName=" + serviceName + "&platformNo=" + platformNo + "&reqData=" + reqData);
        DepositoryResponseDTO<DepositoryBaseResponse> depositoryResponse = JSON
                .parseObject(responseBody, new TypeReference<DepositoryResponseDTO<DepositoryBaseResponse>>() {
                });

        // ???????????????????????????
        depositoryRecord.setResponseData(responseBody);

        // ?????????, ???????????????????????????( ?????????????????? )???????????????(signature)?????? false, ???????????????????????????!
        boolean isFail = "false".equals(depositoryResponse.getSignature());

        // ???????????????????????????????????????????????????
        depositoryRecord.setRequestStatus(isFail ?
                StatusCode.STATUS_FAIL.getCode() : StatusCode.STATUS_IN.getCode());
        // ????????????????????????
        depositoryRecord.setConfirmDate(LocalDateTime.now());
        // ???????????????
        updateById(depositoryRecord);

        if (isFail) {
            // ???????????????
            throw new BusinessException(DepositoryErrorCode.E_160101);
        }

        return depositoryResponse;
    }

    /**
     * ???????????????
     *
     * @param record ????????????
     * @return
     */
    private DepositoryResponseDTO<DepositoryBaseResponse> handleIdempotent(DepositoryRecord record) {
        // 1. ?????? requestNo ????????????
        String requestNo = record.getRequestNo();
        DepositoryRecord depositoryRecord = this.getEntityByRequestNo(requestNo);

        // 2. ??????????????????????????????????????????
        if (depositoryRecord == null) {
            // ?????????????????????????????????
            record.setCreateDate(LocalDateTime.now()); // ??????????????????
            record.setRequestStatus(StatusCode.STATUS_OUT.getCode()); // ????????????????????????
            // ??????????????????
            this.save(record);
            // ?????????????????????????????????????????????????????????????????????
            return null;
        }

        // 3. ????????????????????????????????????????????????????????????????????????????????????????????????
        if (StatusCode.STATUS_OUT.getCode().compareTo(depositoryRecord.getRequestStatus()) == 0) {
            /*
             * ??????redis??????????????????????????????????????? redis ??? incrBy ???????????????
             * ?????? requestNo ??????????????????1???????????????????????????????????????requestNo???????????????+1???
             */
            Long count = cache.incrBy(requestNo, 1L);

            if (count == 1) {
                // ??????????????????1?????????
                cache.expire(requestNo, 5); // ?????? requestNo ?????????5???
                // ?????????????????????????????????????????????????????????????????????
                return null;
            } else if (count > 1) {
                // ???count??????1????????????????????????????????????????????????????????????????????????
                throw new BusinessException(DepositoryErrorCode.E_160103);
            }
        }

        /*
         * 4. ?????????????????????????????????????????????????????????/"????????????"??????????????????????????????????????????????????????
         *   ?????????????????? RESPONSE_DATA ??????????????? DepositoryResponseDTO<DepositoryBaseResponse> ????????????
         */
        return JSON.parseObject(depositoryRecord.getResponseData(),
                new TypeReference<DepositoryResponseDTO<DepositoryBaseResponse>>() {
                });
    }

    /**
     * ???????????????????????????????????????
     *
     * @param requestNo ???????????????
     * @return
     */
    private DepositoryRecord getEntityByRequestNo(String requestNo) {
        return getOne(new QueryWrapper<DepositoryRecord>().lambda().eq(DepositoryRecord::getRequestNo, requestNo));
    }

    /**
     * ???????????????
     *
     * @param userAutoPreTransactionRequest
     * @return
     */
    @Override
    public DepositoryResponseDTO<DepositoryBaseResponse> userAutoPreTransaction(UserAutoPreTransactionRequest userAutoPreTransactionRequest) {
        // ?????? DepositoryRecord ????????????????????????????????????
        String requestNo = userAutoPreTransactionRequest.getRequestNo();
        DepositoryRecord depositoryRecord = new DepositoryRecord()
                .setRequestNo(requestNo) // ?????????????????????
                .setRequestType(userAutoPreTransactionRequest.getBizType()) // ??????????????????
                .setObjectType("UserAutoPreTransactionRequest") // ??????????????????????????????
                .setObjectId(userAutoPreTransactionRequest.getId()); // ??????????????????????????????

        // ???????????????????????????????????????
        DepositoryResponseDTO<DepositoryBaseResponse> responseDTO = handleIdempotent(depositoryRecord);
        if (responseDTO != null) {
            return responseDTO;
        }

        // ????????????????????????
        depositoryRecord = getEntityByRequestNo(requestNo);

        // ???????????????????????????
        String jsonString = JSON.toJSONString(userAutoPreTransactionRequest);
        String reqData = EncryptUtil.encodeUTF8StringBase64(jsonString);

        // ?????? OKHttpClient ?????? Http ???????????????????????????????????????(????????????)??????????????????????????????????????????
        return sendHttpGet("USER_AUTO_PRE_TRANSACTION", reqData, depositoryRecord);
    }

    /**
     * ??????????????????
     *
     * @param loanRequest
     * @return
     */
    @Override
    public DepositoryResponseDTO<DepositoryBaseResponse> confirmLoan(LoanRequest loanRequest) {
        // ?????? DepositoryRecord ????????????????????????????????????
        String requestNo = loanRequest.getRequestNo();
        DepositoryRecord depositoryRecord = new DepositoryRecord()
                .setRequestNo(requestNo) // ?????????????????????
                .setRequestType(DepositoryRequestTypeCode.FULL_LOAN.getCode()) // ??????????????????
                .setObjectType("LoanRequest") // ??????????????????????????????
                .setObjectId(loanRequest.getId()); // ??????????????????????????????

        // ???????????????????????????????????????
        DepositoryResponseDTO<DepositoryBaseResponse> responseDTO = handleIdempotent(depositoryRecord);
        if (responseDTO != null) {
            return responseDTO;
        }

        // ????????????????????????
        depositoryRecord = getEntityByRequestNo(requestNo);

        // ???????????????????????????
        String jsonString = JSON.toJSONString(loanRequest);
        String reqData = EncryptUtil.encodeUTF8StringBase64(jsonString);

        // ?????? OKHttpClient ?????? Http ???????????????????????????????????????(????????????)??????????????????????????????????????????
        return sendHttpGet("CONFIRM_LOAN", reqData, depositoryRecord);
    }

    /**
     * ??????????????????
     *
     * @param modifyProjectStatusDTO
     * @return
     */
    @Override
    public DepositoryResponseDTO<DepositoryBaseResponse> modifyProjectStatus(ModifyProjectStatusDTO modifyProjectStatusDTO) {
        // ?????? DepositoryRecord ????????????????????????????????????
        String requestNo = modifyProjectStatusDTO.getRequestNo();
        DepositoryRecord depositoryRecord = new DepositoryRecord()
                .setRequestNo(requestNo) // ?????????????????????
                .setRequestType(DepositoryRequestTypeCode.MODIFY_STATUS.getCode()) // ??????????????????
                .setObjectType("Project") // ??????????????????????????????
                .setObjectId(modifyProjectStatusDTO.getId()); // ??????????????????????????????

        // ???????????????????????????????????????
        DepositoryResponseDTO<DepositoryBaseResponse> responseDTO = handleIdempotent(depositoryRecord);
        if (responseDTO != null) {
            return responseDTO;
        }

        // ????????????????????????
        depositoryRecord = getEntityByRequestNo(requestNo);

        // ???????????????????????????
        String jsonString = JSON.toJSONString(modifyProjectStatusDTO);
        String reqData = EncryptUtil.encodeUTF8StringBase64(jsonString);

        // ?????? OKHttpClient ?????? Http ???????????????????????????????????????(??????????????????)??????????????????????????????????????????
        return sendHttpGet("MODIFY_PROJECT", reqData, depositoryRecord);
    }

    /**
     * ????????????
     *
     * @param repaymentRequest
     * @return
     */
    @Override
    public DepositoryResponseDTO<DepositoryBaseResponse> confirmRepayment(RepaymentRequest repaymentRequest) {
        // ?????? DepositoryRecord ????????????????????????????????????
        String requestNo = repaymentRequest.getRequestNo();
        DepositoryRecord depositoryRecord = new DepositoryRecord()
                .setRequestNo(requestNo) // ?????????????????????
                .setRequestType(PreprocessBusinessTypeCode.REPAYMENT.getCode()) // ??????????????????
                .setObjectType("Repayment") // ??????????????????????????????
                .setObjectId(repaymentRequest.getId()); // ??????????????????????????????

        // ???????????????????????????????????????
        DepositoryResponseDTO<DepositoryBaseResponse> responseDTO = handleIdempotent(depositoryRecord);
        if (responseDTO != null) {
            return responseDTO;
        }

        // ????????????????????????
        depositoryRecord = getEntityByRequestNo(requestNo);

        // ????????????????????????????????? base64 ??????
        String jsonString = JSON.toJSONString(repaymentRequest);
        String reqData = EncryptUtil.encodeUTF8StringBase64(jsonString);

        // ?????? OKHttpClient ?????? Http ???????????????????????????????????????(????????????)??????????????????????????????????????????
        return sendHttpGet("CONFIRM_REPAYMENT", reqData, depositoryRecord);
    }

    /**
     * ????????????????????????
     *
     * @param rechargeRequest ??????????????????
     * @return
     */
    @Override
    public GatewayRequest createRechargeRecord(RechargeRequest rechargeRequest) {
        /* 1.?????????????????? */
        DepositoryRecord record = new DepositoryRecord();
        // ???????????????
        record.setRequestNo(rechargeRequest.getRequestNo());
        // ????????????
        record.setRequestType(DepositoryRequestTypeCode.RECHARGE.getCode());
        // ??????????????????
        record.setObjectType(CommonConstants.OBJECT_TYPE_CONSUMER);
        // ????????????????????????
        record.setObjectId(rechargeRequest.getId());
        // ????????????
        record.setCreateDate(LocalDateTime.now());
        // ?????????????????????????????????
        record.setRequestStatus(StatusCode.STATUS_OUT.getCode());
        this.save(record); // MP ?????????????????????

        /* 2.???????????????????????????????????? */
        // ????????????????????????json?????????
        String reqData = JSON.toJSONString(rechargeRequest);
        // ??????????????????????????????????????? json ???????????????
        String sign = RSAUtil.sign(reqData, configService.getP2pPrivateKey(), CommonConstants.UTF8);

        // ????????????????????????
        GatewayRequest gatewayRequest = new GatewayRequest();
        // ????????????????????????????????????????????????????????????.pdf???
        gatewayRequest.setServiceName(ServiceNameConstants.NAME_RECHARGE);
        // ???????????????????????????????????????????????????????????????apollo???
        gatewayRequest.setPlatformNo(configService.getP2pCode());
        // ?????????????????????json?????????????????????
        gatewayRequest.setReqData(EncryptUtil.encodeURL(EncryptUtil.encodeUTF8StringBase64(reqData)));
        // ??????
        gatewayRequest.setSignature(EncryptUtil.encodeURL(sign));
        // ????????????????????????????????????apollo???
        gatewayRequest.setDepositoryUrl(configService.getDepositoryUrl() + "/gateway");
        return gatewayRequest;
    }

    /**
     * ????????????????????????
     *
     * @param withdrawRequest ??????????????????
     * @return
     */
    @Override
    public GatewayRequest createWithdrawRecord(WithdrawRequest withdrawRequest) {
        /* 1.?????????????????? */
        DepositoryRecord record = new DepositoryRecord();
        // ???????????????
        record.setRequestNo(withdrawRequest.getRequestNo());
        // ????????????
        record.setRequestType(DepositoryRequestTypeCode.WITHDRAW.getCode());
        // ??????????????????
        record.setObjectType(CommonConstants.OBJECT_TYPE_CONSUMER);
        // ????????????????????????
        record.setObjectId(withdrawRequest.getId());
        // ????????????
        record.setCreateDate(LocalDateTime.now());
        // ?????????????????????????????????
        record.setRequestStatus(StatusCode.STATUS_OUT.getCode());
        this.save(record); // MP ?????????????????????

        /* 2.???????????????????????????????????? */
        // ????????????????????????json?????????
        String reqData = JSON.toJSONString(withdrawRequest);
        // ??????????????????????????????????????? json ???????????????
        String sign = RSAUtil.sign(reqData, configService.getP2pPrivateKey(), CommonConstants.UTF8);

        // ????????????????????????
        GatewayRequest gatewayRequest = new GatewayRequest();
        // ????????????????????????????????????????????????????????????.pdf???
        gatewayRequest.setServiceName(ServiceNameConstants.NAME_WITHDRAW);
        // ???????????????????????????????????????????????????????????????apollo???
        gatewayRequest.setPlatformNo(configService.getP2pCode());
        // ?????????????????????json?????????????????????
        gatewayRequest.setReqData(EncryptUtil.encodeURL(EncryptUtil.encodeUTF8StringBase64(reqData)));
        // ??????
        gatewayRequest.setSignature(EncryptUtil.encodeURL(sign));
        // ????????????????????????????????????apollo???
        gatewayRequest.setDepositoryUrl(configService.getDepositoryUrl() + "/gateway");
        return gatewayRequest;
    }
}

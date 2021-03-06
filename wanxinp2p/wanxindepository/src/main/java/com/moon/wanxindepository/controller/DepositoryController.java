package com.moon.wanxindepository.controller;

import com.moon.wanxindepository.common.constant.AuditStatusCode;
import com.moon.wanxindepository.common.constant.TransactionStatusCode;
import com.moon.wanxindepository.common.domain.BusinessException;
import com.moon.wanxindepository.common.domain.PageVO;
import com.moon.wanxindepository.common.util.EncryptUtil;
import com.moon.wanxindepository.model.BalanceDetailsDTO;
import com.moon.wanxindepository.model.BankCardDTO;
import com.moon.wanxindepository.model.BankCardQuery;
import com.moon.wanxindepository.model.BankCardRequest;
import com.moon.wanxindepository.model.LocalResponse;
import com.moon.wanxindepository.model.PersonalRegisterRequest;
import com.moon.wanxindepository.model.PersonalRegisterResponse;
import com.moon.wanxindepository.model.RechargeRequest;
import com.moon.wanxindepository.model.RechargeResponse;
import com.moon.wanxindepository.model.UserDTO;
import com.moon.wanxindepository.model.WithdrawRequest;
import com.moon.wanxindepository.model.WithdrawResponse;
import com.moon.wanxindepository.service.BalanceDetailsService;
import com.moon.wanxindepository.service.BankCardService;
import com.moon.wanxindepository.service.RechargeDetailsService;
import com.moon.wanxindepository.service.UserService;
import com.moon.wanxindepository.service.WithdrawDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

/**
 * ??????????????????API
 */
@Slf4j
@RestController
@Api(value = "??????????????????API", tags = "Depository", description = "??????????????????API")
public class DepositoryController {

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceDetailsService balanceDetailsService;

    @Autowired
    private RechargeDetailsService rechargeDetailsService;

    @Autowired
    private WithdrawDetailsService withdrawDetailsService;


    @ApiOperation("?????????????????????")
    @ApiImplicitParam(name = "mobile", value = "?????????", required = true, dataType = "String")
    @GetMapping("/smscode/{mobile}")
    public LocalResponse<String> getSMSCode(@PathVariable String mobile) {
        return LocalResponse.success();
    }

    @ApiOperation("???????????????")
    @ApiImplicitParam(name = "bankCardRequest", value = "???????????????", required = true, dataType = "BankCardDTO", paramType = "body")
    @PostMapping("/bank-cards")
    public LocalResponse<String> createBankCard(@RequestBody BankCardRequest bankCardRequest) {
        bankCardService.createBankCard(bankCardRequest);
        return LocalResponse.success();
    }

    @ApiOperation("?????????????????????")
    @ApiImplicitParam(name = "cardNumber", value = "??????", required = true, dataType = "String")
    @GetMapping("/bank-cards/card-number/{cardNumber}")
    public LocalResponse<BigDecimal> getBalance(@PathVariable String cardNumber) {
        return LocalResponse.success(bankCardService.getBalance(cardNumber));
    }

    @ApiOperation("????????????????????????")
    @ApiImplicitParam(name = "userNo", value = "????????????", required = true, dataType = "String")
    @GetMapping("/balance-details/{userNo}")
    public BalanceDetailsDTO getP2PBalanceDetails(@PathVariable String userNo) {
        BalanceDetailsDTO balanceDetailsDTO = balanceDetailsService.getP2PBalanceDetails(userNo);
        return balanceDetailsDTO;
    }

    @ApiOperation("?????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankCardQuery", value = "?????????????????????", dataType = "WithdrawRecordQueryDTO", paramType = "body"),
            @ApiImplicitParam(name = "pageNo", value = "??????", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "????????????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "??????", dataType = "String", paramType = "query")})
    @PostMapping("/bank-cards/q")
    public LocalResponse<PageVO<BankCardDTO>> queryBankCards(@RequestBody BankCardQuery bankCardQuery,
                                                             @RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam String sortBy,
                                                             @RequestParam String order) {
        PageVO<BankCardDTO> pageVO = bankCardService.queryBankCards(bankCardQuery, pageNo, pageSize, sortBy, order);
        return LocalResponse.success(pageVO);
    }


    @ApiOperation("????????????????????????")
    @ApiImplicitParam(name = "userDTO", value = "????????????", required = true, dataType = "UserDTO", paramType = "body")
    @PostMapping("/users/password")
    public LocalResponse<Integer> verifyPassword(@RequestBody UserDTO userDTO) {
        return LocalResponse.success(userService.verifyPassword(userDTO.getUserNo(), userDTO.getPassword()) ? 1 : 0);
    }

    @ApiOperation("????????????")
    @ApiImplicitParam(name = "personalRegisterRequest", value = "????????????", required = true, dataType = "PersonalRegisterRequest", paramType = "form")
    @PostMapping(value = "/trans/users")
    public ModelAndView createUser(PersonalRegisterRequest personalRegisterRequest) {
        int code;
        String msg = "";
        try {
            PersonalRegisterResponse response = userService.createUser(personalRegisterRequest);
            code = response.getAuditStatus().equals(AuditStatusCode.PASSED.getCode()) ? 0 : 1;
        } catch (BusinessException e) {
            code = 2;
            msg = e.getMessage();
        }
        return new ModelAndView(
                "redirect:" + addRedirectAttributes(personalRegisterRequest.getCallbackUrl(), code, msg));
    }

    @ApiOperation("????????????")
    @ApiImplicitParam(name = "rechargeRequest", value = "????????????", required = true, dataType = "RechargeRequest", paramType = "form")
    @PostMapping(value = "/trans/recharge-details")
    public ModelAndView recharge(RechargeRequest rechargeRequest) {
        int code;
        String msg = "";
        try {
            RechargeResponse response = rechargeDetailsService.recharge(rechargeRequest);
            code = response.getTransactionStatus().equals(TransactionStatusCode.SUCCESS.getCode()) ? 0 : 1;
        } catch (BusinessException e) {
            code = 2;
            msg = e.getMessage();
        }
        return new ModelAndView("redirect:" + addRedirectAttributes(rechargeRequest.getCallbackUrl(), code, msg));
    }

    @ApiOperation("????????????")
    @ApiImplicitParam(name = "withdrawRequest", value = "????????????", required = true, dataType = "WithdrawRequest", paramType = "form")
    @PostMapping(value = "/trans/withdraw-details")
    public ModelAndView withdraw(WithdrawRequest withdrawRequest) {
        int code;
        String msg = "";
        try {
            WithdrawResponse response = withdrawDetailsService.withDraw(withdrawRequest);
            code = response.getTransactionStatus().equals(TransactionStatusCode.SUCCESS.getCode()) ? 0 : 1;
        } catch (BusinessException e) {
            code = 2;
            msg = e.getMessage();
        }
        return new ModelAndView("redirect:" + addRedirectAttributes(withdrawRequest.getCallbackUrl(), code, msg));
    }

    /**
     * ??????url???????????????
     *
     * @param url
     * @param code
     * @param msg
     * @return
     */
    private String addRedirectAttributes(String url, int code, String msg) {
        StringBuilder callbackUrl = new StringBuilder(url);
        callbackUrl.append("&code=").append(code);
        if (StringUtils.isNotBlank(msg)) {
            callbackUrl.append("&msg=").append(EncryptUtil.encodeURL(msg));
        }
        return callbackUrl.toString();
    }

}

package com.moon.wanxinp2p.api.depository.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 放款明细请求信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-18 16:15
 * @description
 */
@Data
public class LoanDetailRequest {

    /**
     * 放款金额
     */
    private BigDecimal amount;
    /**
     * 预处理业务流水号
     */
    private String preRequestNo;
}

package com.moon.wanxinp2p.api.repayment.model;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.TenderDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 标的还款信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-18 16:47
 * @description
 */
@Data
public class ProjectWithTendersDTO {

    /**
     * 标的信息
     */
    private ProjectDTO project;
    /**
     * 标的对应的所有投标记录
     */
    private List<TenderDTO> tenders;

    /**
     * 投资人让出利率 ( 投资人让利 )
     */
    private BigDecimal commissionInvestorAnnualRate;

    /**
     * 借款人给平台的利率 ( 借款人让利 )
     */
    private BigDecimal commissionBorrowerAnnualRate;

}

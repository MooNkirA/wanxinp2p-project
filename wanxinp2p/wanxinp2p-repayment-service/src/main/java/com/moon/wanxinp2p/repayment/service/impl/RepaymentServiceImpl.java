package com.moon.wanxinp2p.repayment.service.impl;

import com.moon.wanxinp2p.api.repayment.model.ProjectWithTendersDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.TenderDTO;
import com.moon.wanxinp2p.common.enums.DepositoryReturnCode;
import com.moon.wanxinp2p.common.enums.RepaymentWayCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.DateUtil;
import com.moon.wanxinp2p.repayment.common.enums.RepaymentErrorCode;
import com.moon.wanxinp2p.repayment.entity.ReceivablePlan;
import com.moon.wanxinp2p.repayment.entity.RepaymentPlan;
import com.moon.wanxinp2p.repayment.mapper.PlanMapper;
import com.moon.wanxinp2p.repayment.mapper.ReceivablePlanMapper;
import com.moon.wanxinp2p.repayment.model.EqualInterestRepayment;
import com.moon.wanxinp2p.repayment.service.RepaymentService;
import com.moon.wanxinp2p.repayment.util.RepaymentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 还款业务接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-19 22:07
 * @description
 */
@Service
public class RepaymentServiceImpl implements RepaymentService {

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private ReceivablePlanMapper receivablePlanMapper;

    /**
     * 启动还款
     *
     * @param projectWithTendersDTO
     * @return
     */
    @Override
    public String startRepayment(ProjectWithTendersDTO projectWithTendersDTO) {
        /* 1. 生成借款人还款计划 */
        // 获取标的信息
        ProjectDTO projectDTO = projectWithTendersDTO.getProject();
        // 获取投标信息
        List<TenderDTO> tenderDTOList = projectWithTendersDTO.getTenders();

        // 计算还款的月数
        int month = Double.valueOf(Math.ceil(projectDTO.getPeriod() / 30.0)).intValue();

        // 获取还款方式（目前项目只针对等额本息）
        String repaymentWay = projectDTO.getRepaymentWay();
        // 生成还款计划（项目目前只支持“等额本息”还款方式，所以使其他方式暂时抛出业务异常，以后再进行开发）
        if (!RepaymentWayCode.FIXED_REPAYMENT.getCode().equals(repaymentWay)) {
            throw new BusinessException(RepaymentErrorCode.E_170104);
        }
        // 还款计划
        EqualInterestRepayment fixedRepayment = RepaymentUtil.fixedRepayment(projectDTO.getAmount(), projectDTO.getBorrowerAnnualRate(), month, projectDTO.getCommissionAnnualRate());
        // 保存还款计划
        List<RepaymentPlan> planList = saveRepaymentPlan(projectDTO, fixedRepayment);

        /* 2. 生成投资人应收明细 */
        // 根据投标信息生成应收明细
        tenderDTOList.forEach(tenderDTO -> {
            // 生成应收明细列表（目前项目只针对等额本息）
            EqualInterestRepayment receipts = RepaymentUtil.fixedRepayment(tenderDTO.getAmount(), tenderDTO.getProjectAnnualRate(), month, projectWithTendersDTO.getCommissionInvestorAnnualRate());

            // 保存应收明细到数据库，每个投标人每次应收生成一条应收明细记录
            planList.forEach(plan -> saveRreceivablePlan(plan, tenderDTO, receipts));
        });


        return DepositoryReturnCode.RETURN_CODE_00000.getCode();
    }

    /**
     * 保存还款计划到数据库，并返回还款计划列表数据
     *
     * @param projectDTO
     * @param fixedRepayment
     * @return
     */
    private List<RepaymentPlan> saveRepaymentPlan(ProjectDTO projectDTO, EqualInterestRepayment fixedRepayment) {
        // 获取每期利息
        final Map<Integer, BigDecimal> interestMap = fixedRepayment.getInterestMap();
        // 平台收取利息
        final Map<Integer, BigDecimal> commissionMap = fixedRepayment.getCommissionMap();
        // 获取每期本金，将循环生成还款的记录列表。批量新增记录到 repayment_plan 借款人还款计划表后，并返回
        return fixedRepayment.getPrincipalMap().entrySet()
                .stream()
                .map(entry -> {
                    Integer key = entry.getKey();
                    // 创建还款计划实体类
                    final RepaymentPlan repaymentPlan = new RepaymentPlan();
                    // 标的id
                    repaymentPlan.setProjectId(projectDTO.getId());
                    // 发标人用户标识
                    repaymentPlan.setConsumerId(projectDTO.getConsumerId());
                    // 发标人用户编码
                    repaymentPlan.setUserNo(projectDTO.getUserNo());
                    // 标的编码
                    repaymentPlan.setProjectNo(projectDTO.getProjectNo());
                    // 期数
                    repaymentPlan.setNumberOfPeriods(key);
                    // 当期还款利息
                    repaymentPlan.setInterest(interestMap.get(key));
                    // 还款本金
                    repaymentPlan.setPrincipal(entry.getValue());
                    // 本息 = 本金 + 利息
                    repaymentPlan.setAmount(repaymentPlan.getPrincipal().add(repaymentPlan.getInterest()));
                    // 应还时间 = 当前时间 + 期数( 单位月 )
                    repaymentPlan.setShouldRepaymentDate(DateUtil.localDateTimeAddMonth(DateUtil.now(), key));
                    // 应还状态, 当前业务为待还
                    repaymentPlan.setRepaymentStatus("0");
                    // 计划创建时间
                    repaymentPlan.setCreateDate(DateUtil.now());
                    // 设置平台佣金( 借款人让利 ) 注意这个地方是 具体佣金
                    repaymentPlan.setCommission(commissionMap.get(key));

                    // 保存到数据库（这里为了方便，循环插入，实际项目尽量避免这种操作）
                    planMapper.insert(repaymentPlan);
                    return repaymentPlan;
                }).collect(Collectors.toList());
    }

    /**
     * 保存应收明细到数据库
     *
     * @param plan
     * @param tender
     * @param receipts
     */
    private void saveRreceivablePlan(RepaymentPlan plan, TenderDTO tender, EqualInterestRepayment receipts) {
        // 应收本金
        final Map<Integer, BigDecimal> principalMap = receipts.getPrincipalMap();
        // 应收利息
        final Map<Integer, BigDecimal> interestMap = receipts.getInterestMap();
        // 平台收取利息
        final Map<Integer, BigDecimal> commissionMap = receipts.getCommissionMap();

        // 创建投资人应收明细实体对象，补充相关属性
        ReceivablePlan receivablePlan = new ReceivablePlan();
        // 投标信息标识
        receivablePlan.setTenderId(tender.getId());
        // 设置期数
        receivablePlan.setNumberOfPeriods(plan.getNumberOfPeriods());
        // 投标人用户标识
        receivablePlan.setConsumerId(tender.getConsumerId());
        // 投标人用户编码
        receivablePlan.setUserNo(tender.getUserNo());
        // 还款计划项标识
        receivablePlan.setRepaymentId(plan.getId());
        // 应收利息
        receivablePlan.setInterest(interestMap.get(plan.getNumberOfPeriods()));
        // 应收本金
        receivablePlan.setPrincipal(principalMap.get(plan.getNumberOfPeriods()));
        // 应收本息 = 应收本金 + 应收利息
        receivablePlan.setAmount(receivablePlan.getInterest().add(receivablePlan.getPrincipal()));
        // 应收时间
        receivablePlan.setShouldReceivableDate(plan.getShouldRepaymentDate());
        // 应收状态, 当前业务为未收
        receivablePlan.setReceivableStatus(0);
        // 创建时间
        receivablePlan.setCreateDate(DateUtil.now());
        // 设置投资人让利, 注意这个地方是具体佣金
        receivablePlan.setCommission(commissionMap.get(plan.getNumberOfPeriods()));
        // 保存到数据库
        receivablePlanMapper.insert(receivablePlan);
    }
}

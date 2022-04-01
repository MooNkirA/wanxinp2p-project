package com.moon.wanxinp2p.repayment.service;

import com.moon.wanxinp2p.api.repayment.model.ProjectWithTendersDTO;
import com.moon.wanxinp2p.api.repayment.model.RepaymentRequest;
import com.moon.wanxinp2p.repayment.entity.RepaymentDetail;
import com.moon.wanxinp2p.repayment.entity.RepaymentPlan;

import java.util.List;

/**
 * 还款业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-19 22:04
 * @description
 */
public interface RepaymentService {

    /**
     * 启动还款
     *
     * @param projectWithTendersDTO
     * @return
     */
    String startRepayment(ProjectWithTendersDTO projectWithTendersDTO);

    /**
     * 执行还款
     *
     * @param date 还款日期
     */
    @Deprecated
    void executeRepayment(String date);

    /**
     * 查询所有到期的还款计划
     *
     * @param date 格式：yyyy-MM-dd
     * @return
     */
    List<RepaymentPlan> selectDueRepayment(String date);

    /**
     * 执行还款
     *
     * @param date          还款日期
     * @param shardingCount 分片数
     * @param shardingItem  当前分片号
     */
    void executeRepayment(String date, int shardingCount, int shardingItem);

    /**
     * 查询所有到期的还款计划
     *
     * @param date          格式：yyyy-MM-dd
     * @param shardingCount 分片数
     * @param shardingItem  当前分片号
     * @return
     */
    List<RepaymentPlan> selectDueRepayment(String date, int shardingCount, int shardingItem);

    /**
     * 根据还款计划生成还款明细并保存
     *
     * @param repaymentPlan 还款计划
     * @return
     */
    RepaymentDetail saveRepaymentDetail(RepaymentPlan repaymentPlan);

    /**
     * 还款预处理：冻结借款人应还金额
     *
     * @param repaymentPlan 还款计划
     * @param preRequestNo  请求流水号
     * @return
     */
    boolean preRepayment(RepaymentPlan repaymentPlan, String preRequestNo);

    /**
     * 确认还款处理
     *
     * @param repaymentPlan
     * @param repaymentRequest
     * @return
     */
    boolean confirmRepayment(RepaymentPlan repaymentPlan, RepaymentRequest repaymentRequest);

    /**
     * 远程调用确认还款接口
     *
     * @param repaymentPlan
     * @param repaymentRequest
     */
    void invokeConfirmRepayment(RepaymentPlan repaymentPlan, RepaymentRequest repaymentRequest);

    /**
     * 查询还款人相关信息，并调用发送短信接口进行还款提醒
     *
     * @param date 还款日期
     */
    void sendRepaymentNotify(String date);
}

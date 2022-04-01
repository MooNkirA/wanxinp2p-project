package com.moon.wanxinp2p.repayment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moon.wanxinp2p.repayment.entity.RepaymentPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 借款人还款计划 Mapper 接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-19 21:58
 * @description
 */
public interface PlanMapper extends BaseMapper<RepaymentPlan> {

    /**
     * 根据日期查询所有还款计划
     *
     * @param date
     * @return
     */
    List<RepaymentPlan> selectDueRepayment(@Param("date") String date);

    /**
     * 根据日期与服务分片，查询所有还款计划
     *
     * @param date  日期
     * @param count 分片数量
     * @param item  当前作业的分片号
     * @return
     */
    List<RepaymentPlan> selectDueRepaymentBySharding(@Param("date") String date, @Param("shardingCount") int count, @Param("shardingItem") int item);

}

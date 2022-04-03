package com.moon.wanxinp2p.repayment.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.moon.wanxinp2p.repayment.service.RepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 还款定时任务执行类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-30 15:13
 * @description
 */
@Component
public class RepaymentJob implements SimpleJob {

    @Autowired
    private RepaymentService repaymentService;

    /**
     * 执行作业.
     *
     * @param shardingContext 分片上下文
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        // 从 Elastic-Job 分片上下文中，获取分片总数
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        // 获取当前分片项
        int shardingItem = shardingContext.getShardingItem();

        // 调用业务层执行还款任务
        repaymentService.executeRepayment(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE), shardingTotalCount, shardingItem);

        /* 调用业务层执行还款短信提醒，需要提前一天通知，所以查询还款的日期是明天 */
        repaymentService.sendRepaymentNotify(LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

}

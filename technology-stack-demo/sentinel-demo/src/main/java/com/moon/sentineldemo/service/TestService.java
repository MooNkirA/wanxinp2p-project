package com.moon.sentineldemo.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试业务层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-09 16:07
 * @description
 */
@Service
public class TestService {

    /**
     * 标识资源
     *
     * @return
     */
    @SentinelResource(value = "hello", blockHandler = "exceptionHandler")
    public String hello() {
        return "Hello Sentinel";
    }

    /**
     * 资源被限制后的执行的逻辑
     *
     * @param ex
     * @return
     */
    public String exceptionHandler(BlockException ex) {
        ex.printStackTrace();
        return "系统繁忙，请稍候";
    }

    /**
     * 定义了限流资源和限流规则，使用 @PostConstruct 注解，让类在构造方法执行完毕后会自动运行
     */
    @PostConstruct
    public void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        // 封装限流规则
        FlowRule rule = new FlowRule();
        rule.setResource("hello");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(2);

        rules.add(rule);

        // 加载限流规则，使其启用
        FlowRuleManager.loadRules(rules);
    }

}

package com.moon.wanxinp2p.repayment.controller;

import com.moon.wanxinp2p.api.repayment.RepaymentApi;
import com.moon.wanxinp2p.api.repayment.model.ProjectWithTendersDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.repayment.service.RepaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 还款微服务控制类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-18 16:54
 * @description
 */
@RestController
public class RepaymentController implements RepaymentApi {

    @Autowired
    private RepaymentService repaymentService;

    /**
     * 启动还款
     *
     * @param projectWithTendersDTO
     * @return
     */
    @ApiOperation("启动还款")
    @ApiImplicitParam(name = "projectWithTendersDTO", value = "通过id获取标的信息", required = true,
            dataType = "ProjectWithTendersDTO", paramType = "body")
    @PostMapping("/l/start-repayment")
    @Override
    public RestResponse<String> startRepayment(@RequestBody ProjectWithTendersDTO projectWithTendersDTO) {
        return RestResponse.success(repaymentService.startRepayment(projectWithTendersDTO));
    }

    /**
     * 手动触发用户还款
     *
     * @param date
     */
    @ApiOperation("手动触发用户还款")
    @GetMapping("/execute-repayment/{date}")
    public void executeRepayment(@PathVariable String date) {
        repaymentService.executeRepayment(date);
    }
}

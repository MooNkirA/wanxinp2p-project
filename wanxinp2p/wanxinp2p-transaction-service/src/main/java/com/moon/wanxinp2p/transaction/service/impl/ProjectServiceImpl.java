package com.moon.wanxinp2p.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CodePrefixCode;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import com.moon.wanxinp2p.common.enums.ProjectCode;
import com.moon.wanxinp2p.common.enums.RepaymentWayCode;
import com.moon.wanxinp2p.common.enums.StatusCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.CodeNoUtil;
import com.moon.wanxinp2p.transaction.agent.ConsumerApiAgent;
import com.moon.wanxinp2p.transaction.common.enums.ProjectTypeCode;
import com.moon.wanxinp2p.transaction.common.utils.SecurityUtil;
import com.moon.wanxinp2p.transaction.entity.Project;
import com.moon.wanxinp2p.transaction.mapper.ProjectMapper;
import com.moon.wanxinp2p.transaction.model.LoginUser;
import com.moon.wanxinp2p.transaction.service.ConfigService;
import com.moon.wanxinp2p.transaction.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 交易中心 标的服务接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-05 17:44
 * @description
 */
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ConsumerApiAgent consumerApiAgent;

    @Autowired
    private ConfigService configService;

    /**
     * 创建标的
     *
     * @param projectDTO
     * @return ProjectDTO
     */
    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        // 通过 Spring Security 工具类，获取网关转发请求域中的用户实例
        LoginUser user = SecurityUtil.getUser();
        // 远程调用用户服务，根据手机号获取完整的用户信息
        RestResponse<ConsumerDTO> response = consumerApiAgent.getCurrConsumer(user.getMobile());
        ConsumerDTO consumer = response.getResult();
        if (consumer == null) {
            // 用户不存在
            throw new BusinessException(CommonErrorCode.E_140101);
        }

        // 创建标的实例
        Project project = new Project();
        // 设置标的相关字段
        project.setConsumerId(consumer.getId());
        project.setUserNo(consumer.getUserNo());
        // 生成标的编码
        project.setProjectNo(CodeNoUtil.getNo(CodePrefixCode.CODE_PROJECT_PREFIX));
        // 标的状态修改
        project.setProjectStatus(ProjectCode.COLLECTING.getCode());
        // 标的可用状态修改, 未同步
        project.setStatus(StatusCode.STATUS_OUT.getCode());
        // 设置标的创建时间
        project.setCreateDate(LocalDateTime.now());
        // 设置还款方式
        project.setRepaymentWay(RepaymentWayCode.FIXED_REPAYMENT.getCode());
        // 设置标的类型
        project.setType(ProjectTypeCode.TYPE_CODE_NEW.getCode());
        // 年化利率(借款人视图)，在 apollo 中配置
        project.setBorrowerAnnualRate(configService.getBorrowerAnnualRate());
        // 年化利率(投资人视图)，在 apollo 中配置
        project.setAnnualRate(configService.getAnnualRate());
        // 年化利率(平台佣金，利差)，在 apollo 中配置
        project.setCommissionAnnualRate(configService.getCommissionAnnualRate());
        // 债权转让
        project.setIsAssignment(0);
        // 判断男女
        String sex = Integer.parseInt(consumer.getIdNumber().substring(16, 17)) % 2 == 0 ? "女士" : "先生";
        // 构造借款次数查询条件
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Project::getConsumerId, consumer.getId());
        // 设置标的名字, 姓名+性别+第N次借款（已借款次数+1）
        project.setName(consumer.getFullname() + sex + "第" + (count(queryWrapper) + 1) + "次借款");

        // 保存
        save(project);
        // 复制响应
        BeanUtils.copyProperties(project, projectDTO);
        return projectDTO;
    }

}

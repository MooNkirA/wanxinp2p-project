package com.moon.wanxinp2p.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectInvestDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import com.moon.wanxinp2p.api.transaction.model.TenderDTO;
import com.moon.wanxinp2p.api.transaction.model.TenderOverviewDTO;
import com.moon.wanxinp2p.common.domain.PageVO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CodePrefixCode;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import com.moon.wanxinp2p.common.enums.DepositoryReturnCode;
import com.moon.wanxinp2p.common.enums.ProjectCode;
import com.moon.wanxinp2p.common.enums.RepaymentWayCode;
import com.moon.wanxinp2p.common.enums.StatusCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.CodeNoUtil;
import com.moon.wanxinp2p.common.util.CommonUtil;
import com.moon.wanxinp2p.transaction.agent.ConsumerApiAgent;
import com.moon.wanxinp2p.transaction.agent.ContentSearchApiAgent;
import com.moon.wanxinp2p.transaction.agent.DepositoryAgentApiAgent;
import com.moon.wanxinp2p.transaction.common.enums.ProjectTypeCode;
import com.moon.wanxinp2p.transaction.common.enums.TransactionErrorCode;
import com.moon.wanxinp2p.transaction.common.utils.SecurityUtil;
import com.moon.wanxinp2p.transaction.entity.Project;
import com.moon.wanxinp2p.transaction.entity.Tender;
import com.moon.wanxinp2p.transaction.mapper.ProjectMapper;
import com.moon.wanxinp2p.transaction.mapper.TenderMapper;
import com.moon.wanxinp2p.transaction.model.LoginUser;
import com.moon.wanxinp2p.transaction.service.ConfigService;
import com.moon.wanxinp2p.transaction.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private DepositoryAgentApiAgent depositoryAgentApiAgent;

    @Autowired
    private ContentSearchApiAgent contentSearchApiAgent;

    @Autowired
    private TenderMapper tenderMapper;

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

    /**
     * 根据分页条件检索标的信息
     *
     * @param projectQueryDTO 封装查询条件
     * @param order           排序的方式
     * @param pageNo          当前页
     * @param pageSize        每页大小
     * @param sortBy          排序的字段
     * @return
     */
    @Override
    public PageVO<ProjectDTO> queryProjectsByQueryDTO(ProjectQueryDTO projectQueryDTO, String order,
                                                      Integer pageNo, Integer pageSize, String sortBy) {
        // 构建查询条件
        QueryWrapper<Project> queryWrapper = createQueryWrapper(projectQueryDTO, order, sortBy);

        // 构造分页对象
        Page<Project> page = new Page<>(pageNo, pageSize);

        // 执行查询
        IPage<Project> iPage = page(page, queryWrapper);

        // 结果类型转换
        List<ProjectDTO> projectDTOList = convertProjectEntitysToDTOs(iPage.getRecords());
        // 封装结果
        return new PageVO<>(projectDTOList, iPage.getTotal(), pageNo, pageSize);
    }

    /**
     * 构建查询条件
     *
     * @param projectQueryDTO
     * @param order
     * @param sortBy
     * @return
     */
    private QueryWrapper<Project> createQueryWrapper(ProjectQueryDTO projectQueryDTO, String order, String sortBy) {
        // 构造查询条件
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Project> lambdaQueryWrapper = queryWrapper.lambda();

        // 标的类型
        if (StringUtils.hasText(projectQueryDTO.getType())) {
            lambdaQueryWrapper.eq(Project::getType, projectQueryDTO.getType());
        }
        // 起止年化利率(投资人) -- 区间
        if (null != projectQueryDTO.getStartAnnualRate()) {
            lambdaQueryWrapper.ge(Project::getAnnualRate, projectQueryDTO.getStartAnnualRate());
        }
        if (null != projectQueryDTO.getEndAnnualRate()) {
            lambdaQueryWrapper.le(Project::getAnnualRate, projectQueryDTO.getEndAnnualRate());
        }
        // 借款期限 -- 区间
        if (null != projectQueryDTO.getStartPeriod()) {
            lambdaQueryWrapper.ge(Project::getPeriod, projectQueryDTO.getStartPeriod());
        }
        if (null != projectQueryDTO.getEndPeriod()) {
            lambdaQueryWrapper.le(Project::getPeriod, projectQueryDTO.getEndPeriod());
        }
        // 标的状态
        if (StringUtils.hasText(projectQueryDTO.getProjectStatus())) {
            lambdaQueryWrapper.eq(Project::getProjectStatus,
                    projectQueryDTO.getProjectStatus());
        }

        // 排序
        if (StringUtils.hasText(order) && StringUtils.hasText(sortBy)) {
            if ("asc".equalsIgnoreCase(order)) {
                queryWrapper.orderByAsc(sortBy);
            } else if ("desc".equalsIgnoreCase(order)) {
                queryWrapper.orderByDesc(sortBy);
            }
        } else {
            // 没有上送排序规则，则按创建日期倒序
            lambdaQueryWrapper.orderByDesc(Project::getCreateDate);
        }

        return queryWrapper;
    }

    /**
     * entity 转 dto
     *
     * @param projectList
     * @return
     */
    private List<ProjectDTO> convertProjectEntitysToDTOs(List<Project> projectList) {
        if (projectList == null) {
            return null;
        }
        return projectList.stream().map(p -> {
            ProjectDTO dto = new ProjectDTO();
            BeanUtils.copyProperties(p, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 管理员审核标的信息
     *
     * @param id
     * @param approveStatus
     * @return
     */
    @Override
    public String projectsApprovalStatus(Long id, String approveStatus) {
        // 根据 id 查询标的信息
        Project project = this.getById(id);
        // 转在 dto 传输对象
        ProjectDTO dto = new ProjectDTO();
        BeanUtils.copyProperties(project, dto);

        // 生成流水号(不存在才生成)
        if (StringUtils.isEmpty(dto.getRequestNo())) {
            dto.setRequestNo(CodeNoUtil.getNo(CodePrefixCode.CODE_REQUEST_PREFIX));
            // 更新 project_0/project_1 表的 REQUEST_NO 字段
            update(Wrappers.<Project>lambdaUpdate()
                    .set(Project::getRequestNo, dto.getRequestNo())
                    .eq(Project::getId, id)
            );
        }

        // 通过 feign 远程访问存管代理服务接口，传递标的信息
        RestResponse<String> response = depositoryAgentApiAgent.createProject(dto);

        // 失败则抛异常（根据银行存管系统文档返回说明，成功则返回"00000"）
        if (!DepositoryReturnCode.RETURN_CODE_00000.getCode().equals(response.getResult())) {
            throw new BusinessException(TransactionErrorCode.E_150113);
        }

        // 响应结果是成功，则修改状态
        update(Wrappers.<Project>lambdaUpdate()
                .set(Project::getStatus, Integer.parseInt(approveStatus))
                .eq(Project::getId, id)
        );
        return "success";
    }

    /**
     * 标的信息检索
     *
     * @param projectQueryDTO
     * @param order
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    @Override
    public PageVO<ProjectDTO> queryProjects(ProjectQueryDTO projectQueryDTO, String order,
                                            Integer pageNo, Integer pageSize, String sortBy) {
        RestResponse<PageVO<ProjectDTO>> response = contentSearchApiAgent
                .queryProjectIndex(projectQueryDTO, pageNo, pageSize, sortBy, order);
        if (response.isSuccessful()) {
            return response.getResult();
        } else {
            log.error("标的信息检索错误：{}", response.getMsg());
            throw new BusinessException(CommonErrorCode.UNKOWN);
        }
    }

    /**
     * 通过ids获取多个标的
     *
     * @param ids 多个标的id字符串，不同id之间使用逗号分隔
     * @return
     */
    @Override
    public List<ProjectDTO> queryProjectsIds(String ids) {
        // 查询标的信息
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        // 将多个标的id字符串转成集合
        List<Long> list = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        // 组装 id范围查询条件 ... where id in (1,2,3,...)
        queryWrapper.lambda().in(Project::getId, list);
        // 查询
        List<Project> projects = this.list(queryWrapper);

        // 循环转成dto类型
        return projects.stream().map(p -> {
            ProjectDTO dto = new ProjectDTO();
            BeanUtils.copyProperties(p, dto);

            // 获取剩余额度
            dto.setRemainingAmount(this.getProjectRemainingAmount(p));

            // 查询已出借人数
            Integer tenderCount = tenderMapper.selectCount(
                    Wrappers.<Tender>lambdaQuery().eq(Tender::getProjectId, p.getId())
            );
            dto.setTenderCount(tenderCount);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取标的剩余可投额度
     *
     * @param project
     * @return
     */
    private BigDecimal getProjectRemainingAmount(Project project) {
        // 根据标的id在投标表查询已投金额
        List<BigDecimal> decimalList =
                tenderMapper.selectAmountInvestedByProjectId(project.getId());
        // 求和结果集
        BigDecimal amountInvested = decimalList.stream()
                .reduce(new BigDecimal("0.0"), BigDecimal::add);
        // 得到剩余额度
        return project.getAmount().subtract(amountInvested);
    }

    /**
     * 根据标的id查询投标记录
     *
     * @param id 标的id
     * @return
     */
    @Override
    public List<TenderOverviewDTO> queryTendersByProjectId(Long id) {
        // 使用 mp 业务方法，根据标的id查询 tender_0/tender_1 表
        List<Tender> tenderList = tenderMapper.selectList(Wrappers.<Tender>lambdaQuery().eq(Tender::getProjectId, id));

        // 转换成 dto 类型，并返回
        return tenderList.stream().map(t -> {
            TenderOverviewDTO dto = new TenderOverviewDTO();
            BeanUtils.copyProperties(t, dto);
            // 使用工具类对用户的手机号进行隐藏保护
            dto.setConsumerUsername(CommonUtil.hiddenMobile(t.getConsumerUsername()));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 用户投标
     *
     * @param projectInvestDTO
     * @return
     */
    @Override
    public TenderDTO createTender(ProjectInvestDTO projectInvestDTO) {
        return null;
    }
}

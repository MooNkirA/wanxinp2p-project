package com.moon.wanxindepository.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxindepository.common.constant.ProjectStatusCode;
import com.moon.wanxindepository.common.util.EncryptUtil;
import com.moon.wanxindepository.entity.Project;
import com.moon.wanxindepository.mapper.ProjectMapper;
import com.moon.wanxindepository.model.CreateProjectRequest;
import com.moon.wanxindepository.model.CreateProjectResponse;
import com.moon.wanxindepository.model.ModifyProjectRequest;
import com.moon.wanxindepository.model.ModifyProjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 标的信息表 服务实现类
 */
@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Override
    public CreateProjectResponse createProject(String reqData) {
        String decodeReqData = EncryptUtil.decodeUTF8StringBase64(reqData);
        CreateProjectRequest request = JSON.parseObject(decodeReqData, CreateProjectRequest.class);
        String requestNo = request.getRequestNo();

        //保存标的信息
        Project project = new Project();
        BeanUtils.copyProperties(request, project);
        project.setName(request.getProjectName());
        project.setDescription(request.getProjectDesc());
        project.setType(request.getProjectType());
        project.setPeriod(request.getProjectPeriod());
        project.setBorrowerAnnualRate(request.getAnnualRate());
        project.setAmount(request.getProjectAmount());
        project.setProjectStatus(ProjectStatusCode.RAISE.getCode());
        save(project);

        return new CreateProjectResponse(requestNo);
    }

    @Override
    public ModifyProjectResponse modifyProject(String reqData) {
        String decodeReqData = EncryptUtil.decodeUTF8StringBase64(reqData);
        ModifyProjectRequest request = JSON.parseObject(decodeReqData, ModifyProjectRequest.class);
        String requestNo = request.getRequestNo();

        update(Wrappers.<Project>update().lambda().eq(Project::getProjectNo, request.getProjectNo())
                .set(Project::getProjectStatus, request.getProjectStatus()));

        return new ModifyProjectResponse(requestNo);
    }

    @Override
    public Project getByProjectNo(String projectNo) {
        return getOne(Wrappers.<Project>query().lambda().eq(Project::getProjectNo, projectNo), false);
    }
}

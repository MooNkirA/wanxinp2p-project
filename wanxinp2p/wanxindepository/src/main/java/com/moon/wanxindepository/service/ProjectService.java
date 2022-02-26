package com.moon.wanxindepository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxindepository.entity.Project;
import com.moon.wanxindepository.model.CreateProjectResponse;
import com.moon.wanxindepository.model.ModifyProjectResponse;

/**
 * 标的信息表 服务类
 */
public interface ProjectService extends IService<Project> {

    /**
     * 创建标的
     *
     * @param reqData 业务数据
     * @return
     */
    CreateProjectResponse createProject(String reqData);

    /**
     * 更新标的状态
     *
     * @param reqData 业务数据
     * @return
     */
    ModifyProjectResponse modifyProject(String reqData);

    /**
     * 根据标的编号获取标的信息
     *
     * @param projectNo
     * @return
     */
    Project getByProjectNo(String projectNo);

}

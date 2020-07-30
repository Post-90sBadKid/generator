package com.wry.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wry.generator.dto.ProjectInfoDTO;
import com.wry.generator.entity.ProjectInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
public interface ProjectInfoService extends IService<ProjectInfo> {

    PageInfo findProjectInfoList(ProjectInfoDTO projectInfoDTO);

}

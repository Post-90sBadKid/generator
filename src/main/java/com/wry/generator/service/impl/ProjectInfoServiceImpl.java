package com.wry.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wry.generator.dto.ProjectInfoDTO;
import com.wry.generator.entity.DataType;
import com.wry.generator.entity.ProjectInfo;
import com.wry.generator.mapper.ProjectInfoMapper;
import com.wry.generator.service.ProjectInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements ProjectInfoService {

    @Resource
    private ProjectInfoMapper projectInfoMapper;

    @Override
    public PageInfo findProjectInfoList(ProjectInfoDTO projectInfoDTO) {
        if (!Objects.isNull(projectInfoDTO.getPageNum()) || !Objects.isNull(projectInfoDTO.getPageSize())) {
            PageHelper.startPage(projectInfoDTO.getPageNum(), projectInfoDTO.getPageSize());
        }
        List list = projectInfoMapper.selectProjectInfoList(projectInfoDTO.getSearchValue());
        return new PageInfo(list);
    }
}

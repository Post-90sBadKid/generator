package com.wry.generator.controller;

import com.wry.generator.dto.ProjectInfoDTO;
import com.wry.generator.entity.ProjectInfo;
import com.wry.generator.result.Result;
import com.wry.generator.service.ProjectInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 项目配置信息 控制层
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/19
 */
@RestController
@RequestMapping("/projectInfo")
@Api(tags = "项目配置信息")
public class ProjectInfoController {
    @Resource
    private ProjectInfoService projectInfoService;

    @PostMapping("/list")
    @ApiOperation("获取所有项目配置信息")
    public Result getProjectInfo(@Validated @RequestBody ProjectInfoDTO projectInfoDTO) {
        return Result.success(projectInfoService.findProjectInfoList(projectInfoDTO));
    }

    @PostMapping("/save")
    @ApiOperation("添加项目配置信息")
    public Result saveProjectInfo(@Validated @RequestBody ProjectInfo projectInfo) {
        return Result.success(projectInfoService.save(projectInfo));
    }

    @PutMapping("/update")
    @ApiOperation("修改项目配置信息")
    public Result updateProjectInfo(@Validated @RequestBody ProjectInfo projectInfo) {
        return Result.success(projectInfoService.updateById(projectInfo));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除项目配置信息")
    public Result removeProjectInfo(@RequestBody String id) {
        return Result.success(projectInfoService.removeById(id));
    }



}

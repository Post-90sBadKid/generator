package com.wry.generator.controller;

import com.wry.generator.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/23
 */
@RestController
@RequestMapping("/template")
@Api(tags = "模板信息")
public class TemplateController {

    @PostMapping("/list")
    @ApiOperation("获取模板集合")
    public Result saveProjectInfo() throws FileNotFoundException {
        String templatePath = Thread.currentThread().getContextClassLoader().getResource("template").getPath();
        File file= new File(templatePath);
        String[] list = file.list();
        return Result.success(list);
    }

}

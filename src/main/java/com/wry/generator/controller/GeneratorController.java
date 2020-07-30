package com.wry.generator.controller;

import com.github.pagehelper.PageInfo;
import com.wry.generator.dto.GeneratorCodeDTO;
import com.wry.generator.dto.GeneratorSearchDTO;
import com.wry.generator.result.Result;
import com.wry.generator.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;


/**
 * <p>
 * 代码生成 控制层
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/19
 */
@RestController
@RequestMapping("/generator")
@Api(tags = "代码生成")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation("获取当前数据源的表集合")
    public Result list(@Validated @RequestBody GeneratorSearchDTO generatorDTO) {

        PageInfo info = generatorService.findTableList(generatorDTO);
        return Result.success(info);
    }

    /**
     * 生成代码
     */
    @PostMapping("/code")
    @ApiOperation("生成代码")
    public byte[] code(@Validated @RequestBody GeneratorCodeDTO generatorDTO, HttpServletResponse response) throws IOException {

        byte[] data = generatorService.generatorCode(generatorDTO);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=code" + System.currentTimeMillis() + ".zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
//        IOUtils.write(data, response.getOutputStream());
        return data;
    }
}

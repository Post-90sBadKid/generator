package com.wry.generator.controller;

import com.wry.generator.dto.DataTypeDTO;
import com.wry.generator.entity.DataType;
import com.wry.generator.result.Result;
import com.wry.generator.service.DataTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 数据类型 控制层
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/19
 */
@RestController
@RequestMapping("/dataType")
@Api(tags = "数据库类型信息")
public class DataTypeController {
    @Resource
    private DataTypeService dataTypeService;

    @PostMapping("/list")
    @ApiOperation("获取所有数据库类型信息")
    public Result getDataType(@Validated @RequestBody DataTypeDTO dataTypeDTO) {
        return Result.success(dataTypeService.findDataTypeList(dataTypeDTO));
    }

    @PostMapping("/save")
    @ApiOperation("添加数据库类型信息")
    public Result saveDataType(@Validated @RequestBody DataType dataType) {
        return Result.success(dataTypeService.save(dataType));
    }

    @PutMapping("/update")
    @ApiOperation("修改数据库类型信息")
    public Result updateDataType(@Validated @RequestBody DataType dataType) {
        return Result.success(dataTypeService.updateById(dataType));
    }

    @DeleteMapping("/remove")
    @ApiOperation("删除数据库类型信息")
    public Result removeDataType(@RequestBody String id) {
        return Result.success(dataTypeService.removeById(id));
    }

}

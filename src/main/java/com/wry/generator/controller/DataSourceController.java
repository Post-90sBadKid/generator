package com.wry.generator.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.wry.generator.config.DataSourceContextHolder;
import com.wry.generator.config.DynamicDataSource;
import com.wry.generator.dto.DataSourceDTO;
import com.wry.generator.result.Result;
import com.wry.generator.result.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.DriverManager;
import java.util.Map;

/**
 * <p>
 * 数据源 控制层
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
@RestController
@RequestMapping("/dataSource")
@Api(tags = "添加删除数据源")
public class DataSourceController {

    @GetMapping("/list")
    @ApiOperation("获取当前所有数据源")
    public Result now() {
        Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
        return Result.success(dataSourceMap.keySet());
    }

    @PostMapping("/add")
    @ApiOperation("通用添加数据源（推荐）")
    public Result add(@Validated @RequestBody DataSourceDTO dto) {

        Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();

        if (dataSourceMap.containsKey(dto.getPollName())) {
            return Result.error(ResultEnum.DATA_SOURCE_KEY_EXISTS);
        }
        try {
            // 排除连接不上的错误
            Class.forName(dto.getDriverClassName());
            // 相当于连接数据库
            DriverManager.getConnection(dto.getUrl(), dto.getUsername(), dto.getPassword());

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(ResultEnum.DATA_SOURCE_CONNECTION_ERROR);
        }

        DruidDataSource dynamicDataSource = new DruidDataSource();
        dynamicDataSource.setDriverClassName(dto.getDriverClassName());
        dynamicDataSource.setUrl(dto.getUrl());
        dynamicDataSource.setUsername(dto.getUsername());
        dynamicDataSource.setPassword(dto.getPassword());

        dataSourceMap.put(dto.getPollName(), dynamicDataSource);
        DynamicDataSource.getInstance().setTargetDataSources(dataSourceMap);
        return Result.success(dataSourceMap.keySet());
    }


    @DeleteMapping("/remove")
    @ApiOperation("删除数据源")
    public Result remove(@RequestBody String pollName) {
        Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
        dataSourceMap.remove(pollName);
        DataSourceContextHolder.clearPollName();
        return Result.success();
    }
}

package com.wry.generator.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wry.generator.config.DataSourceContextHolder;
import com.wry.generator.config.DynamicDataSource;
import com.wry.generator.config.GeneratorMapperFactory;
import com.wry.generator.dto.GeneratorCodeDTO;
import com.wry.generator.dto.GeneratorSearchDTO;
import com.wry.generator.entity.DataType;
import com.wry.generator.entity.ProjectInfo;
import com.wry.generator.exception.CustomException;
import com.wry.generator.mapper.DataTypeMapper;
import com.wry.generator.mapper.GeneratorMapper;
import com.wry.generator.mapper.ProjectInfoMapper;
import com.wry.generator.result.ResultEnum;
import com.wry.generator.service.GeneratorService;
import com.wry.generator.util.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;


/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {


    @Resource
    private DataTypeMapper dataTypeMapper;
    @Resource
    private ProjectInfoMapper projectInfoMapper;
    @Resource
    private GeneratorMapperFactory generatorMapperFactory;

    @Override
    public PageInfo findTableList(GeneratorSearchDTO generatorDTO) {
        //设置使用的数据源
        DataSourceContextHolder.setPollName(generatorDTO.getPollName());
        //判断是否存在当前数据源
        if (!DynamicDataSource.getInstance().getDataSourceMap().containsKey(generatorDTO.getPollName())) {
            throw new CustomException(ResultEnum.CURRENT_DATASOURCE_NOT_EXISTS);
        }
        List<Map<String, String>> list = null;
        try {
            GeneratorMapper generatorMapper = generatorMapperFactory.getGeneratorMapper(generatorDTO.getDbType(), generatorDTO.getPollName());
            PageHelper.startPage(generatorDTO.getPageNum(), generatorDTO.getPageSize());
            list = generatorMapper.selectTableList(generatorDTO.getTableName());
        } catch (Exception e) {
            throw new CustomException(ResultEnum.CURRENT_DATASOURCE_NOT_SUPPORTED);
        }
        //设置默认的数据源
        DataSourceContextHolder.clearPollName();

        return new PageInfo(list);
    }


    @Override
    public byte[] generatorCode(@Validated @RequestBody GeneratorCodeDTO generatorDTO) {
        ProjectInfo projectInfo = projectInfoMapper.selectById(generatorDTO.getProjectInfoId());
        List<DataType> dataTypeList = dataTypeMapper.selectDataTypeList(null);
        //设置使用的数据源
        DataSourceContextHolder.setPollName(generatorDTO.getPollName());
        GeneratorMapper generatorMapper = generatorMapperFactory.getGeneratorMapper(generatorDTO.getDbType(), generatorDTO.getPollName());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : generatorDTO.getTables().split(",")) {
            //查询表信息
            Map<String, String> table = generatorMapper.selectTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = generatorMapper.selectTableColumns(tableName);
            //类型关系转Map 【key：数据库类型，value：java类型】
            Map<String, String> dataTypeMap = new HashMap<>();
            dataTypeList.forEach(item -> {
                dataTypeMap.put(item.getDbType(), item.getJavaType());
            });
            //生成代码
            GenUtils.generatorCode(table, columns, projectInfo, dataTypeMap, generatorDTO.getTemplatePath(), zip);
        }

        IOUtils.closeQuietly(zip);
        //设置默认的数据源
        DataSourceContextHolder.clearPollName();
        return outputStream.toByteArray();
    }


}

package com.wry.generator.service.impl;

import com.wry.generator.config.DataSourceContextHolder;
import com.wry.generator.config.DynamicDataSource;
import com.wry.generator.dto.GeneratorCodeDTO;
import com.wry.generator.dto.GeneratorSearchDTO;
import com.wry.generator.entity.DataType;
import com.wry.generator.entity.ProjectInfo;
import com.wry.generator.entity.Table;
import com.wry.generator.exception.CustomException;
import com.wry.generator.mapper.DataTypeMapper;
import com.wry.generator.mapper.ProjectInfoMapper;
import com.wry.generator.result.ResultEnum;
import com.wry.generator.service.GeneratorService;
import com.wry.generator.util.GenUtils;
import com.wry.generator.util.MetadataUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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


    @Override
    public List<Table> findTableList(GeneratorSearchDTO generatorDTO) throws Exception {
        //判断是否存在当前数据源
        if (!DynamicDataSource.getInstance().getDataSourceMap().containsKey(generatorDTO.getPollName())) {
            throw new CustomException(ResultEnum.CURRENT_DATASOURCE_NOT_EXISTS);
        }
        //设置使用的数据源
        DataSourceContextHolder.setPollName(generatorDTO.getPollName());
        List<Table> tableList = MetadataUtils.getData();
        List<DataType> dataBaseTyspeInfo = MetadataUtils.getDataBaseTyspeInfo();
        //设置默认的数据源
        DataSourceContextHolder.clearPollName();
        Map map = new HashMap();
        dataBaseTyspeInfo.forEach(info -> {
            map.put("db_type", info.getDbType());
            List list = dataTypeMapper.selectByMap(map);
            if (list == null || list.isEmpty()) {
                dataTypeMapper.insert(info);
            }
        });
        return tableList;
    }


    @Override
    public byte[] generatorCode(@Validated @RequestBody GeneratorCodeDTO generatorDTO) throws SQLException {
        ProjectInfo projectInfo = projectInfoMapper.selectById(generatorDTO.getProjectInfoId());
        List<DataType> dataTypeList = dataTypeMapper.selectDataTypeList(null);
        //类型关系转Map 【key：数据库类型，value：java类型】
        Map<String, String> dataTypeMap = new HashMap<>();
        dataTypeList.forEach(item ->
                dataTypeMap.put(item.getDbType(), item.getJavaType())
        );
        //设置使用的数据源
        DataSourceContextHolder.setPollName(generatorDTO.getPollName());
        //前端传过来的表面
        String[] split = generatorDTO.getTables().split(",");
        //元数据获取的
        List<Table> tableList = MetadataUtils.getData();
        //求交集
        List<Table> tables = tableList.stream()
                .filter(table ->
                        Stream.of(split)
                                .anyMatch(
                                        item -> Objects.equals(table.getTableName(), item)
                                )
                ).collect(Collectors.toList());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        //生成代码
        tables.forEach(table -> {
            GenUtils.generatorCode(table, projectInfo, dataTypeMap, generatorDTO.getTemplatePath(), zip);
        });

        //设置默认的数据源
        DataSourceContextHolder.clearPollName();
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}

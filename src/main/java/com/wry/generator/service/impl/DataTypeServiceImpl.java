package com.wry.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wry.generator.dto.DataTypeDTO;
import com.wry.generator.entity.DataType;
import com.wry.generator.mapper.DataTypeMapper;
import com.wry.generator.service.DataTypeService;
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
public class DataTypeServiceImpl extends ServiceImpl<DataTypeMapper, DataType> implements DataTypeService {
    @Resource
    private DataTypeMapper dataTypeMapper;

    @Override
    public PageInfo findDataTypeList(DataTypeDTO dataTypeDTO) {
        if (!Objects.isNull(dataTypeDTO.getPageNum())|| !Objects.isNull(dataTypeDTO.getPageSize())) {
            PageHelper.startPage(dataTypeDTO.getPageNum(), dataTypeDTO.getPageSize());
        }
        List<DataType> list = dataTypeMapper.selectDataTypeList(dataTypeDTO.getSearchValue());
        return new PageInfo(list);
    }
}

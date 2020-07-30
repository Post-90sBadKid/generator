package com.wry.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wry.generator.entity.DataType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据库类型字典
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/19
 */
@Mapper
public interface DataTypeMapper extends BaseMapper<DataType> {

    List<DataType> selectDataTypeList(@Param("searchValue") String searchValue);

}
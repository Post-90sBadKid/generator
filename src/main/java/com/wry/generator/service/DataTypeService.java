package com.wry.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.wry.generator.dto.DataTypeDTO;
import com.wry.generator.entity.DataType;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
public interface DataTypeService extends IService<DataType> {

    /**
     * 查询所有的数据库和Java类型关系
     *
     * @param dataTypeDTO 查询条件
     * @return 分页对象
     */
    PageInfo findDataTypeList(DataTypeDTO dataTypeDTO);
}

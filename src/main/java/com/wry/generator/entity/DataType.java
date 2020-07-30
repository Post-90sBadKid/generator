package com.wry.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wry.generator.dto.PageDTO;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/19
 */
@Data
@TableName(value = "data_type")
public class DataType  {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "db_type")
    private String dbType;

    @TableField(value = "java_type")
    private String javaType;


}
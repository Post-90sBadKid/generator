package com.wry.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/24
 */
@Data
@TableName(value = "project_info")
public class ProjectInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "main_path")
    private String mainPath;

    @TableField(value = "package_name")
    private String packageName;

    @TableField(value = "module_name")
    private String moduleName;

    @TableField(value = "author")
    private String author;

    @TableField(value = "email")
    private String email;

    @TableField(value = "table_prefix")
    private String tablePrefix;

}
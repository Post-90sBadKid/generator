package com.wry.generator.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
@Data
public class GeneratorSearchDTO extends PageDTO {

    @ApiModelProperty(value = "数据库类型", example = "pollName", required = true)
    private String pollName;

    @ApiModelProperty(value = "数据库类型", example = "mysql", required = true)
    private String dbType;

    @ApiModelProperty(value = "查询条件的表名", example = "log")
    private String tableName;


}

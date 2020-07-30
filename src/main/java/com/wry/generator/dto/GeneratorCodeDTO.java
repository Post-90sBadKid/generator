package com.wry.generator.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
@Data
public class GeneratorCodeDTO {

    @NotBlank
    @ApiModelProperty(value = "连接池名称(唯一)", example = "test")
    private String pollName;

    @NotBlank
    @ApiModelProperty(value = "数据库类型", example = "text")
    private String dbType;

    @NotBlank
    @ApiModelProperty(value = "项目配置信息", example = "1")
    private String projectInfoId;

    @NotBlank
    @ApiModelProperty(value = "模板路径", example = "blog")
    private String templatePath;

    @NotBlank
    @ApiModelProperty(value = "生成代码的表", example = "log,user")
    private String tables;
}

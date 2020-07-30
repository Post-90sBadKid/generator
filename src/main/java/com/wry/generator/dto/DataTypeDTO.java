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
public class DataTypeDTO extends PageDTO {

    @ApiModelProperty(value = "筛选条件", example = "")
    private String searchValue;

}

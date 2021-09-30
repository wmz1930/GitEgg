package com.gitegg.code.generator.engine.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 数据源表
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-02 18:09:28
 */
@Data
@ApiModel(value="TableDTO对象", description="数据源表")
public class TableDTO {

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表描述")
    private String tableComment;

}

package com.gitegg.code.generator.field.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author GitEgg
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TableFieldDTO", description="TableFieldDTO")
public class TableFieldDTO extends BaseEntity {

    @ApiModelProperty(value = "关联表主键")
    private Long joinId;

    @ApiModelProperty(value = "表名")
    private String joinTableName;

    @ApiModelProperty(value = "字段列表")
    private List<FieldDTO> fieldDTOList;
}

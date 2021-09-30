package com.gitegg.code.generator.engine.dto;

import com.gitegg.code.generator.config.dto.QueryConfigDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wanglei
 */
@Data
@ApiModel(value="QueryFieldDTO对象", description="查询表字段")
public class QueryTableFieldDTO implements Serializable {

    private static final long serialVersionUID = -6908350866963781362L;

    @ApiModelProperty(value = "配置")
    private Long datasourceId;

    @ApiModelProperty(value = "表列表")
    private List<String> tableList;
}

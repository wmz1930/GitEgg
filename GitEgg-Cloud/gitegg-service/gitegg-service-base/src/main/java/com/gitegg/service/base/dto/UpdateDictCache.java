package com.gitegg.service.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 修改数据字典缓存
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
@Data
@ApiModel(value = "UpdateDictCache", description = "数据字典缓存")
public class UpdateDictCache {
    
    @ApiModelProperty(value = "主键id")
    private Long id;
    
    @ApiModelProperty(value = "租户")
    private Long tenantId;
    
    @ApiModelProperty(value = "字典类型")
    private Long parentId;
    
    @ApiModelProperty(value = "所有上级id的集合")
    private String ancestors;
    
    @ApiModelProperty(value = "字典名称")
    private String dictName;
    
    @ApiModelProperty(value = "字典值")
    private String dictCode;
    
    @ApiModelProperty(value = "字典值旧值")
    private String oldDictCode;
    
    @ApiModelProperty(value = "排序")
    private Integer dictOrder;
    
    @ApiModelProperty(value = "1有效，0禁用")
    private Integer dictStatus;
}

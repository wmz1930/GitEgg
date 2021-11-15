
package com.gitegg.service.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 查询用户权限
 * </p>
 *
 * @author gitegg
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "QueryUserResourceDTO", description = "查询用户权限")
public class QueryUserResourceDTO implements Serializable
{
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "资源权限类型")
    private List<String> resourceTypeList;

}


package com.gitegg.service.system.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 角色更新
 * </p>
 *
 * @author gitegg
 * @since 2019-05-19
 */
@Data
@ApiModel(value = "UpdateRole对象", description = "更新角色时的对象")
public class UpdateRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String roleKey;

    @ApiModelProperty(value = "角色级别")
    private Integer roleLevel;

    @ApiModelProperty(value = "1有效，0禁用")
    private Integer roleStatus;

    @ApiModelProperty(value = "备注")
    private String comments;

}

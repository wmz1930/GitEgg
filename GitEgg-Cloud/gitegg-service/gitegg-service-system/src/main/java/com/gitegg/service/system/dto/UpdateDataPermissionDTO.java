package com.gitegg.service.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author gitegg
 */
@Data
@ApiModel(value = "UpdateDataPermission对象", description = "更新数据权限")
public class UpdateDataPermissionDTO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "需要添加的数据权限")
    private List<Long> addDataPermissions;

    @ApiModelProperty(value = "需要删除的数据权限")
    private List<Long> removeDataPermissions;
}

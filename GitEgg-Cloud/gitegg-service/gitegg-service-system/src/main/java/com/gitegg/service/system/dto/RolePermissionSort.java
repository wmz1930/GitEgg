package com.gitegg.service.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GitEgg
 * @date 2021-05-15 22:40:31
 **/
@Data
public class RolePermissionSort {

    @ApiModelProperty(value = "序号")
    private int index;

    @ApiModelProperty(value = "数据权限类型")
    private String dataPermissionType;
}
